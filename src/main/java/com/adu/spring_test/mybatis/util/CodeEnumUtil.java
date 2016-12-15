package com.adu.spring_test.mybatis.util;

import com.adu.spring_test.mybatis.enums.CodeBaseEnum;

/**
 * @author yunjie.du
 * @date 2016/12/15 16:20
 */
public class CodeEnumUtil {
    /**
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends Enum<?> & CodeBaseEnum> E codeOf(Class<E> enumClass, int code) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.code() == code)
                return e;
        }
        return null;
    }
}
