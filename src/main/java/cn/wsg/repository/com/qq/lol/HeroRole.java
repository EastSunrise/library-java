package cn.wsg.repository.com.qq.lol;

import lombok.Getter;

/**
 * @author Kingen
 */
public enum HeroRole {

    MAGE("法师"),
    FIGHTER("战士"),
    TANK("坦克"),
    ASSASSIN("刺客"),
    SUPPORT("辅助"),
    MARKSMAN("射手");

    @Getter
    private final String displayName;

    HeroRole(String displayName) {
        this.displayName = displayName;
    }
}
