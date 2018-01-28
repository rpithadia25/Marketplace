package com.pithadia.marketplace.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BidAddRequest {

    @NotNull
    private Long buyerId;

    @NotNull
    private Long projectId;

    @NotNull
    private BigDecimal amount;

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
