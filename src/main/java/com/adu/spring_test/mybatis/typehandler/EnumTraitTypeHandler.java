package com.adu.spring_test.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.adu.spring_test.mybatis.enums.EnumTrait;
import com.adu.spring_test.mybatis.util.EnumTraitUtil;

/**
 * mapper里字段到枚举类的映射。
 * 用法:
 * 入库：#{item.myEnum, typeHandler=com.adu.spring_test.mybatis.typehandler.EnumTraitTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="enumDataField" column="enum_data_field" javaType="com.xxx.MyEnum" typeHandler="com.adu.spring_test.mybatis.typehandler.EnumTraitTypeHandler"/>
 * </resultMap>
 */
public class EnumTraitTypeHandler<E extends EnumTrait> extends BaseTypeHandler<EnumTrait> {

    private Class<E> type;

    public EnumTraitTypeHandler(Class<E> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EnumTrait parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                return fromCode(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                return fromCode(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            try {
                return fromCode(i);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + i + " to " + type.getSimpleName() + " by ordinal value.", ex);
            }
        }
    }

    private E fromCode(int code) {
        return EnumTraitUtil.codeOf(type, code);
    }
}
