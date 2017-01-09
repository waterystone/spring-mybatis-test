package com.adu.spring_test.mybatis.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
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
public class MapF2FInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(MapF2FInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        if (target instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) target;
            MappedStatement mappedStatement = ReflectUtil.getFieldValue(defaultResultSetHandler, "mappedStatement");

            String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");// 当前类
            String currentMethodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");// 当前方法

            Method method = findMethod(className, currentMethodName);// 获取当前Method

            if (method == null || method.getAnnotation(MapF2F.class) == null) {// 如果当前Method没有注解MapF2F
                return invocation.proceed();
            }

            // 如果有MapF2F注解，则这里对结果进行拦截并转换
            Statement statement = (Statement) invocation.getArgs()[0];
            Pair<Class<?>, Class<?>> kvTypePair = getKVTypeOfReturnMap(method);// 获取返回Map里key-value的类型
            TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();// 获取各种TypeHander的注册器
            return result2Map(statement, typeHandlerRegistry, kvTypePair);
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
     * 找到与指定函数名匹配的Method。
     *
     * @param className
     * @param targetMethodName
     * @return
     * @throws Throwable
     */
    private Method findMethod(String className, String targetMethodName) throws Throwable {
        Method[] methods = Class.forName(className).getDeclaredMethods();// 该类所有声明的方法
        if (methods == null) {
            return null;
        }

        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), targetMethodName)) {
                return method;
            }
        }

        return null;
    }

    /**
     * 获取函数返回Map中key-value的类型
     * 
     * @param mapF2FMethod
     * @return left为key的类型，right为value的类型
     */
    private Pair<Class<?>, Class<?>> getKVTypeOfReturnMap(Method mapF2FMethod) {
        Type returnType = mapF2FMethod.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (!Map.class.equals(parameterizedType.getRawType())) {
                throw new RuntimeException(
                        "[ERROR-MapF2F-return-map-type]使用MapF2F,返回类型必须是java.util.Map类型！！！method=" + mapF2FMethod);
            }

            return new Pair<>((Class<?>) parameterizedType.getActualTypeArguments()[0],
                    (Class<?>) parameterizedType.getActualTypeArguments()[1]);
        }

        return new Pair<>(null, null);
    }

    /**
     * 将查询结果映射成Map，其中第一个字段作为key，第二个字段作为value.
     * 
     * @param statement
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param kvTypePair 函数指定返回Map key-value的类型
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement, TypeHandlerRegistry typeHandlerRegistry,
            Pair<Class<?>, Class<?>> kvTypePair) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        List<Object> res = new ArrayList();
        Map<Object, Object> map = new HashMap();

        while (resultSet.next()) {
            Object key = this.getObject(resultSet, 1, typeHandlerRegistry, kvTypePair.getKey());
            Object value = this.getObject(resultSet, 2, typeHandlerRegistry, kvTypePair.getValue());

            map.put(key, value);// 第一列作为key,第二列作为value。
        }

        res.add(map);
        return res;
    }

    /**
     * 结果类型转换。
     * <p>
     * 这里借用注册在MyBatis的typeHander（包括自定义的），方便进行类型转换。
     * 
     * @param resultSet
     * @param columnIndex 字段下标，从1开始
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param javaType 要转换的Java类型
     * @return
     * @throws SQLException
     */
    private Object getObject(ResultSet resultSet, int columnIndex, TypeHandlerRegistry typeHandlerRegistry,
            Class<?> javaType) throws SQLException {
        final TypeHandler<?> typeHandler = typeHandlerRegistry.hasTypeHandler(javaType)
                ? typeHandlerRegistry.getTypeHandler(javaType) : typeHandlerRegistry.getUnknownTypeHandler();

        return typeHandler.getResult(resultSet, columnIndex);

    }

}
