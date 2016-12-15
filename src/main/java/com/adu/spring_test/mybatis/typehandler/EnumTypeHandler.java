package com.adu.spring_test.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.adu.spring_test.mybatis.util.CodeEnumUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.adu.spring_test.mybatis.enums.CodeBaseEnum;

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
public class EnumTypeHandler<E extends Enum<?> & CodeBaseEnum> extends BaseTypeHandler<CodeBaseEnum> {
    private Class<E> clazz;

    public EnumTypeHandler(Class<E> enumType) {
        if (enumType == null)
            throw new IllegalArgumentException("Type argument cannot be null");

        this.clazz = enumType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CodeBaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.code());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, rs.getInt(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, rs.getInt(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CodeEnumUtil.codeOf(clazz, cs.getInt(columnIndex));
    }
}
