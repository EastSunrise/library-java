package cn.wsg.repository.com.clcindex;

import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.support.CssSelectors;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves the details of Chinese Library Classification.
 *
 * @author Kingen
 * @see <a href="https://www.clcindex.com/category/">Chinese Library Classification</a>
 */
public class ClcIndex extends BaseSiteClient {

    public ClcIndex() {
        super("CLC Index", HttpHost.create("https://www.clcindex.com"));
    }

    /**
     * Retrieves the complete tree of all categories.
     *
     * @return the list of 22 basic categories
     */
    public List<CategoryNode> findAll() {
        return findChildren(httpGet(""));
    }

    private List<CategoryNode> findChildren(RequestBuilder builder) {
        Element table = findDocument(builder).getElementById("catTable");
        Elements items = table.select("tr[name=item-row]");
        if (items.isEmpty()) {
            return null;
        }
        List<CategoryNode> nodes = new ArrayList<>(items.size());
        for (Element tr : items) {
            Elements tds = tr.select(CssSelectors.TAG_TD);
            String idx = tds.get(1).text();
            String title = tds.get(2).selectFirst(CssSelectors.TAG_A).text();
            String nextIdx = URLEncoder.encode(idx, StandardCharsets.UTF_8);
            String path = String.format("/category/%s/", nextIdx);
            List<CategoryNode> children = findChildren(httpGet(path));
            nodes.add(new CategoryNode(idx, title, children));
        }
        return nodes;
    }
}
