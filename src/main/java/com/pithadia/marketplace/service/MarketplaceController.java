package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Bid;
import com.pithadia.marketplace.entity.Buyer;
import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.exception.UnsupportedOperationException;
import com.pithadia.marketplace.exception.UserUnauthorizedException;
import com.pithadia.marketplace.repository.SellerRepository;
import com.pithadia.marketplace.request.BidAddRequest;
import com.pithadia.marketplace.request.BidDeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketplaceController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private BidService bidService;

    @PostMapping(value = "/project")
    public Project createProject(@RequestParam(value = "sellerId") Long sellerId, @RequestBody @Valid Project project) throws EntityNotFoundException {

        Seller seller = sellerRepository.findOne(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId.toString());
        }

        project.setSeller(seller);

        return projectService.saveProject(project);
    }

    @GetMapping(value = "/project")
    public Project getProject(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @GetMapping(value = "/projects")
    public List<Project> getAllOpenProjects() throws EntityNotFoundException {
        return projectService.getAllOpenProjects();
    }

    @DeleteMapping(value = "/project")
    public ResponseEntity deleteProject(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {

        Project project = projectService.getProject(projectId);

        projectService.deleteProject(project);

        return ResponseEntity.ok().body("Project Deleted!");
    }

    @PostMapping(value = "/bid")
    public ResponseEntity<Project> placeBid(@RequestBody BidAddRequest bidAddRequest) throws EntityNotFoundException, UnsupportedOperationException {

        Buyer buyer = buyerService.getBuyer(bidAddRequest.getBuyerId());

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Project project = projectService.getProject(bidAddRequest.getProjectId());

        Date currentDate = new Date();

        if ((project.getAuctionStartDate() == null || project.getAuctionEndDate() == null) || (!currentDate.after(project.getAuctionStartDate()) || !currentDate.before(project.getAuctionEndDate()))) {
            throw new UnsupportedOperationException(Project.class, "Project is Not Active");
        }

        if (bidAddRequest.getAmount().compareTo(project.getMaxBudget()) > 0) {
            throw new UnsupportedOperationException(Project.class, "Bid amount is greater than max budget!");
        }

        Bid bid = new Bid(bidAddRequest.getAmount(), buyer);

        // Placing the Bid
        project.placeBid(bid);

        projectService.saveProject(project);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping(value = "/bid")
    public ResponseEntity deleteBid(@RequestBody BidDeleteRequest bidDeleteRequest, @RequestParam(value = "buyerId") Long buyerId) throws EntityNotFoundException, UserUnauthorizedException, UnsupportedOperationException {

        Project project = projectService.getProject(bidDeleteRequest.getProjectId());

        if (!project.isAuctionActive()) {
            throw new UnsupportedOperationException(Project.class, "Auction is not Active. Bid cannot be deleted.");
        }

        Buyer buyer = buyerService.getBuyer(buyerId);

        Bid bid = bidService.getBid(bidDeleteRequest.getBidId());

        if (bid.getBuyer().getId() != buyer.getId()) {
            throw new UserUnauthorizedException(Buyer.class, "Buyer with id : " + buyerId + " not authorized to delete bid with id : " + bid.getId());
        }

        List<Bid> bids = project.getBids();

        if (bids.size() == 1 && bids.get(0).getId() == bid.getId()) {
            bids.remove(0);
            bidService.deleteBid(bid);
            project.setBuyerWithMinBid(null);
            projectService.saveProject(project);
        } else {
            for (int i = 0; i < bids.size(); i++) {
                Bid currentBid = bids.get(i);
                if (currentBid.getId() == bidDeleteRequest.getBidId()) {
                    bids.remove(i);
                    bidService.deleteBid(bid);
                    if (project.getMinBidIndex() == i) {
                        Collections.sort(bids, Comparator.comparing(Bid::getBidAmount));
                        project.setBuyerWithMinBid(bids.get(0).getBuyer());
                        projectService.saveProject(project);
                    }
                    break;
                }
            }
        }

        return ResponseEntity.ok().body("Bid Deleted!");
    }

    @GetMapping(value = "/status")
    public ResponseEntity getAuctionStatus(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {

        Project project = projectService.getProject(projectId);

        if (!project.isAuctionActive()) {
            return ResponseEntity.ok().body("Project is not on Auction");
        }

        Date currentDate = new Date();

        if (currentDate.before(project.getAuctionStartDate())) {
            return ResponseEntity.ok().body("Auction is Not Active");
        }

        if (currentDate.after(project.getAuctionStartDate()) && currentDate.before(project.getAuctionEndDate())) {
            return ResponseEntity.ok().body("Auction is still Active");
        }

        Buyer buyer = project.getBuyerWithMinBid();

        return ResponseEntity.ok().body("Buyer " + buyer.getFirstName() + " won the auction for a Bid of " + project.getLowestBid());
    }
}
