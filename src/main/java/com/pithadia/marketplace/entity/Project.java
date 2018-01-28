package com.pithadia.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date auctionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date auctionEndDate;

    @NotNull
    private BigDecimal maxBudget;

    private Integer minBidIndex;

    @ManyToOne
    private Buyer buyerWithMinBid;

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

    public Buyer getBuyerWithMinBid() {
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
            return new BigDecimal(0);
        }

        return bids.get(minBidIndex).getBidAmount();
    }

    public Boolean isAuctionActive() {

        if (auctionStartDate == null || auctionEndDate == null) {
            return false;
        }

        Date currentDate = new Date();

        if (currentDate.after(auctionStartDate) && currentDate.before(auctionEndDate)) {
            return true;
        }

        return false;
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
                ", buyerWithMinBid=" + buyerWithMinBid +
                ", seller=" + seller +
                ", bids=" + bids +
                '}';
    }
}
