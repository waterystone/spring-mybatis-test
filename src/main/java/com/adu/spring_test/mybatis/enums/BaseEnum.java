package com.adu.spring_test.mybatis.enums;

public interface BaseEnum {
    int code();

    // 实现类还必须实现static *Enum codeOf(int code)方法，以方便mybatis的typeHandler统一处理。
}
