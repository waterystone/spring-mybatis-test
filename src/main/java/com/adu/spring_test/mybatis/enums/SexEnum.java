package com.adu.spring_test.mybatis.enums;

/**
 * @author yunjie.du
 * @date 2016/9/27 15:24
 */
public enum SexEnum implements BaseEnum {
    UNKNOWN(0,"未知"),
    MAN(1, "男士"),
    WOMAN(2, "女士");

    private final int code;
    private final String desc;

    SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SexEnum codeOf(int code) {
        for (SexEnum e : SexEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return UNKNOWN;
    }

    public int code() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
