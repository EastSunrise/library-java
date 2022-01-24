package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.common.Region;
import cn.wsg.commons.internet.util.EnumMapping;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kingen
 */
enum RegionMapping implements EnumMapping<Region> {

    US(Region.US, "United States", "USA"),
    KR(Region.KR, "South Korea"),
    ;

    private final Region region;
    private final String[] aka;

    RegionMapping(Region region, String... aka) {
        this.region = region;
        this.aka = aka;
    }

    @Override
    public Region getEnum() {
        return region;
    }

    @Override
    public boolean match(String value) {
        return ArrayUtils.contains(aka, value);
    }
}
