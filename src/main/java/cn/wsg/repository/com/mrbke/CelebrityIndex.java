package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.data.schema.item.Person;
import cn.wsg.commons.util.AssertUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Kingen
 */
@Getter
@ToString
public class CelebrityIndex implements Person {

    private final int id;

    private final CelebrityType type;

    private final String name;

    CelebrityIndex(int id, CelebrityType type, String name) {
        this.id = id;
        this.type = Objects.requireNonNull(type);
        this.name = AssertUtils.requireNotBlank(name);
    }
}
