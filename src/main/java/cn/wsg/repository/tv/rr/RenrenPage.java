package cn.wsg.repository.tv.rr;

import cn.wsg.commons.internet.page.AmountCountablePageImpl;

import java.util.List;

/**
 * A paged result of {@link RenrenVideoIndex}.
 *
 * @author Kingen
 */
public class RenrenPage extends AmountCountablePageImpl<RenrenVideoIndex> {

    private static final long serialVersionUID = 7967804881652489036L;

    RenrenPage(List<RenrenVideoIndex> content, RenrenReq req, int total) {
        super(content, req, total);
    }

    @Override
    public RenrenReq nextPageReq() {
        return (RenrenReq)super.nextPageReq();
    }

    @Override
    public RenrenReq previousPageReq() {
        return (RenrenReq)super.previousPageReq();
    }
}
