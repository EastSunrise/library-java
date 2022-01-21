package cn.wsg.repository.com.douban;

import cn.wsg.commons.Region;
import cn.wsg.commons.internet.util.EnumMapping;
import cn.wsg.commons.util.EnumUtilExt;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

/**
 * @author Kingen
 */
public enum RegionMapping implements EnumMapping<Region> {

    CN(Region.CN, "中国大陆"),
    HK(Region.HK, "中国香港"),
    TW(Region.TW, "中国台湾"),
    RU(Region.RU, "俄罗斯"),
    XA(Region.XA, "西德"),
    ;

    private final Region region;
    private final String[] alias;

    RegionMapping(Region region, String... alias) {
        this.region = region;
        this.alias = alias;
    }

    public static Region of(String value) {
        try {
            return EnumUtilExt.valueOf(Region.class, value, (k, e) -> Objects.equals(k, e.getZhShortName()));
        } catch (IllegalArgumentException ex) {
            return EnumUtilExt.valueOf(RegionMapping.class, value, (k, e) -> e.match(k)).getEnum();
        }
    }

    @Override
    public Region getEnum() {
        return region;
    }

    @Override
    public boolean match(String value) {
        return ArrayUtils.contains(alias, value);
    }
}
