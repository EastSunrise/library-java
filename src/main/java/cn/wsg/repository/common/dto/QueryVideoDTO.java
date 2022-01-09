package cn.wsg.repository.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Kingen
 */
@Getter
@Setter
@ToString
public class QueryVideoDTO {

    private Long doubanId;

    private String imdbId;
}
