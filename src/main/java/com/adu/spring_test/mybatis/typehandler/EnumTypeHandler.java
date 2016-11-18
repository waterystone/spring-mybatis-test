package com.adu.spring_test.mybatis.typehandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.adu.spring_test.mybatis.enums.BaseEnum;

/**
 * mapper里字段到枚举类的映射。
 * 用法一:
 * 入库：#{enumDataField, typeHandler=com.adu.spring_test.mybatis.typehandler.EnumTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="enumDataField" column="enum_data_field" javaType="com.xxx.MyEnum" typeHandler="com.adu.spring_test.mybatis.typehandler.EnumTypeHandler"/>
 * </resultMap>
 *
 * 用法二：
 * 1）在mybatis-config.xml中指定handler:
 *      <typeHandlers>
 *              <typeHandler handler="com.adu.spring_test.mybatis.typehandler.EnumTypeHandler" javaType="com.xxx.MyEnum"/>
 *      </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 */
public class EnumTypeHandler<E extends BaseEnum> extends BaseTypeHandler<BaseEnum> {
    private Method codeOf;//MyEnum类要有static E codeOf(int)方法!!!!

    public EnumTypeHandler(Class<E> enumType) {
        if (enumType == null)
            throw new IllegalArgumentException("Type argument cannot be null");

        String className = enumType.getName();

        try {
            this.codeOf = enumType.getDeclaredMethod("codeOf", new Class[]{Integer.TYPE});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code) required.");
        }

        if(!Modifier.isStatic(this.codeOf.getModifiers())) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code) required.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.code());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return  this.codeOf(rs.getInt(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return  this.codeOf(rs.getInt(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return  this.codeOf(cs.getInt(columnIndex));
    }


    private E codeOf(int code) {
        try {
            return (E) this.codeOf.invoke((Object) null, new Object[]{Integer.valueOf(code)});
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

}
