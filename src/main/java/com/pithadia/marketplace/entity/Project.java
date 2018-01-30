package com.pithadia.marketplace.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @NotNull
    @Min(1)
    private BigDecimal maxBudget;

    private Integer minBidIndex;

    @ManyToOne
    private Buyer buyerWithMinBid;

    @ManyToOne
    private Seller seller;

    @OneToOne(targetEntity = Auction.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "auctionId", referencedColumnName = "id")
    private Auction auction;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    private Boolean isActive = false;

    // JPA needs a private no arg constructor
    private Project() {
    }

    public Project(String description, BigDecimal maxBudget, Seller seller) {
        this.description = description;
        this.maxBudget = maxBudget;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Buyer getBuyerWithMinBid() {

        if (bids.size() == 0 || bids == null) {
            buyerWithMinBid = null;
        }

        return buyerWithMinBid;
    }

    public void setBuyerWithMinBid(Buyer buyerWithMinBid) {
        this.buyerWithMinBid = buyerWithMinBid;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public Boolean isActive() {
        if (auction == null) {
            return false;
        }
        return auction.isActive();
    }

    public void setActive(Boolean status) {
        isActive = status;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        if (auction != null) {
            this.buyerWithMinBid = null;
            this.bids.clear();
        }
        this.auction = auction;
    }

    public Integer getNumberOfBids() {
        return bids.size();
    }

    public void placeBid(Bid bid) {

        if (bids.size() == 0) {
            minBidIndex = 0;
            buyerWithMinBid = bid.getBuyer();
        } else if (bids.get(minBidIndex).getBidAmount().compareTo(bid.getBidAmount()) > 0) {
            minBidIndex++;
            buyerWithMinBid = bid.getBuyer();
        }
        bids.add(bid);
    }

    public BigDecimal getLowestBid() {

        if (bids == null || bids.size() == 0) {
            return new BigDecimal(Integer.MAX_VALUE);
        }

        return bids.get(minBidIndex).getBidAmount();
    }

    public void deleteBid(Bid bid) {

        bids.remove(bid);

        if (bids.size() == 0 || bids == null) {
            minBidIndex = null;
            buyerWithMinBid = null;
        } else {
            Collections.sort(bids, Comparator.comparing(Bid::getBidAmount));
            buyerWithMinBid = bids.get(0).getBuyer();
            minBidIndex = 0;
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", maxBudget=" + maxBudget +
                ", minBidIndex=" + minBidIndex +
                ", buyerWithMinBid=" + buyerWithMinBid +
                ", seller=" + seller +
                ", bids=" + bids +
                '}';
    }
}
