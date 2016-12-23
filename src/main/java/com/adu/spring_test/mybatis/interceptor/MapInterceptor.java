package com.adu.spring_test.mybatis.interceptor;

import java.lang.reflect.Method;
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
                    return result2Map(statement);
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
     * 将查询结果映射成Map，其中第一个字段作为key，第二个字段作为value.
     * 
     * @param statement
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }

        List<Object> res = new ArrayList();

        Map<Object, Object> map = new HashMap();
        while (resultSet.next()) {
            Object key = resultSet.getObject(1);
            Object value = resultSet.getObject(2);

            if (key instanceof BigInteger) {// 将BigInteger转为Long处理
                key = ((BigInteger) key).longValue();
            }

            map.put(key, value);// 第一列作为key,第二列作为value。
        }

        res.add(map);
        return res;
    }
}
