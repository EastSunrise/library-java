package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.intangible.Length;
import cn.wsg.commons.intangible.Mass;
import cn.wsg.commons.internet.common.BloodType;
import cn.wsg.commons.internet.common.Constellation;
import cn.wsg.commons.internet.common.Gender;
import cn.wsg.commons.internet.common.Zodiac;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Basic information of a celebrity.
 *
 * @author Kingen
 */
@Getter
@ToString
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CelebrityInfo {

    private Gender gender;

    private String fullName;

    private List<String> zhNames;

    private List<String> jaNames;

    private List<String> enNames;

    private List<String> aka;

    private Zodiac zodiac;

    private Constellation constellation;

    private List<String> interests;

    private Length height;

    private Mass weight;

    private String figure;

    private String cup;

    private BloodType bloodType;

    private String birthday;

    private List<String> occupations;

    private String agency;

    private String startDate;

    private String retireDate;

    private String country;

    private String language;

    private String firm;

    private String school;

    private String birthplace;

    private String ethnicity;

    private Map<String, String> others;
}
