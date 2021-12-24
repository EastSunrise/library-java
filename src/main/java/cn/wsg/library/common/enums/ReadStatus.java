package cn.wsg.library.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum ReadStatus implements IntCodeSupplier, Displayable {

    WISH(0, "未读"), READING(1, "在读"), DONE(2, "已读");

    private final int code;

    private final String displayName;

    ReadStatus(int code, String displayName) {
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
