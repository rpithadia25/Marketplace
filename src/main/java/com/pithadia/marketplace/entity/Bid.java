package com.pithadia.marketplace.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Bid {

    @Id @GeneratedValue
    private Long id;

    private BigDecimal bidAmount;

    private Date bidDateTime;
}
