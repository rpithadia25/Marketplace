package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Buyer;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    public Buyer getBuyer(Long buyerId) throws EntityNotFoundException {

        Buyer buyer = buyerRepository.findOne(buyerId);;

        if (buyer == null) {
            throw new EntityNotFoundException(Buyer.class, "buyerId: " + buyerId.toString());
        }

        return buyer;
    }
}
