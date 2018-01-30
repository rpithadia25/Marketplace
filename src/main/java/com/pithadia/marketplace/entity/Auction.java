package com.pithadia.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by RP103864 on 1/29/2018.
 */

@Entity
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date endDate;

    public Long getId() {
        return id;
    }

    private Auction() {

    }

    public Auction(Date auctionStartDate, Date auctionEndDate) {
        this.startDate = auctionStartDate;
        this.endDate = auctionEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean isActive() {

        Date currentDate = new Date();

        if (currentDate.after(startDate) && currentDate.before(endDate)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
