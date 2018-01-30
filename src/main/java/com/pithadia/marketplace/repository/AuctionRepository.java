package com.pithadia.marketplace.repository;

import com.pithadia.marketplace.entity.Auction;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by RP103864 on 1/29/2018.
 */
public interface AuctionRepository extends CrudRepository<Auction, Long> {
}
