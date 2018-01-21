package com.pithadia.marketplace.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Bid {

    @Id @GeneratedValue
    private Long id;

    private BigDecimal bidAmount;

    private Date bidDateTime;

    @ManyToOne
    private Project project;
}
