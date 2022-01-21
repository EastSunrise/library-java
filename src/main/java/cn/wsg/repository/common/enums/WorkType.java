package cn.wsg.repository.common.enums;

import cn.wsg.commons.function.BilingualDisplayable;

/**
 * @author Kingen
 */
public enum WorkType implements BilingualDisplayable {

    AU("Author", "作者"),
    TR("Translator", "译者");

    private final String enName;
    private final String zhName;

    WorkType(String enName, String zhName) {
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
