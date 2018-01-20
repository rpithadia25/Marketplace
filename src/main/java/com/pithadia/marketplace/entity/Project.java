package com.pithadia.marketplace.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Project {

    @Id @GeneratedValue
    private Long id;

    private String description;

    private Date auctionStartDate;

    private Date auctionEndDate;

    private BigDecimal maxBudget;

    private BigDecimal minBid;

    private Boolean isOpen;
}
