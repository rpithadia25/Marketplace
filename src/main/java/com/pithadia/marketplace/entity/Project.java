package com.pithadia.marketplace.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Project {

    @Id @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Date auctionStartDate;

    private Date auctionEndDate;

    private BigDecimal maxBudget;

    private BigDecimal minBid;

    private Boolean isOpen;

    @OneToMany(mappedBy = "project")
    private List<Bid> bids;

    // JPA need a private no arg constructor
    private Project() {
    }

    public Project(String description, Date auctionStartDate, Date auctionEndDate, BigDecimal maxBudget, BigDecimal minBid, Boolean isOpen) {
        this.description = description;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.maxBudget = maxBudget;
        this.minBid = minBid;
        this.isOpen = isOpen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAuctionStartDate() {
        return auctionStartDate;
    }

    public void setAuctionStartDate(Date auctionStartDate) {
        this.auctionStartDate = auctionStartDate;
    }

    public Date getAuctionEndDate() {
        return auctionEndDate;
    }

    public void setAuctionEndDate(Date auctionEndDate) {
        this.auctionEndDate = auctionEndDate;
    }

    public BigDecimal getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(BigDecimal maxBudget) {
        this.maxBudget = maxBudget;
    }

    public BigDecimal getMinBid() {
        return minBid;
    }

    public void setMinBid(BigDecimal minBid) {
        this.minBid = minBid;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", auctionStartDate=" + auctionStartDate +
                ", auctionEndDate=" + auctionEndDate +
                ", maxBudget=" + maxBudget +
                ", minBid=" + minBid +
                ", isOpen=" + isOpen +
                '}';
    }
}
