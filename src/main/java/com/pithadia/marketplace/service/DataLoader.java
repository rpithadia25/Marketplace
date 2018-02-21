package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Buyer;
import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.repository.BuyerRepository;
import com.pithadia.marketplace.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataLoader {

    private SellerRepository sellerRepository;
    private BuyerRepository buyerRepository;

    @Autowired
    public DataLoader(SellerRepository sellerRepository, BuyerRepository buyerRepository) {
        this.sellerRepository = sellerRepository;
        this.buyerRepository = buyerRepository;
    }

    @PostConstruct
    private void loadData() {

        Seller seller1 = new Seller("Jane", "Doe", "Wayne Enterprise", "dan@mail.com");
        sellerRepository.save(seller1);

        Seller seller2 = new Seller("Dan", "Doe", "Wayne Enterprise", "dan@mail.com");
        sellerRepository.save(seller2);

        Buyer buyer1 = new Buyer("John", "Doe", "john@mail.com");
        buyerRepository.save(buyer1);

        Buyer buyer2 = new Buyer("Diego", "Garcia", "diego@mail.com");
        buyerRepository.save(buyer2);
    }
}
