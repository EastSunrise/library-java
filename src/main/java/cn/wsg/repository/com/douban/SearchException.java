package cn.wsg.repository.com.douban;

import java.io.IOException;

/**
 * Exceptions thrown when searching.
 *
 * @author Kingen
 */
public class SearchException extends IOException {

    private static final long serialVersionUID = 1L;

    public SearchException(String reasonPhrase) {
        super(reasonPhrase);
    }
}
