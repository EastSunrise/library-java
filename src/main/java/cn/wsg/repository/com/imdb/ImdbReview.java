package cn.wsg.repository.com.imdb;

import cn.wsg.commons.internet.common.video.Rating;
import cn.wsg.commons.internet.org.schema.item.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Kingen
 */
@Getter
@JsonTypeName("Review")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImdbReview extends ImdbCreativeWork implements Review {

    @JsonProperty("itemReviewed")
    private ImdbThing itemReviewed;

    @JsonProperty("reviewBody")
    private String reviewBody;

    @JsonProperty("reviewRating")
    private Rating reviewRating;
}
