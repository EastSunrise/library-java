package cn.wsg.repository.common.enums;

import cn.wsg.commons.web.BilingualDisplayable;

/**
 * @author Kingen
 */
public enum Gender implements BilingualDisplayable {

    FE("Female", "女"),
    MA("Male", "男");

    private final String enName;
    private final String zhName;

    Gender(String enName, String zhName) {
        this.enName = enName;
        this.zhName = zhName;
    }

    @Override
    public String getZhDisplayName() {
        return zhName;
    }

    @Override
    public String getEnDisplayName() {
        return enName;
    }
}
