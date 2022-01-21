package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.internet.view.NextSupplier;
import cn.wsg.commons.util.AssertUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An album including a series of pictures.
 *
 * @author Kingen
 */
@Getter
@ToString
public class Album extends AlbumIndex implements NextSupplier<AlbumIndex> {

    private final List<URL> images;

    private final LocalDateTime updateTime;

    @Setter(AccessLevel.PACKAGE)
    private Set<String> keywords;

    @Setter(AccessLevel.PACKAGE)
    private List<CelebrityIndex> relatedCelebrities;

    @Setter(AccessLevel.PACKAGE)
    private AlbumIndex nextId;

    Album(int id, AlbumType type, String title, List<URL> images, LocalDateTime updateTime) {
        super(id, type, title);
        this.images = AssertUtils.requireNotEmpty(images, "images of an album");
        this.updateTime = Objects.requireNonNull(updateTime);
    }
}
