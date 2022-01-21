package cn.wsg.repository.com.mrbke;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.Set;

/**
 * @author Kingen
 */
@Getter
@ToString
public class SimpleCelebrity extends CelebrityIndex {

    @Setter(AccessLevel.PACKAGE)
    private URL image;

    @Setter(AccessLevel.PACKAGE)
    private Set<String> keywords;

    SimpleCelebrity(int id, CelebrityType type, String name) {
        super(id, type, name);
    }
}
