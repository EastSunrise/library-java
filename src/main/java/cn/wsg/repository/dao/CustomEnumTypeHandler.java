package cn.wsg.repository.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Persists an enum constant with Jackson.
 *
 * @author Kingen
 */
public class CustomEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Class<E> type;

    public CustomEnumTypeHandler(Class<E> type) {
        this.type = Objects.requireNonNull(type, "Type argument cannot be null");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize the parameter", e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return deserializeNullable(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return deserializeNullable(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deserializeNullable(cs.getString(columnIndex));
    }

    private E deserializeNullable(String value) throws SQLException {
        if (value == null) {
            return null;
        }
        try {
            return MAPPER.readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to deserialize the parameter", e);
        }
    }
}
