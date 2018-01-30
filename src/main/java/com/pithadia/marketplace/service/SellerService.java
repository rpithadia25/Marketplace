package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public Seller getSeller(Long sellerId) throws EntityNotFoundException {

        Seller seller = sellerRepository.findOne(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId.toString());
        }

        return seller;
    }
}
