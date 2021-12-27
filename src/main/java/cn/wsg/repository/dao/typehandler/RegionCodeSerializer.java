package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.Region;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Serializes a {@link Region} enum to an alpha-2 code.
 *
 * @author Kingen
 */
public class RegionCodeSerializer extends StdSerializer<Region> {

    public static final RegionCodeSerializer INSTANCE = new RegionCodeSerializer();

    protected RegionCodeSerializer() {
        super(Region.class);
    }

    @Override
    public void serialize(Region region, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        gen.writeString(region.getAlpha2());
    }
}
