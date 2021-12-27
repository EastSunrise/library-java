package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum Gender implements IntCodeSupplier, Displayable {

    FEMALE(0, "女"),
    MALE(1, "男");

    private final int code;
    private final String displayName;

    Gender(int code, String displayName) {
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
