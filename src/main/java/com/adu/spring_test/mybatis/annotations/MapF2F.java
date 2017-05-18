package com.adu.spring_test.mybatis.annotations;

import java.lang.annotation.*;

/**
 * 将查询结果映射成map的注解，其中第一个字段为key，第二个字段为value.
 * <p>
 * 注：返回类型必须为{@link java.util.Map Map<K, V>}。K/V的类型通过MyBatis的TypeHander进行类型转换，如有必要可自定义TypeHander。
 *
 * @author yunjie.du
 * @date 2016/12/22 18:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MapF2F {
    boolean isUnique() default true;//是否要求key无重复。如果要求无重复，而实际结果出现了重复，会抛出org.springframework.dao.DuplicateKeyException。
}
