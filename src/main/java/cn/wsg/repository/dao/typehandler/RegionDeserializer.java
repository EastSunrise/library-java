package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.EnumUtilExt;
import cn.wsg.commons.lang.Region;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Deserializes an alpha-2 code to a {@link Region} enum.
 *
 * @author Kingen
 */
public class RegionDeserializer extends StdDeserializer<Region> {

    public static final RegionDeserializer INSTANCE = new RegionDeserializer();

    protected RegionDeserializer() {
        super(Region.class);
    }

    @Override
    public Region deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            return null;
        }
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return EnumUtilExt.valueOfKey(Region.class, p.getValueAsString(), Region::getAlpha2);
        }
        return (Region) ctxt.handleUnexpectedToken(String.class, p.currentToken(), p,
            "Unexpected token as a key to be deserialized to a region.");
    }
}
