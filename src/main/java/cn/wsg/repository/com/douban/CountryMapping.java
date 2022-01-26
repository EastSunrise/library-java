package cn.wsg.repository.com.douban;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.internet.util.EnumMapping;
import cn.wsg.commons.util.EnumUtilExt;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

/**
 * @author Kingen
 */
public enum CountryMapping implements EnumMapping<Country> {

    CN(Country.CN, "中国大陆"),
    HK(Country.HK, "中国香港"),
    TW(Country.TW, "中国台湾"),
    RU(Country.RU, "俄罗斯"),
    XA(Country.XA, "西德"),
    ;

    private final Country country;
    private final String[] alias;

    CountryMapping(Country country, String... alias) {
        this.country = country;
        this.alias = alias;
    }

    public static Country of(String value) {
        try {
            return EnumUtilExt.valueOf(Country.class, value, (k, e) -> Objects.equals(k, e.getZhShortName()));
        } catch (IllegalArgumentException ex) {
            return EnumUtilExt.valueOf(CountryMapping.class, value, (k, e) -> e.match(k)).getEnum();
        }
    }

    @Override
    public Country getEnum() {
        return country;
    }

    @Override
    public boolean match(String value) {
        return ArrayUtils.contains(alias, value);
    }
}
