package cn.wsg.repository.tv.rr;

import cn.wsg.commons.data.common.Region;
import cn.wsg.commons.data.common.video.MovieGenre;
import cn.wsg.commons.internet.page.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * The request of pagination information.
 *
 * @author Kingen
 */
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenrenReq extends PageRequest implements Serializable {

    private static final long serialVersionUID = -5684090741879047056L;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private VideoType type;
    private Region area;
    private MovieGenre genre;
    private SerializedStatus status;
    private Sort sort;

    public RenrenReq(int current) {
        super(current, DEFAULT_PAGE_SIZE);
    }

    public RenrenReq(int current, VideoType type, Region area, MovieGenre genre, SerializedStatus status, Sort sort) {
        super(current, DEFAULT_PAGE_SIZE);
        this.type = type;
        this.area = area;
        this.genre = genre;
        this.status = status;
        this.sort = sort;
    }

    public static RenrenReq first() {
        return new RenrenReq(0);
    }

    @JsonProperty("dramaType")
    public String getType() {
        return type == null ? null : type.name();
    }

    @JsonProperty("area")
    public String getArea() {
        return area == null ? null : Mapping.REGION_ALIAS.getOrDefault(area, area.getZhShortName());
    }

    @JsonProperty("plotType")
    public String getGenre() {
        return genre == null ? null : genre.getZhTitle();
    }

    @JsonProperty("serializedStatus")
    public String getStatus() {
        return status == null ? null : String.valueOf(status.getCode());
    }

    @JsonProperty("sort")
    public String getSort() {
        return Objects.requireNonNullElse(sort, Sort.LATEST).name().toLowerCase();
    }

    @JsonProperty("webChannel")
    public String getChannel() {
        return "M_STATION";
    }

    @JsonProperty("page")
    @Override
    public int getCurrent() {
        return super.getCurrent() + 1;
    }

    @Override
    @JsonIgnore
    public int getPageSize() {
        return super.getPageSize();
    }

    @Override
    @JsonIgnore
    public long getOffset() {
        return super.getOffset();
    }

    @Override
    public RenrenReq next() {
        return new RenrenReq(super.next().getCurrent(), type, area, genre, status, sort);
    }

    @Override
    public RenrenReq previous() {
        return new RenrenReq(super.previous().getCurrent(), type, area, genre, status, sort);
    }

    private static class Mapping {
        private static final Map<Region, String> REGION_ALIAS =
            Map.of(Region.CN, "内地", Region.HK, "中国香港", Region.TW, "中国台湾");
    }

    public enum Sort {
        /**
         * by time
         */
        LATEST,
        /**
         * by clout
         */
        HOT,
        /**
         * by score
         */
        SCORE
    }
}
