package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.data.common.Language;
import cn.wsg.commons.data.common.video.MovieGenre;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Persists a complex object as a JSON string.
 *
 * @author Kingen
 */
@MappedJdbcTypes(value = JdbcType.VARCHAR)
@MappedTypes(value = {String[].class, MovieGenre[].class, Country[].class, Language[].class, int[].class})
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

    private final Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        this.clazz = Objects.requireNonNull(clazz, "Type argument cannot be null");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize the parameter", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return deserializeNullable(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return deserializeNullable(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deserializeNullable(cs.getString(columnIndex));
    }

    private T deserializeNullable(String value) throws SQLException {
        if (value == null) {
            return null;
        }
        try {
            return MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to deserialize the parameter", e);
        }
    }
}
