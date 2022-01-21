package cn.wsg.repository.com.bd2020;

import lombok.Getter;

/**
 * Types of the resources on {@link BdMovieSite}.
 *
 * @author Kingen
 */
@Getter
public enum BdMovieType {
    /**
     * @see <a href="https://www.bd2020.com/zx/index.htm">Latest</a>
     */
    ZX(1328),
    /**
     * @see <a href="https://www.bd2020.com/gq/index.htm">HD</a>
     */
    GQ(375),
    /**
     * @see <a href="https://www.bd2020.com/gy/index.htm">Mandarin</a>
     */
    GY(348),
    /**
     * @see <a href="https://www.bd2020.com/zy/index.htm">Micro Movies</a>
     */
    ZY(1257),
    /**
     * @see <a href="https://www.bd2020.com/jd/index.htm">Classic</a>
     */
    JD(359),
    /**
     * @see <a href="https://www.bd2020.com/dh/index.htm">Animes</a>
     */
    DH(387);

    private final int firstId;

    BdMovieType(int firstId) {
        this.firstId = firstId;
    }
}
