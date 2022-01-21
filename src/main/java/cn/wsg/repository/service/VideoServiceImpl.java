package cn.wsg.repository.service;

import cn.wsg.commons.Language;
import cn.wsg.commons.Region;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.internet.support.LoginException;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.UnexpectedException;
import cn.wsg.commons.util.CollectionUtilsExt;
import cn.wsg.repository.com.douban.DoubanMovie;
import cn.wsg.repository.com.douban.DoubanRepository;
import cn.wsg.repository.com.douban.DoubanTVSeries;
import cn.wsg.repository.com.douban.DoubanVideo;
import cn.wsg.repository.com.imdb.*;
import cn.wsg.repository.common.dto.QueryVideoDTO;
import cn.wsg.repository.common.error.DataIntegrityException;
import cn.wsg.repository.dao.mapper.video.MovieMapper;
import cn.wsg.repository.dao.mapper.video.SeasonMapper;
import cn.wsg.repository.dao.mapper.video.SeriesMapper;
import cn.wsg.repository.entity.video.MovieDO;
import cn.wsg.repository.entity.video.SeasonDO;
import cn.wsg.repository.entity.video.SeriesDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Kingen
 */
@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    private final DoubanRepository doubanRepo;
    private final ImdbRepository imdbRepo;
    private final MovieMapper movieMapper;
    private final SeriesMapper seriesMapper;
    private final SeasonMapper seasonMapper;

    @Autowired
    public VideoServiceImpl(DoubanRepository doubanRepo, ImdbRepository imdbRepo, MovieMapper movieMapper,
        SeriesMapper seriesMapper, SeasonMapper seasonMapper) {
        this.doubanRepo = doubanRepo;
        this.imdbRepo = imdbRepo;
        this.movieMapper = movieMapper;
        this.seriesMapper = seriesMapper;
        this.seasonMapper = seasonMapper;
    }

    @Override
    public int countVideoBy(QueryVideoDTO cond) {
        return movieMapper.countBy(cond) + seriesMapper.countBy(cond) + seasonMapper.countBy(cond);
    }

    @Override
    public long importVideoFromDouban(long doubanId, DoubanVideo video) throws DataIntegrityException {
        Optional<Long> optional = checkDouban(doubanId);
        if (optional.isPresent()) {
            return optional.get();
        }

        log.info("Importing a subject from Douban Plugin: {}", doubanId);
        return importVideo(doubanId, video);
    }

    @Override
    public long importVideoFromDouban(long doubanId) throws DataIntegrityException, NotFoundException {
        Optional<Long> optional = checkDouban(doubanId);
        if (optional.isPresent()) {
            return optional.get();
        }
        log.info("Importing a subject from Douban: {}", doubanId);
        DoubanVideo video = doubanRepo.findVideoById(doubanId);
        return importVideo(doubanId, video);
    }

    @Override
    public long importVideoFromImdb(String imdbId) throws DataIntegrityException, NotFoundException {
        MovieDO movie = movieMapper.selectByImdbId(imdbId);
        if (movie != null) {
            return movie.getId();
        }
        SeriesDO exists = seriesMapper.selectByImdbId(imdbId);
        if (exists != null) {
            return exists.getId();
        }

        log.info("Importing a title from IMDb: {}", imdbId);
        ImdbCreativeWork imdbTitle = imdbRepo.findTitleById(imdbId);
        if (imdbTitle instanceof ImdbMovie) {
            try {
                long dbId = doubanRepo.getDbIdByImdbId(imdbId);
                DoubanMovie doubanMovie = (DoubanMovie)doubanRepo.findVideoById(dbId);
                return insertMovie(imdbId, (ImdbMovie)imdbTitle, dbId, doubanMovie);
            } catch (NotFoundException | LoginException e) {
                throw new DataIntegrityException("Can't import the movie with Douban id.");
            }
        }

        if (imdbTitle instanceof ImdbTVSeries) {
            return insertSeries(imdbId, imdbTitle.getName());
        }

        throw new DataIntegrityException("Unexpected type to be imported: " + imdbTitle.getClass().getName());
    }

    private Optional<Long> checkDouban(long doubanId) {
        MovieDO movie = movieMapper.selectByDoubanId(doubanId);
        if (movie != null) {
            return Optional.of(movie.getId());
        }
        SeasonDO season = seasonMapper.selectByDoubanId(doubanId);
        return Optional.ofNullable(season).map(SeasonDO::getId);
    }

    private long importVideo(long doubanId, DoubanVideo video) throws DataIntegrityException {
        String imdbId = video.getImdbId();
        if (imdbId == null) {
            throw new DataIntegrityException("Can't import the subject without IMDb id.");
        }
        ImdbCreativeWork imdbTitle;
        try {
            imdbTitle = imdbRepo.findTitleById(imdbId);
        } catch (NotFoundException e) {
            throw new UnexpectedException(e);
        }

        if (video instanceof DoubanMovie) {
            if (!(imdbTitle instanceof ImdbMovie)) {
                throw new DataIntegrityException("Can't import the movie with conflict types.");
            }
            return insertMovie(imdbId, (ImdbMovie)imdbTitle, doubanId, (DoubanMovie)video);
        }

        return insertSeason(doubanId, (DoubanTVSeries)video, imdbId, imdbTitle);
    }

    private long insertSeason(long doubanId, DoubanTVSeries doubanSeries, String imdbId, ImdbCreativeWork imdbTitle)
        throws DataIntegrityException {
        SeasonDO season = new SeasonDO();
        String seriesImdbId;
        if (imdbTitle instanceof ImdbTVSeries) {
            SeriesDO series = seriesMapper.selectByImdbId(imdbId);
            season.setSeriesId(series == null ? insertSeries(imdbId, imdbTitle.getName()) : series.getId());
            season.setCurrentSeason(1);
        } else if (imdbTitle instanceof ImdbEpisode) {
            seriesImdbId = ((ImdbEpisode)imdbTitle).getSeriesTitleId();
            SeriesDO series = seriesMapper.selectByImdbId(seriesImdbId);
            if (series == null) {
                try {
                    ImdbTVSeries imdbSeries = (ImdbTVSeries)imdbRepo.findTitleById(seriesImdbId);
                    season.setSeriesId(insertSeries(seriesImdbId, imdbSeries.getName()));
                } catch (NotFoundException e) {
                    throw new UnexpectedException(e);
                }
            } else {
                season.setSeriesId(series.getId());
            }
            season.setCurrentSeason(((ImdbEpisode)imdbTitle).getEpisodeNumber().getSeason());
        } else {
            throw new DataIntegrityException(String
                .format("Conflict type: ImdbTVSeries or ImdbEpisode expected but %s provided.", imdbTitle.getClass()));
        }

        season.setEpisodesCount(
            checkDataIntegrity(doubanSeries.getNumberOfEpisodes(), "number of episodes of the season"));
        season.setDoubanId(doubanId);
        season.setZhTitle(doubanSeries.getZhTitle());
        season.setOriginalTitle(doubanSeries.getOriginalTitle());
        season.setReleaseDate(doubanSeries.getDatePublished());
        season.setDescription(doubanSeries.getDescription());

        if (doubanSeries.getAlternateNames() != null) {
            season.setOtherTitles(doubanSeries.getAlternateNames().toArray(new String[0]));
        }
        if (doubanSeries.getGenres() != null) {
            season.setGenres(doubanSeries.getGenres().toArray(new MovieGenre[0]));
        }
        if (doubanSeries.getCountriesOfOrigin() != null) {
            season.setRegions(doubanSeries.getCountriesOfOrigin().toArray(new Region[0]));
        }
        if (doubanSeries.getLanguages() != null) {
            season.setLanguages(doubanSeries.getLanguages().toArray(new Language[0]));
        }

        seasonMapper.insert(season);
        return season.getId();
    }

    /**
     * All parameters are required to be not null.
     */
    private long insertMovie(String imdbId, ImdbMovie imdbMovie, long dbId, DoubanMovie doubanMovie)
        throws DataIntegrityException {
        MovieDO movie = new MovieDO();
        movie.setImdbId(imdbId);
        movie.setDoubanId(dbId);
        movie.setZhTitle(doubanMovie.getZhTitle());
        movie.setOriginalTitle(doubanMovie.getOriginalTitle());
        movie.setEnTitle(imdbMovie.getName());
        movie.setGenres(imdbMovie.getGenres().toArray(new MovieGenre[0]));
        movie.setRegions(imdbMovie.getCountriesOfOrigin().toArray(new Region[0]));
        movie.setLanguages(checkDataIntegrity(imdbMovie.getLanguages(), "languages").toArray(new Language[0]));
        movie.setReleaseDate(Optional.ofNullable(imdbMovie.getDatePublished()).orElse(doubanMovie.getDatePublished()));
        movie.setDescription(doubanMovie.getDescription());

        if (doubanMovie.getAlternateNames() != null) {
            movie.setOtherTitles(doubanMovie.getAlternateNames().toArray(new String[0]));
        }

        Set<Integer> runtimes = new HashSet<>();
        runtimes.add((int)checkDataIntegrity(imdbMovie.getDuration(), "duration").toMinutes());
        runtimes.add((int)doubanMovie.getDuration().toMinutes());
        movie.setRuntimes(CollectionUtilsExt.toIntArray(runtimes));

        movieMapper.insert(movie);
        return movie.getId();
    }

    private long insertSeries(String imdbId, String enTitle) {
        SeriesDO series = new SeriesDO();
        series.setImdbId(imdbId);
        series.setEnTitle(enTitle);
        seriesMapper.insert(series);
        return series.getId();
    }

    private <T> T checkDataIntegrity(T value, String message) throws DataIntegrityException {
        if (value == null) {
            throw new DataIntegrityException("Lacking of " + message);
        }
        return value;
    }
}
