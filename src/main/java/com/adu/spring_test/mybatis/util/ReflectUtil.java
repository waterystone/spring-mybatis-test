package com.adu.spring_test.mybatis.util;

import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 */
public class ReflectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 分离最后一个代理的目标对象
     * 
     * @param invocation
     * @return
     */
    public static MetaObject getRealTarget(Invocation invocation) {
        MetaObject metaStatementHandler = SystemMetaObject.forObject(invocation.getTarget());

        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }

        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }

        return metaStatementHandler;
    }

}