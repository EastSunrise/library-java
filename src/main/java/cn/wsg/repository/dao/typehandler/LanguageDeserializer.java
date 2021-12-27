package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.EnumUtilExt;
import cn.wsg.commons.lang.Language;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Deserializes a terminology code to a {@link Language} enum.
 *
 * @author Kingen
 */
public class LanguageDeserializer extends StdDeserializer<Language> {

    public static final LanguageDeserializer INSTANCE = new LanguageDeserializer();

    protected LanguageDeserializer() {
        super(Language.class);
    }

    @Override
    public Language deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            return null;
        }
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return EnumUtilExt
                .valueOfKey(Language.class, p.getValueAsString(), Language::getTerminologyCode);
        }
        return (Language) ctxt.handleUnexpectedToken(String.class, p.currentToken(), p,
            "Unexpected token as a key to be deserialized to a language.");
    }
}
