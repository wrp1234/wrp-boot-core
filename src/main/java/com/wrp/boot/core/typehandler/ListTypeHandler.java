package com.wrp.boot.core.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrp.boot.core.context.BeanFactoryContext;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wrp
 * @since  2024年07月10日 14:16
 */
@MappedJdbcTypes(value = {JdbcType.OTHER}, includeNullJdbcType = true)
@MappedTypes({List.class})
public abstract class ListTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private static final PGobject jsonObject = new PGobject();

    /**
     * 设置非空参数
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("json");
        try {
            jsonObject.setValue(BeanFactoryContext.getBean(ObjectMapper.class).writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ps.setObject(i, jsonObject);
    }

    /**
     * 根据列名，获取可以为空的结果
     */
    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        return getResult(sqlJson);
    }

    /**
     * 根据列索引，获取可以为空的结果
     */
    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        return getResult(sqlJson);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        return getResult(sqlJson);
    }

    private List<T> getResult(String sqlJson) {
        if (StringUtils.hasText(sqlJson)) {
            try {
                return BeanFactoryContext.getBean(ObjectMapper.class).readValue(sqlJson, specificType());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 具体类型，由子类提供
     *
     * @return 具体类型
     */
    protected abstract TypeReference<List<T>> specificType();


}