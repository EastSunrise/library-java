package cn.wsg.repository.com.mrbke;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Kingen
 */
@Getter
@ToString
@Setter(AccessLevel.PACKAGE)
public class Celebrity extends SimpleCelebrity {

    private final CelebrityInfo info;

    private List<String> descriptions;

    private List<String> experiences;

    private List<String> groupLives;

    private List<String> awards;

    private Set<String> works;

    Celebrity(int id, CelebrityType type, String name, CelebrityInfo info) {
        super(id, type, name);
        this.info = Objects.requireNonNull(info);
    }
}
