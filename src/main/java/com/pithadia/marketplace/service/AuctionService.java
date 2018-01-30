package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Auction;
import com.pithadia.marketplace.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by RP103864 on 1/29/2018.
 */

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public void deleteAuction(Auction auction) {
        auctionRepository.delete(auction);
    }
}
