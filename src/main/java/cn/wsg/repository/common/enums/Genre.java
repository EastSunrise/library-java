package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum Genre implements IntCodeSupplier, Displayable {
    ;

    private final int code;
    private final String displayName;

    Genre(int code, String displayName) {
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
