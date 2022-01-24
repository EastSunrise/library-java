package cn.wsg.repository.tw.x80s;

import cn.wsg.commons.data.download.DownloadLink;
import cn.wsg.repository.resource.BaseResourceItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

/**
 * An item on {@link X80sSite}.
 *
 * @author Kingen
 */
@Getter
@ToString
public class X80sItem extends BaseResourceItem<X80sType> {

    @Setter(AccessLevel.PACKAGE)
    private Integer year;

    @Setter(AccessLevel.PACKAGE)
    private LocalDate update;

    @Setter(AccessLevel.PACKAGE)
    private Long doubanId;

    X80sItem(String title, X80sType subtype, URL url, List<DownloadLink> links) {
        super(title, subtype, url, links);
    }
}
