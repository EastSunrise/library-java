package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.data.schema.item.CreativeWork;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * An adult work of a celebrity.
 *
 * @author Kingen
 */
@Getter
@ToString
@Setter(AccessLevel.PACKAGE)
public class AdultWork implements CreativeWork {

    private final SimpleCelebrity celebrity;

    private final String serialNum;

    private final String title;

    private final URL image;

    private final List<String> actresses;

    private Boolean mosaic;

    private Duration duration;

    private LocalDate datePublished;

    private String director;

    private String producer;

    private String publisher;

    private String series;

    private List<String> tags;

    AdultWork(SimpleCelebrity celebrity, String serialNum, String title, URL image, List<String> actresses) {
        this.celebrity = celebrity;
        this.serialNum = serialNum;
        this.title = title;
        this.image = image;
        this.actresses = actresses;
    }
}
