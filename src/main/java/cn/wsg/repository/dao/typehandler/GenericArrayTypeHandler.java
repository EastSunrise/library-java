package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.Language;
import cn.wsg.commons.lang.Region;
import cn.wsg.commons.lang.jackson.EnumDeserializers;
import cn.wsg.commons.lang.jackson.EnumSerializers;
import cn.wsg.repository.common.enums.Genre;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Persists an array parameter as a JSON string.
 *
 * @author Kingen
 */
@MappedJdbcTypes(value = JdbcType.VARCHAR)
@MappedTypes(value = {String[].class, Genre[].class, Region[].class, Language[].class, int[].class})
public class GenericArrayTypeHandler<T> extends BaseTypeHandler<T[]> {

    private final Class<T[]> clazz;

    public GenericArrayTypeHandler(Class<T[]> clazz) {
        this.clazz = Objects.requireNonNull(clazz, "Type argument cannot be null");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType)
        throws SQLException {
        try {
            ps.setString(i, Lazy.MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize the parameter", e);
        }
    }

    @Override
    public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return deserializeNullable(rs.getString(columnName));
    }

    @Override
    public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return deserializeNullable(rs.getString(columnIndex));
    }

    @Override
    public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deserializeNullable(cs.getString(columnIndex));
    }

    private T[] deserializeNullable(String value) throws SQLException {
        if (value == null) {
            return null;
        }
        try {
            return Lazy.MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to deserialize the parameter", e);
        }
    }

    private static class Lazy {

        private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new SimpleModule()
                .addSerializer(Genre.class, EnumSerializers.ofIntCode(Genre.class))
                .addDeserializer(Genre.class, EnumDeserializers.ofIntCode(Genre.class))
                .addSerializer(Region.class, RegionCodeSerializer.INSTANCE)
                .addDeserializer(Region.class, RegionDeserializer.INSTANCE)
                .addSerializer(Language.class, LanguageCodeSerializer.INSTANCE)
                .addDeserializer(Language.class, LanguageDeserializer.INSTANCE)
            );
    }
}
