package cn.wsg.repository.com.imdb;

import cn.wsg.commons.data.common.Region;

import java.util.function.Supplier;

/**
 * @author Kingen
 */
enum RegionMapping implements Supplier<Region> {

    CSHH(Region.CS, "Czechoslovakia"),
    DDDE(Region.DD, "East Germany"),
    SUHH(Region.SU, "USSR"),
    XKO(Region.KR, "South Korea"),
    XKV(Region.RS, "Kosovo"),
    XPI(Region.PS, "Palestine"),
    XWG(Region.XA, "West Germany"),
    XYU(Region.YU, "Yugoslavia"),
    YUCS(Region.YU, "Yugoslavia"),
    ;

    private final Region region;
    private final String name;

    RegionMapping(Region region, String name) {
        this.region = region;
        this.name = name;
    }

    @Override
    public Region get() {
        return region;
    }

    public String getName() {
        return name;
    }
}
