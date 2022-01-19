package cn.wsg.repository.common.enums;

import cn.wsg.commons.lang.function.BilingualDisplayable;

/**
 * @author Kingen
 */
public enum CollectStatus implements BilingualDisplayable {

    WI("Wish", "未拥有"),
    OW("Owned", "已拥有");

    private final String enName;
    private final String zhName;

    CollectStatus(String enName, String zhName) {
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
