package cn.wsg.repository.tv.rr;

import lombok.Getter;

/**
 * The serialized status of series.
 *
 * @author Kingen
 */
@Getter
public enum SerializedStatus {
    /**
     * to be continued
     */
    AIRING(0, "连载中"),
    /**
     * finished
     */
    CONCLUDED(1, "已完结"),
    /**
     * not released
     */
    PREPARING(2, "未开播");

    private final int code;
    private final String displayName;

    SerializedStatus(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
