package cn.wsg.repository.dao.typehandler;

import cn.wsg.commons.lang.Language;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Serializes a {@link Language} enum to a terminology code.
 *
 * @author Kingen
 */
public class LanguageCodeSerializer extends StdSerializer<Language> {

    public static final LanguageCodeSerializer INSTANCE = new LanguageCodeSerializer();

    protected LanguageCodeSerializer() {
        super(Language.class);
    }

    @Override
    public void serialize(Language language, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        gen.writeString(language.getTerminologyCode());
    }
}
