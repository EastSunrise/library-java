package cn.wsg.repository.config.jackson;

import cn.wsg.commons.data.common.Language;
import cn.wsg.repository.com.douban.LanguageMapping;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

/**
 * @author Kingen
 */
public class LanguageDeserializer extends FromStringDeserializer<Language> {

    protected LanguageDeserializer() {
        super(Language.class);
    }

    @Override
    protected Language _deserialize(String value, DeserializationContext ctxt) {
        return LanguageMapping.of(value);
    }
}
