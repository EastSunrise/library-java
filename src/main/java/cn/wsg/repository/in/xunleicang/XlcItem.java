package cn.wsg.repository.in.xunleicang;

import cn.wsg.commons.internet.common.video.resource.BaseResourceItem;
import cn.wsg.commons.internet.common.video.resource.ResourceState;
import cn.wsg.commons.internet.download.DownloadLink;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * An item in {@link XlcSite}.
 *
 * @author Kingen
 */
@Getter
@ToString
public class XlcItem extends BaseResourceItem<XlcType> {

    private final LocalDate updateDate;
    private final ResourceState state;

    @Setter(AccessLevel.PACKAGE)
    private Integer year;

    XlcItem(String title, XlcType subtype, URL url, List<DownloadLink> links, LocalDate updateDate,
        ResourceState state) {
        super(title, subtype, url, links);
        this.updateDate = Objects.requireNonNull(updateDate, "the update date of an item");
        this.state = Objects.requireNonNull(state, "state of the resource");
    }
}
