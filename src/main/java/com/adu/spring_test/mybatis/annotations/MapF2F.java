package com.adu.spring_test.mybatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将查询结果映射成map的注解，其中第一个字段为key，第二个字段为value. <br/>
 * 注：返回类型必须为java.util.Map<K, V>
 *
 * @author yunjie.du
 * @date 2016/12/22 18:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MapF2F {
}
