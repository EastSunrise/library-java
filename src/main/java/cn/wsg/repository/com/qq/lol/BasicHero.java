package cn.wsg.repository.com.qq.lol;

import cn.wsg.commons.jackson.IntStringBooleanDeserializer;
import cn.wsg.commons.jackson.JsonJoinedValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BasicHero {

    @JsonProperty("heroId")
    private int id;

    @JsonProperty("name")
    private String title;

    @JsonProperty("title")
    private String name;

    @JsonProperty("alias")
    private String enName;

    @JsonProperty("attack")
    private int attack;

    @JsonProperty("magic")
    private int magic;

    @JsonProperty("defense")
    private int defense;

    @JsonProperty("difficulty")
    private int difficulty;

    @JsonProperty("goldPrice")
    private int goldPrice;

    @JsonJoinedValue
    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("ispermanentweekfree")
    @JsonDeserialize(using = IntStringBooleanDeserializer.class)
    private boolean permanentWeekFree;

    @JsonProperty("campId")
    private String campId;

    @JsonProperty("camp")
    private String camp;

    @JsonProperty("roles")
    private List<HeroRole> roles;

    @JsonProperty("banAudio")
    private String bannedAudioUrl;

    @JsonProperty("changeLabel")
    private String changeLabel;

    @JsonProperty("selectAudio")
    private String selectedAudioUrl;

    @JsonProperty("couponPrice")
    private int couponPrice;

    @JsonProperty("isWeekFree")
    @JsonDeserialize(using = IntStringBooleanDeserializer.class)
    private boolean weekFree;

    @JsonProperty("isARAMweekfree")
    @JsonDeserialize(using = IntStringBooleanDeserializer.class)
    private boolean aramWeekFree;
}
