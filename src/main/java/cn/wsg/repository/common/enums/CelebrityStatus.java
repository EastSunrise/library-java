package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum CelebrityStatus implements IntCodeSupplier, Displayable {

    DIRECTOR(1, "导演"),
    WRITER(2, "编剧"),
    ACTOR(3, "演员");

    private final int code;
    private final String displayName;

    CelebrityStatus(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public int getIntCode() {
        return code;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
