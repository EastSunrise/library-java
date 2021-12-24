package cn.wsg.library.dao.api;

import cn.wsg.commons.internet.BaseSite;
import cn.wsg.commons.internet.support.CssSelectors;
import cn.wsg.commons.internet.support.OtherResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
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
public class ClcIndex extends BaseSite {

    public ClcIndex() {
        super("CLC Index", httpsHost("www.clcindex.com"), client(), defaultContext());
    }

    private static CloseableHttpClient client() {
        return defaultClient();
    }

    /**
     * Retrieves the complete tree of all categories.
     *
     * @return the list of 22 basic categories
     */
    public List<CategoryNode> findAll() throws OtherResponseException {
        return findChildren(httpGet(""));
    }

    private List<CategoryNode> findChildren(RequestBuilder builder) throws OtherResponseException {
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
