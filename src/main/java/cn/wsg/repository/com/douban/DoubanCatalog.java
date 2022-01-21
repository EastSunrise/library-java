package cn.wsg.repository.com.douban;

import cn.wsg.commons.function.IntCodeSupplier;

/**
 * @author Kingen
 */
public enum DoubanCatalog implements IntCodeSupplier {

    /**
     * @see <a href="https://book.douban.com/">Books</a>
     */
    BOOK(1001, "读书", "author", "authors"),
    /**
     * @see <a href="https://movie.douban.com/">Movies</a>
     */
    MOVIE(1002, "电影", "celebrity", "celebrities"),
    /**
     * @see <a href="https://music.douban.com/">Music</a>
     */
    MUSIC(1003, "音乐", "musician", "musicians");

    private final int code;
    private final String topic;
    private final String person;
    private final String personPlurality;

    DoubanCatalog(int code, String topic, String person, String personPlurality) {
        this.code = code;
        this.topic = topic;
        this.person = person;
        this.personPlurality = personPlurality;
    }

    @Override
    public int getIntCode() {
        return code;
    }

    public String getTopic() {
        return topic;
    }

    public String getPerson() {
        return person;
    }

    public String getPersonPlurality() {
        return personPlurality;
    }
}
