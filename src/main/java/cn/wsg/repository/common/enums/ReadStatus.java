package cn.wsg.repository.common.enums;

import cn.wsg.commons.function.BilingualDisplayable;

/**
 * @author Kingen
 */
public enum ReadStatus implements BilingualDisplayable {

    WI("Wish", "想读"),
    RE("Reading", "在读"),
    DO("DONE", "已读");

    private final String enName;
    private final String zhName;

    ReadStatus(String enName, String zhName) {
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
