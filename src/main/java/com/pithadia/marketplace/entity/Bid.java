package com.pithadia.marketplace.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Bid {

    @Id @GeneratedValue
    private Long id;

    private BigDecimal bidAmount;

    @CreatedDate
    private Date bidDate;

    @ManyToOne
    private Buyer buyer;

    private Bid() {}

    public Bid(BigDecimal bidAmount, Buyer buyer) {
        this.bidAmount = bidAmount;
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidAmount=" + bidAmount +
                ", bidDate=" + bidDate +
                ", buyer=" + buyer +
                '}';
    }
}
