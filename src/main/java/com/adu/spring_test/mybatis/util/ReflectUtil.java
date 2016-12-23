package com.adu.spring_test.mybatis.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 */
public class ReflectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 利用反射获取指定对象的指定属性
     * 
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, String fieldName) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return (T) field.get(obj);
            } catch (Exception e) {
                logger.error("[ERROR-getFieldValue]obj={},fieldName={}", obj, fieldName, e);
            }
        }

        return null;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     * 
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;

        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

}