package cn.wsg.repository.config.jackson;

import cn.wsg.commons.internet.com.douban.RegionMapping;
import cn.wsg.commons.lang.Region;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

/**
 * @author Kingen
 */
public class RegionDeserializer extends FromStringDeserializer<Region> {

    protected RegionDeserializer() {
        super(Region.class);
    }

    @Override
    protected Region _deserialize(String value, DeserializationContext ctxt) {
        return RegionMapping.of(value);
    }
}
