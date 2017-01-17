package com.adu.spring_test.mybatis.interceptor;

import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

/**
 * @author yunjie.du
 * @date 2017/1/17 15:32
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }))
public class SlowQueryTimeInterceptor implements Interceptor {
    private int slowMilliSecond = 100;
    private Logger logger = LoggerFactory.getLogger(SlowQueryTimeInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object proceed = invocation.proceed();
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        //判断是否慢查询
        if (elapsed > slowMilliSecond) {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object params = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            BoundSql boundSql = mappedStatement.getBoundSql(params);
            logger.warn("[DB_SLOW_QUERY]method={},sql={},params={},rowBounds=[{},{}],elapsed={}ms", mappedStatement.getId(),
                    removeBreakingWhitespace(boundSql.getSql()), params, rowBounds.getOffset(),rowBounds.getLimit(), elapsed);
        }

        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String slowMilliSecondStr = properties.getProperty("slowMilliSecond", "1000");
        this.slowMilliSecond = Integer.valueOf(slowMilliSecondStr);
    }

    /**
     * 去除换行符等
     * 
     * @param original
     * @return
     */
    private String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }
}
