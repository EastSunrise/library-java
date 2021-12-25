package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum WorkType implements IntCodeSupplier, Displayable {
    /**
     * As the author or the translator
     */
    AUTHOR(1, "作者"), TRANSLATOR(2, "译者");

    private final int code;

    private final String displayName;

    WorkType(int code, String displayName) {
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
