package cn.wsg.repository.com.imdb;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Position of the episode within the TV series.
 *
 * @author Kingen
 */
@Getter
@AllArgsConstructor
public class EpisodeNumber {

    private final int season;
    private final int episode;
}
