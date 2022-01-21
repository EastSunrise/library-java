package cn.wsg.repository.com.imdb;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Kingen
 * @see ImdbOrganization
 * @see ImdbPerson
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = ImdbOrganization.class, name = "Organization"),
    @JsonSubTypes.Type(value = ImdbPerson.class, name = "Person")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface ImdbCreator {
}
