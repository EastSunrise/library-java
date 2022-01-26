package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.data.common.Country;
import cn.wsg.commons.internet.util.EnumMapping;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kingen
 */
enum CountryMapping implements EnumMapping<Country> {

    US(Country.US, "United States", "USA"),
    KR(Country.KR, "South Korea"),
    ;

    private final Country country;
    private final String[] aka;

    CountryMapping(Country country, String... aka) {
        this.country = country;
        this.aka = aka;
    }

    @Override
    public Country getEnum() {
        return country;
    }

    @Override
    public boolean match(String value) {
        return ArrayUtils.contains(aka, value);
    }
}
