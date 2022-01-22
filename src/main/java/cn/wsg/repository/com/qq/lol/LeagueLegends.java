package cn.wsg.repository.com.qq.lol;

import cn.wsg.commons.DatetimeConsts;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.support.NotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kingen
 * @see <a href="https://lol.qq.com/main.shtml">League of Legends</a>
 */
public class LeagueLegends extends BaseSiteClient {

    public LeagueLegends() {
        super("League of Legends", HttpHost.create("https://lol.qq.com"));
    }

    /**
     * Retrieves the list of all heroes with basic information.
     */
    public List<BasicHero> findHeroes() throws NotFoundException {
        String heroes = "https://game.gtimg.cn/images/lol/act/img/js/heroList/hero_list.js";
        return getObject(RequestBuilder.get(heroes), Lazy.MAPPER, HeroList.class).heroes;
    }

    /**
     * Retrieves the details of a hero and its skins by the specified id.
     *
     * @see BasicHero#getId()
     */
    public Hero findHeroById(int id) throws NotFoundException {
        String hero = String.format("https://game.gtimg.cn/images/lol/act/img/js/hero/%d.js", id);
        HeroInfo info = getObject(RequestBuilder.get(hero), Lazy.MAPPER, HeroInfo.class);
        info.hero.setSkins(info.skins);
        return info.hero;
    }

    @Getter
    private static class HeroList {

        @JsonProperty("hero")
        private List<BasicHero> heroes;

        @JsonProperty("fileName")
        private String fileName;

        @JsonProperty("fileTime")
        @JsonFormat(pattern = DatetimeConsts.PAT_YYYY_MM_DD_HH_MM_SS)
        private LocalDateTime fileTime;

        @JsonProperty("version")
        private String version;
    }

    @Getter
    @JsonIgnoreProperties("spells")
    private static class HeroInfo {

        @JsonProperty("hero")
        private Hero hero;

        @JsonProperty("skins")
        private List<HeroSkin> skins;

        @JsonProperty("fileName")
        private String fileName;

        @JsonProperty("fileTime")
        @JsonFormat(pattern = DatetimeConsts.PAT_YYYY_MM_DD_HH_MM_SS)
        private LocalDateTime fileTime;

        @JsonProperty("version")
        private String version;
    }

    private static class Lazy {

        private static final ObjectMapper MAPPER =
            new ObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).registerModule(new JavaTimeModule());
    }
}
