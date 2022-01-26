package cn.wsg.repository.common.dto;

import cn.wsg.commons.data.common.Country;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class AuthorDTO {

    private Long id;

    private String name;

    private String originalName;

    private Country country;

    private String link;
}
