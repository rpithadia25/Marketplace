package com.pithadia.marketplace.repository;

import com.pithadia.marketplace.entity.Buyer;
import org.springframework.data.repository.CrudRepository;

public interface BuyerRepository extends CrudRepository<Buyer, Long> {
}
