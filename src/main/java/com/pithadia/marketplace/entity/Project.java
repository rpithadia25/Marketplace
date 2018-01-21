package com.pithadia.marketplace.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    private Integer minBidIndex;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Seller seller;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    // JPA needs a private no arg constructor
    private Project() {
    }

    public Project(String description, Date auctionStartDate, Date auctionEndDate, BigDecimal maxBudget, Seller seller) {
        this.description = description;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.maxBudget = maxBudget;
        this.seller = seller;
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

    public Integer getMinBidIndex() {
        return minBidIndex;
    }

    public void setMinBidIndex(Integer minBidIndex) {
        this.minBidIndex = minBidIndex;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", auctionStartDate=" + auctionStartDate +
                ", auctionEndDate=" + auctionEndDate +
                ", maxBudget=" + maxBudget +
                ", minBidIndex=" + minBidIndex +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", bids=" + bids +
                '}';
    }
}
