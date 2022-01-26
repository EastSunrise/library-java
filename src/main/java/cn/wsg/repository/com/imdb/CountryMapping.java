package cn.wsg.repository.com.imdb;

import cn.wsg.commons.data.common.Country;

import java.util.function.Supplier;

/**
 * @author Kingen
 */
enum CountryMapping implements Supplier<Country> {

    CSHH(Country.CS, "Czechoslovakia"),
    DDDE(Country.DD, "East Germany"),
    SUHH(Country.SU, "USSR"),
    XKO(Country.KR, "South Korea"),
    XKV(Country.RS, "Kosovo"),
    XPI(Country.PS, "Palestine"),
    XWG(Country.XA, "West Germany"),
    XYU(Country.YU, "Yugoslavia"),
    YUCS(Country.YU, "Yugoslavia"),
    ;

    private final Country country;
    private final String name;

    CountryMapping(Country country, String name) {
        this.country = country;
        this.name = name;
    }

    @Override
    public Country get() {
        return country;
    }

    public String getName() {
        return name;
    }
}
