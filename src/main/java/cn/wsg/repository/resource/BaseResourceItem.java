package cn.wsg.repository.resource;

import cn.wsg.commons.data.download.DownloadLink;
import cn.wsg.commons.util.AssertUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * @author Kingen
 */
@Getter
@ToString
public abstract class BaseResourceItem<E extends Enum<E>> implements ResourceItem<E> {

    private final String title;

    private final E subtype;

    private final URL url;

    private final List<DownloadLink> links;

    @Setter
    private URL image;

    @Setter
    private String description;

    protected BaseResourceItem(String title, E subtype, URL url, List<DownloadLink> links) {
        this.title = AssertUtils.requireNotBlank(title);
        this.subtype = Objects.requireNonNull(subtype);
        this.url = Objects.requireNonNull(url);
        this.links = Objects.requireNonNull(links);
    }
}
