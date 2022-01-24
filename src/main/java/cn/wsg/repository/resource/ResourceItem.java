package cn.wsg.repository.resource;

import cn.wsg.commons.data.download.DownloadLink;
import cn.wsg.commons.data.schema.common.SchemaProperty;
import cn.wsg.commons.data.schema.item.CreativeWork;

import java.net.URL;
import java.util.List;

/**
 * An item that includes download links to the resources of this item.
 *
 * @author Kingen
 */
public interface ResourceItem<E extends Enum<E>> extends CreativeWork {

    /**
     * Returns the title of the item.
     *
     * @return the title
     */
    @SchemaProperty("name")
    String getTitle();

    /**
     * Returns URL of the item.
     *
     * @return the URL
     */
    @SchemaProperty("url")
    URL getUrl();

    /**
     * Returns the subtype of the item.
     *
     * @return the subtype
     */
    @SchemaProperty("genre")
    E getSubtype();

    /**
     * Returns an image of the item.
     *
     * @return an image
     */
    @SchemaProperty("image")
    URL getImage();

    /**
     * Returns a description of the item.
     *
     * @return a description
     */
    @SchemaProperty("description")
    String getDescription();

    /**
     * Returns download links of the item.
     *
     * @return download links
     */
    @SchemaProperty("subjectOf")
    List<DownloadLink> getLinks();
}
