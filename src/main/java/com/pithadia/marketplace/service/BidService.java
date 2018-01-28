package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Bid;
import com.pithadia.marketplace.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public Bid getBid(Long bidId) {
        return bidRepository.findOne(bidId);
    }

    public Bid saveBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public void deleteBid(Bid bid) {
        bidRepository.delete(bid);
    }
}
