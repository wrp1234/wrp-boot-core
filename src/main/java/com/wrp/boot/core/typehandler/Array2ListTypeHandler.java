package com.wrp.boot.core.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeException;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础类集合typehandler
 * @author wrp
 * @since 2024年12月17日 13:45
 */
@MappedTypes (List.class)
public class Array2ListTypeHandler extends BaseTypeHandler<List<Object>> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter.isEmpty()) {
            ps.setObject(i,null);
            return;
        }

        String typeName = null;
        if (parameter.get(0) instanceof Integer) {
            typeName = JDBCType.INTEGER.getName();
        } else if (parameter.get(0) instanceof String) {
            typeName = JDBCType.VARCHAR.getName();
        } else if (parameter.get(0) instanceof Boolean) {
            typeName = JDBCType.BOOLEAN.getName();
        } else if (parameter.get(0) instanceof Double) {
            typeName = JDBCType.DOUBLE.getName();
        } else if (parameter.get(0) instanceof Float) {
            typeName = JDBCType.FLOAT.getName();
        } else if (parameter.get(0) instanceof Long) {
            typeName = JDBCType.BIGINT.getName();
        } else if (parameter.get(0) instanceof Short) {
            typeName = JDBCType.SMALLINT.getName();
        } else if (parameter.get(0) instanceof Byte) {
            typeName = JDBCType.TINYINT.getName();
        }
        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
        }

        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(typeName, parameter.toArray());
        ps.setArray(i, array);
        array.free();
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getList(rs.getArray(columnName));

    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getList(rs.getArray(columnIndex));
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getList(cs.getArray(columnIndex));
    }

    private List<Object> getList(Array array) {
        // 如果数据库字段为 null，这里会转成一个空的 List，根据业务需求更改，赋值为 null 的话，后面对 PO 处理要注意 NPE
        if (array == null) {
            return Collections.emptyList();
        }
        try {
            // 这里要把 java.sql.Array 转成 java.util.List，尝试了多种写法，只有 Copilot 写的不会出警告
            Object[] objects = (Object[]) array.getArray();
            array.free();
            return Arrays.stream(objects).collect(Collectors.toList());
        } catch (Exception ignored) {
        }
        return null;
    }
}