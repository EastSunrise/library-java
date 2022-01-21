package cn.wsg.repository.com.douban;

import cn.wsg.commons.Language;
import cn.wsg.commons.internet.util.EnumMapping;
import cn.wsg.commons.util.EnumUtilExt;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

/**
 * @author Kingen
 */
public enum LanguageMapping implements EnumMapping<Language> {

    CMN(Language.CMN, "汉语普通话", "普通话", "国语"),
    QLE(Language.QLE, "山西话"),
    YUE(Language.YUE, "粤语"),
    QGD(Language.QGD, "重庆话"),
    QGG(Language.QGG, "云南方言"),
    QLF(Language.QLF, "湖南话"),
    HAK(Language.HAK, "客家话"),
    QDB(Language.QDB, "唐山话"),
    CSP(Language.CSP, "南京话"),
    CNP(Language.CNP, "北京话"),
    KOR(Language.KOR, "韩语", "釜山方言"),
    YID(Language.YID, "意第绪语"),
    ELL(Language.ELL, "希腊语"),
    IND(Language.IND, "印度尼西亚语"),
    COR(Language.COR, "科尼什语"),
    TGL(Language.TGL, "菲律宾语"),
    GLA(Language.GLA, "苏格兰盖尔语"),
    ANG(Language.ANG, "古英语", "古代英语"),
    GRC(Language.GRC, "古希腊语"),
    AFR(Language.AFR, "南非语"),
    EUS(Language.EUS, "Basque"),
    XHC(Language.XHC, "匈奴语"),
    ASE(Language.ASE, "美国手语"),
    KVK(Language.KVK, "韩国手语"),
    QGB(Language.QGB, "武汉话"),
    QHF(Language.QHF, "温州话"),
    NAN(Language.NAN, "闽南语", "台语"),
    QEJ(Language.QEJ, "陕西话", "关中话", "西安话"),
    HIN(Language.HIN, "北印度语"),
    LBJ(Language.LBJ, "拉达克语"),
    CAT(Language.CAT, "加泰罗尼亚语"),
    MAS(Language.MAS, "马赛语"),
    QUE(Language.QUE, "克丘亚语"),
    NAP(Language.NAP, "那不勒斯语"),
    GSW(Language.GSW, "瑞士德语"),
    NLD(Language.NLD, "弗拉芒语"),
    QHB(Language.QHB, "上海话", "沪语"),
    QLD(Language.QLD, "四川话"),
    SJN(Language.SJN, "辛达林语"),
    QYA(Language.QYA, "昆雅语"),
    QHN(Language.QHN, "福建话"),
    QGC(Language.QGC, "贵州独山话"),
    PAW(Language.PAW, "波尼语"),
    ;

    private final Language language;
    private final String[] names;

    LanguageMapping(Language language, String... names) {
        this.language = language;
        this.names = names;
    }

    public static Language of(String value) {
        try {
            return EnumUtilExt.valueOf(Language.class, value, (k, e) -> Objects.equals(k, e.getZhName()));
        } catch (IllegalArgumentException ex) {
            return EnumUtilExt.valueOf(LanguageMapping.class, value, (k, e) -> e.match(k)).getEnum();
        }
    }

    @Override
    public Language getEnum() {
        return language;
    }

    @Override
    public boolean match(String value) {
        return ArrayUtils.contains(names, value);
    }
}
