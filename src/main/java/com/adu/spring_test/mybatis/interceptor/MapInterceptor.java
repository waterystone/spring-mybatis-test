package com.adu.spring_test.mybatis.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adu.spring_test.mybatis.annotations.MapF2F;
import com.adu.spring_test.mybatis.util.ReflectUtil;

import javafx.util.Pair;

/**
 * MapF2F的拦截器
 * 
 * @author yunjie.du
 * @date 2016/12/22 18:44
 */
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = { Statement.class }))
public class MapInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(MapInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        if (target instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) target;
            MappedStatement mappedStatement = ReflectUtil.getFieldValue(defaultResultSetHandler, "mappedStatement");

            String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");// 当前类
            String methodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");// 当前方法

            Method[] methods = Class.forName(className).getDeclaredMethods();// 该类所有声明的方法

            if (methods == null) {
                return invocation.proceed();
            }

            for (Method method : methods) {
                if (StringUtils.equals(method.getName(), methodName)) {// 找到当前方法的Method
                    MapF2F mapF2F = method.getAnnotation(MapF2F.class);// 获取当前方法是否有MapF2F注解
                    if (mapF2F == null) {
                        return invocation.proceed();
                    }

                    // 如果MapF2F注解，则这里对结果进行转换。
                    Statement statement = (Statement) invocation.getArgs()[0];
                    Pair<Type, Type> kvTypePair = getKVTypeOfReturnMap(method);
                    return result2Map(statement, kvTypePair);
                }
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取函数返回Map中key-value的类型
     * 
     * @param method
     * @return left为key的类型，right为value的类型
     */
    private Pair<Type, Type> getKVTypeOfReturnMap(Method method) {
        Type returnType = method.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (!Map.class.equals(parameterizedType.getRawType())) {
                throw new RuntimeException(
                        "[ERROR-MapF2F-return-map-type]使用MapF2F,返回类型必须是java.util.Map类型！！！method=" + method);
            }

            return new Pair<>(parameterizedType.getActualTypeArguments()[0],
                    parameterizedType.getActualTypeArguments()[1]);
        }
        return null;
    }

    /**
     * 将查询结果映射成Map，其中第一个字段作为key，第二个字段作为value.
     * 
     * @param statement
     * @param kvTypePair 函数指定返回Map key-value的类型(有些类型需要处理，比如BigInteger-->Long)
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement, Pair<Type, Type> kvTypePair) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }

        List<Object> res = new ArrayList();

        Map<Object, Object> map = new HashMap();
        while (resultSet.next()) {
            Object key = resultSet.getObject(1);
            Object value = resultSet.getObject(2);

            key = convertType(key, kvTypePair.getKey());
            value = convertType(value, kvTypePair.getValue());

            map.put(key, value);// 第一列作为key,第二列作为value。
        }

        res.add(map);
        return res;
    }

    /**
     * 有些DB字段到KV类型的映射需要手工转换，不然后续get会出问题！！！
     * 
     * @param obj Mybatis自己将字段转过来的对象
     * @param type 函数返回map内指定的类型
     * @return
     */
    private Object convertType(Object obj, Type type) {
        // 如果DB中使用bigint而KV指定基本数值类型，就需要人工将BigInteger转为基本数值类型处理
        if (obj instanceof BigInteger) {
            if (Long.class.equals(type)) {// bigint-->Long
                return Long.valueOf(((BigInteger) obj).longValue());
            }

            if (Integer.class.equals(type)) {// bigint-->Integer
                return Integer.valueOf(((BigInteger) obj).intValue());
            }
        }

        return obj;
    }
}
