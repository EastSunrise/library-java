package cn.wsg.repository.com.bd2020;

import cn.wsg.commons.data.download.DownloadLink;
import cn.wsg.commons.internet.view.NextSupplier;
import cn.wsg.repository.resource.BaseResourceItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Items of {@link BdMovieSite}.
 *
 * @author Kingen
 */
@Getter
@ToString
public class BdMovieItem extends BaseResourceItem<BdMovieType> implements NextSupplier<Integer> {

    private final LocalDateTime updateTime;

    @Setter(AccessLevel.PACKAGE)
    private Long dbId;

    @Setter(AccessLevel.PACKAGE)
    private String imdbId;

    @Setter(AccessLevel.PACKAGE)
    private Integer next;

    BdMovieItem(String title, BdMovieType subtype, URL url, List<DownloadLink> links, LocalDateTime updateTime) {
        super(title, subtype, url, links);
        this.updateTime = Objects.requireNonNull(updateTime, "the update time of an item");
    }

    @Override
    public Integer getNextId() {
        return next;
    }
}
