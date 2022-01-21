package cn.wsg.repository.com.imdb;

import cn.wsg.commons.Language;

import java.util.function.Supplier;

/**
 * @author Kingen
 */
enum LanguageMapping implements Supplier<Language> {

    QAB(Language.NAN, "Hokkien"),
    QAD(Language.QHB, "Shanghainese"),
    QAE(Language.LSM, "Saami"),
    QAU(Language.QLA, "Shaanxi"),
    QBN(Language.NLD, "Flemish"),
    SGN(Language.QOA, "Sign Languages"),
    ;

    private final Language language;
    private final String name;

    LanguageMapping(Language language, String name) {
        this.language = language;
        this.name = name;
    }

    @Override
    public Language get() {
        return language;
    }

    public String getName() {
        return name;
    }
}
