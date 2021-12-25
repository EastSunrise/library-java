package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.IntCodeSupplier;
import cn.wsg.commons.web.Displayable;

/**
 * @author Kingen
 */
public enum CollectStatus implements IntCodeSupplier, Displayable {

    WISH(1, "未拥有"), OWNED(2, "已拥有");

    private final int code;

    private final String displayName;

    CollectStatus(int code, String displayName) {
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
