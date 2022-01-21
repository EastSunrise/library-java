package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.util.AssertUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Kingen
 */
@Getter
@ToString
public class AlbumIndex {

    private final int id;

    private final AlbumType type;

    private final String title;

    AlbumIndex(int id, AlbumType type, String title) {
        this.id = id;
        this.type = Objects.requireNonNull(type);
        this.title = AssertUtils.requireNotBlank(title);
    }
}
