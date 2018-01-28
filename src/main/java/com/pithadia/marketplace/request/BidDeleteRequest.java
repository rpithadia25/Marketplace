package com.pithadia.marketplace.request;

import javax.validation.constraints.NotNull;

public class BidDeleteRequest {

    @NotNull
    private Long bidId;

    @NotNull
    private Long projectId;

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
