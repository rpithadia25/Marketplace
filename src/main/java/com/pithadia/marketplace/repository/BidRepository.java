package com.pithadia.marketplace.repository;

import com.pithadia.marketplace.entity.Bid;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Long> {
}
