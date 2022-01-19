package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.BilingualDisplayable;

/**
 * @author Kingen
 */
public enum CelebrityStatus implements BilingualDisplayable {

    DI("Director", "导演"),
    WR("Writer", "编剧"),
    AC("Actor", "演员");

    private final String enName;
    private final String zhName;

    CelebrityStatus(String enName, String zhName) {
        this.enName = enName;
        this.zhName = zhName;
    }

    @Override
    public String getZhName() {
        return zhName;
    }

    @Override
    public String getEnName() {
        return enName;
    }
}
