package cn.wsg.repository.com.douban;

/**
 * @author Kingen
 */
public class RankedSubject extends SubjectIndex {

    private final int rank;

    RankedSubject(long id, DoubanCatalog catalog, String name, int rank) {
        super(id, catalog, name);
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
