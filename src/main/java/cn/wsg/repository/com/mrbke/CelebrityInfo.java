package cn.wsg.repository.com.mrbke;

import cn.wsg.commons.data.common.BloodType;
import cn.wsg.commons.data.common.Constellation;
import cn.wsg.commons.data.common.Gender;
import cn.wsg.commons.data.common.Zodiac;
import cn.wsg.commons.data.intangible.Length;
import cn.wsg.commons.data.intangible.Mass;
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
