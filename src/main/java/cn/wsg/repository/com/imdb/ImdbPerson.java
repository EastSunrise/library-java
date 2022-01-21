package cn.wsg.repository.com.imdb;

import cn.wsg.commons.internet.org.schema.item.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Kingen
 */
@Getter
@JsonTypeName("Person")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImdbPerson extends ImdbThing implements Person, ImdbCreator {

    @JsonProperty("jobTitle")
    private List<String> jobsTitles;

    @JsonProperty("birthDate")
    private LocalDate birthDate;
}
