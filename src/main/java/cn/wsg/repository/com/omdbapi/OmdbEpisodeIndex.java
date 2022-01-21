package cn.wsg.repository.com.omdbapi;

import cn.wsg.commons.internet.org.schema.item.TVEpisode;

import java.time.LocalDate;

/**
 * @author Kingen
 */
public interface OmdbEpisodeIndex extends TVEpisode {

    /**
     * Returns the id of the episode on IMDb.
     *
     * @return the id of IMDb
     */
    String getImdbId();

    /**
     * Returns the title of the episode.
     *
     * @return the title of the episode
     */
    String getTitle();

    /**
     * Returns the order of the episode in the season.
     *
     * @return the order of the episode
     */
    Integer getEpisode();

    /**
     * Returns the release date of the episode.
     *
     * @return the release date
     */
    LocalDate getRelease();

    /**
     * Returns the rating score of the episode on IMDb.
     *
     * @return the rating score
     */
    Double getImdbRating();
}
