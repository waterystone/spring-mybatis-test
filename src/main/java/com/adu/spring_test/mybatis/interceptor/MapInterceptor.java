package com.adu.spring_test.mybatis.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
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
import com.mysql.jdbc.SQLError;

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

            Method method = getMapF2FMethod(className, methodName);

            if (method == null) {
                return invocation.proceed();
            }

            // 如果MapF2F注解，则这里对结果进行转换。
            Statement statement = (Statement) invocation.getArgs()[0];
            Pair<Class<?>, Class<?>> kvTypePair = getKVTypeOfReturnMap(method);// 获取返回Map里key-value的类型。
            return result2Map(statement, kvTypePair);
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
     * 找到与指定函数名匹配，且注解MapF2F的函数。
     * 
     * @param className
     * @param methodName
     * @return
     * @throws Throwable
     */
    private Method getMapF2FMethod(String className, String methodName) throws Throwable {
        Method[] methods = Class.forName(className).getDeclaredMethods();// 该类所有声明的方法
        if (methods == null) {
            return null;
        }

        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), methodName)) {
                if (method.getAnnotation(MapF2F.class) != null) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * 获取函数返回Map中key-value的类型
     * 
     * @param method
     * @return left为key的类型，right为value的类型
     */
    private Pair<Class<?>, Class<?>> getKVTypeOfReturnMap(Method method) {
        Type returnType = method.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (!Map.class.equals(parameterizedType.getRawType())) {
                throw new RuntimeException(
                        "[ERROR-MapF2F-return-map-type]使用MapF2F,返回类型必须是java.util.Map类型！！！method=" + method);
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
     * @param kvTypePair 函数指定返回Map key-value的类型(有些类型需要处理，比如BigInteger-->Long)
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement, Pair<Class<?>, Class<?>> kvTypePair) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            return null;
        }

        List<Object> res = new ArrayList();

        Map<Object, Object> map = new HashMap();

        while (resultSet.next()) {
            Object key = this.getObject(resultSet, 1, kvTypePair.getKey());
            Object value = this.getObject(resultSet, 2, kvTypePair.getValue());

            map.put(key, value);// 第一列作为key,第二列作为value。
        }

        res.add(map);
        return res;
    }

    /**
     * 结果类型转换。参考：ResultSetImpl.getObject(int columnIndex, Class<T> type)。<br/>
     * 注:新jdbc的ResultSet接口是有public <T> T getObject(int columnIndex, Class<T>
     * type)方法的，但commons-dbcp（DelegatingResultSet）、druid（DruidPooledResultSet）的实现类并没有实现这一方法；而spring（DriverManagerDataSource）、C3P0（ComboPooledDataSource）,其用的JDBC4ResultSet是支持public
     * <T> T getObject(int columnIndex, Class<T> type)方法的。<br/>
     * 这里为了本类的通用，所以统一实现了本方法。
     * 
     * @param resultSet
     * @param columnIndex
     * @param type
     * @return
     * @throws SQLException
     */
    private Object getObject(ResultSet resultSet, int columnIndex, Class<?> type) throws SQLException {
        if (type == null) {
            return resultSet.getObject(columnIndex);
        } else if (type.equals(String.class)) {
            return resultSet.getString(columnIndex);
        } else if (type.equals(BigDecimal.class)) {
            return resultSet.getBigDecimal(columnIndex);
        } else if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
            if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                if (!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                    if (!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                        if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                            if (type.equals(byte[].class)) {
                                return resultSet.getBytes(columnIndex);
                            } else if (type.equals(Date.class)) {
                                return resultSet.getDate(columnIndex);
                            } else if (type.equals(Time.class)) {
                                return resultSet.getTime(columnIndex);
                            } else if (type.equals(Timestamp.class)) {
                                return resultSet.getTimestamp(columnIndex);
                            } else if (type.equals(com.mysql.jdbc.Clob.class)) {
                                return resultSet.getClob(columnIndex);
                            } else if (type.equals(com.mysql.jdbc.Blob.class)) {
                                return resultSet.getBlob(columnIndex);
                            } else if (type.equals(Array.class)) {
                                return resultSet.getArray(columnIndex);
                            } else if (type.equals(Ref.class)) {
                                return resultSet.getRef(columnIndex);
                            } else if (type.equals(URL.class)) {
                                return resultSet.getURL(columnIndex);
                            } else {
                                try {
                                    return resultSet.getObject(columnIndex);
                                } catch (ClassCastException var5) {
                                    SQLException sqlEx = SQLError.createSQLException(
                                            "Conversion not supported for type " + type.getName(), "S1009", null);
                                    sqlEx.initCause(var5);
                                    throw sqlEx;
                                }
                            }
                        } else {
                            return Double.valueOf(resultSet.getDouble(columnIndex));
                        }
                    } else {
                        return Float.valueOf(resultSet.getFloat(columnIndex));
                    }
                } else {
                    return Long.valueOf(resultSet.getLong(columnIndex));
                }
            } else {
                return Integer.valueOf(resultSet.getInt(columnIndex));
            }
        } else {
            return Boolean.valueOf(resultSet.getBoolean(columnIndex));
        }
    }

}
