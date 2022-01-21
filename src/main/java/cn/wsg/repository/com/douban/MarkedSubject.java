package cn.wsg.repository.com.douban;

import java.time.LocalDate;

/**
 * @author Kingen
 * @see MarkedStatus
 */
public class MarkedSubject extends SubjectIndex {

    private final LocalDate markedDate;

    MarkedSubject(long id, DoubanCatalog catalog, String title, LocalDate markedDate) {
        super(id, catalog, title);
        this.markedDate = markedDate;
    }

    public LocalDate getMarkedDate() {
        return markedDate;
    }
}
