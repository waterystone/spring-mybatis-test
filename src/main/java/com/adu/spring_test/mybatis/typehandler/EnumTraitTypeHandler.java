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
import com.adu.spring_test.mybatis.util.EnumTraitUtil;

/**
 * mapper里字段到枚举类的映射。
 * 用法一:
 * 入库：#{item.myEnum, typeHandler=com.adu.spring_test.mybatis.typehandler.EnumTraitTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="enumDataField" column="enum_data_field" javaType="com.xxx.MyEnum" typeHandler="com.adu.spring_test.mybatis.typehandler.EnumTraitTypeHandler"/>
 * </resultMap>
 *
 * 用法二：
 * 1）在mybatis-config.xml中指定handler:
 *      <typeHandlers>
 *              <typeHandler handler="com.adu.spring_test.mybatis.typehandler.EnumTraitTypeHandler" javaType="com.xxx.MyClass"/>
 *      </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 */
public class EnumTraitTypeHandler<E extends BaseEnum> extends BaseTypeHandler<BaseEnum> {
    private Class<E> enumType;
    private Method codeOf;//MyEnum类要有static E codeOf(int)方法

    public EnumTraitTypeHandler(Class<E> enumType) {
        if (enumType == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.enumType = enumType;

        String className = enumType.getName();
        String simpleName = enumType.getSimpleName();

        try {
            this.codeOf = enumType.getDeclaredMethod("codeOf", new Class[]{Integer.TYPE});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code):" + simpleName + " required.");
        }

        if(!Modifier.isStatic(this.codeOf.getModifiers())) {
            throw new RuntimeException("Static method " + className + "#codeOf(int code):" + simpleName + " required.");
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
