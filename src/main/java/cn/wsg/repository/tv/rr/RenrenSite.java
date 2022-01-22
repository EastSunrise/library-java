package cn.wsg.repository.tv.rr;

import cn.wsg.commons.Region;
import cn.wsg.commons.internet.BaseSiteClient;
import cn.wsg.commons.internet.common.video.MovieGenre;
import cn.wsg.commons.internet.support.NotFoundException;
import cn.wsg.commons.internet.support.UnexpectedException;
import cn.wsg.commons.jackson.EnumDeserializers;
import cn.wsg.commons.util.AssertUtils;
import cn.wsg.repository.com.douban.RegionMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;

import java.util.List;
import java.util.Objects;

/**
 * @author Kingen
 */
public class RenrenSite extends BaseSiteClient implements RenrenTube {

    public RenrenSite() {
        super("RR TV", HttpHost.create("https://rr.tv/"));
    }

    /**
     * Only first 10 pages are available regardless of arguments of the request.
     */
    @Override
    public RenrenPage findAll(RenrenReq req) throws NotFoundException {
        String json;
        try {
            json = Lazy.MAPPER.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e);
        }
        RequestBuilder builder = create("web-api", BaseSiteClient.METHOD_POST, "/web/drama/filter_search");
        builder.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(json).build());
        RenrenResponse<RenrenResponse.Result> response = getObject(builder, Lazy.MAPPER, new TypeReference<>() {
        });
        return new RenrenPage(response.data.content, req, response.total);
    }

    @Override
    public List<SearchedItem> search(String keyword, int size) {
        AssertUtils.requireRange(size, 1, null);
        RequestBuilder builder = create("web-api", BaseSiteClient.METHOD_GET, "/web/drama/search/season");
        builder.addParameter("keywords", AssertUtils.requireNotBlank(keyword));
        builder.addParameter("size", String.valueOf(size));
        builder.addParameter("webChannel", "M_STATION");
        try {
            return getObject(builder, Lazy.MAPPER, new TypeReference<RenrenResponse<List<SearchedItem>>>() {
            }).getData();
        } catch (NotFoundException e) {
            throw new UnexpectedException(e);
        }
    }

    @Override
    public RenrenVideo findById(Integer id) {
        // todo
        return null;
    }

    private static class Lazy {

        private static final ObjectMapper MAPPER =
            new ObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT).registerModule(
                new SimpleModule().addDeserializer(MovieGenre.class,
                    EnumDeserializers.match(MovieGenre.class, (s, e) -> Objects.equals(s, e.getZhTitle())))
                    .addDeserializer(Region.class, new FromStringDeserializer<>(Region.class) {
                        @Override
                        protected Region _deserialize(String value, DeserializationContext ctxt) {
                            return RegionMapping.of(value);
                        }
                    }));
    }

    @Getter
    private static class RenrenResponse<T> {

        @JsonProperty("code")
        private String code;

        @JsonProperty("data")
        private T data;

        @JsonProperty("msg")
        private String msg;

        @JsonProperty("page")
        private Integer page;

        @JsonProperty("recordsTotal")
        private Integer total;

        @JsonProperty("requestId")
        private String requestId;

        RenrenResponse() {
        }

        @Getter
        static class Result {

            @JsonProperty("content")
            private List<RenrenVideoIndex> content;

            @JsonProperty("currentPage")
            private Integer currentPage;

            @JsonProperty("end")
            private Boolean end;

            @JsonProperty("isEnd")
            private Boolean isEnd;

            @JsonProperty("total")
            private Integer total;

            Result() {
            }
        }
    }
}
