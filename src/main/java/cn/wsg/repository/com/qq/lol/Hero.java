package cn.wsg.repository.com.qq.lol;

import cn.wsg.commons.jackson.JsonUnknownType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * @author Kingen
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Hero extends BasicHero {

    @JsonProperty("damageType")
    private String damageType;

    @JsonProperty("difficultyL")
    private int difficultyL;

    @JsonProperty("crowdControl")
    private int crowdControl;

    @JsonProperty("style")
    private int style;

    @JsonProperty("mobility")
    private int mobility;

    @JsonProperty("durability")
    private int durability;

    @JsonProperty("damage")
    private int damage;

    @JsonProperty("utility")
    private int utility;

    @JsonProperty("attackdamage")
    private double attackDamage;

    @JsonProperty("attackdamageperlevel")
    private double attackDamagePerLevel;

    @JsonProperty("armor")
    private double armor;

    @JsonProperty("armorperlevel")
    private double armorPerLevel;

    @JsonProperty("spellblock")
    private double spellBlock;

    @JsonProperty("spellblockperlevel")
    private double spellBlockPerLevel;

    @JsonProperty("attackspeed")
    private double attackSpeed;

    @JsonProperty("attackspeedperlevel")
    private double attackSpeedPerLevel;

    @JsonProperty("crit")
    private double crit;

    @JsonProperty("critperlevel")
    private double critPerLevel;

    @JsonProperty("movespeed")
    private double moveSpeed;

    @JsonProperty("hpregen")
    private double hpRegen;

    @JsonProperty("hpregenperlevel")
    private double hpRegenPerLevel;

    @JsonProperty("mpregen")
    private double mpRegen;

    @JsonProperty("mpregenperlevel")
    private double mpRegenPerLevel;

    @JsonProperty("attackrange")
    private double attackRange;

    @JsonProperty("hp")
    private double hp;

    @JsonProperty("hpperlevel")
    private double hpPerLevel;

    @JsonProperty("mp")
    private double mp;

    @JsonProperty("mpperlevel")
    private double mpPerLevel;

    @JsonProperty("heroVideoPath")
    private String heroVideoPath;

    @JsonProperty("allytips")
    private List<String> allyTips;

    @JsonProperty("enemytips")
    private List<String> enemyTips;

    @JsonProperty("palmHeroHeadImg")
    private String palmHeroHeadImg;

    @JsonProperty("introduce")
    private String introduce;

    @JsonProperty("shortBio")
    private String shortBackStory;

    @JsonProperty("relations")
    private List<JsonUnknownType> relations;

    @Setter(AccessLevel.PACKAGE)
    private List<HeroSkin> skins;
}
