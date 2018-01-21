package com.pithadia.marketplace.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;

@Entity
public class Auction {

    @Id @GeneratedValue
    private Long id;

    private HashMap<Long, Project> projects;

}
