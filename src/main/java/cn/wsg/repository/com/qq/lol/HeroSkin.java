package cn.wsg.repository.com.qq.lol;

import cn.wsg.commons.jackson.IntStringBooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class HeroSkin {

    @JsonProperty("skinId")
    private int id;

    @JsonProperty("heroId")
    private int heroId;

    @JsonProperty("heroName")
    private String heroTitle;

    @JsonProperty("heroTitle")
    private String heroName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("emblemsName")
    private String emblemsName;

    @JsonProperty("chromas")
    @JsonDeserialize(using = IntStringBooleanDeserializer.class)
    private boolean chromas;

    @JsonProperty("isBase")
    @JsonDeserialize(using = IntStringBooleanDeserializer.class)
    private boolean base;

    @JsonProperty("chromasBelongId")
    private int chromasBelongId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("publishTime")
    private LocalDate publishTime;

    @JsonProperty("mainImg")
    private String mainImg;

    @JsonProperty("sourceImg")
    private String sourceImg;

    @JsonProperty("chromaImg")
    private String chromaImg;

    @JsonProperty("iconImg")
    private String iconImg;

    @JsonProperty("loadingImg")
    private String loadingImg;

    @JsonProperty("videoImg")
    private String videoImg;

    @JsonProperty("suitType")
    private String suitType;

    @JsonProperty("vedioPath")
    private String videoPath;
}
