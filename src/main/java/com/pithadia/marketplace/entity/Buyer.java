package com.pithadia.marketplace.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Buyer {

    @Id @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
