package cn.wsg.repository.config.jackson;

import cn.wsg.commons.data.common.Country;
import cn.wsg.repository.com.douban.CountryMapping;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

/**
 * @author Kingen
 */
public class CountryDeserializer extends FromStringDeserializer<Country> {

    protected CountryDeserializer() {
        super(Country.class);
    }

    @Override
    protected Country _deserialize(String value, DeserializationContext ctxt) {
        return CountryMapping.of(value);
    }
}
