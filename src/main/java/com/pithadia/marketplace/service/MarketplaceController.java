package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.*;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.exception.UnsupportedOperationException;
import com.pithadia.marketplace.exception.UserUnauthorizedException;
import com.pithadia.marketplace.request.BidAddRequest;
import com.pithadia.marketplace.request.BidDeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketplaceController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private BidService bidService;

    @Autowired
    private AuctionService auctionService;

    @PostMapping(value = "/projects")
    public Project createProject(@RequestParam(value = "sellerId") Long sellerId, @RequestBody @Valid Project project) throws EntityNotFoundException {

        Seller seller = sellerService.getSeller(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId.toString());
        }

        project.setSeller(seller);

        return projectService.saveProject(project);
    }

    @GetMapping(value = "/projects/{projectId}")
    public Project getProject(@PathVariable(value = "projectId") Long projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @DeleteMapping(value = "/projects/{projectId}")
    public ResponseEntity deleteProject(@PathVariable(value = "projectId") Long projectId) throws EntityNotFoundException {

        Project project = projectService.getProject(projectId);

        projectService.deleteProject(project);

        return ResponseEntity.ok().body("Project Deleted!");
    }

    @GetMapping(value = "/projects")
    public List<Project> getAllOpenProjects() throws EntityNotFoundException {
        return projectService.getAllOpenProjects();
    }

    @PostMapping(value = "/bids")
    public ResponseEntity<Project> placeBid(@RequestBody @Valid BidAddRequest bidAddRequest) throws EntityNotFoundException, UnsupportedOperationException {

        Buyer buyer = buyerService.getBuyer(bidAddRequest.getBuyerId());

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Project project = projectService.getProject(bidAddRequest.getProjectId());

        if (!project.isActive()) {
            throw new UnsupportedOperationException(Project.class, "Project is Not Active");
        }

        if (bidAddRequest.getAmount().compareTo(project.getMaxBudget()) > 0) {
            throw new UnsupportedOperationException(Project.class, "Bid amount is greater than max budget!");
        }

        if (bidAddRequest.getAmount().compareTo(project.getLowestBid()) >= 0) {
            throw new UnsupportedOperationException(Project.class, "Bid amount is greater than / equal to lowest Bid!");
        }

        Bid bid = new Bid(bidAddRequest.getAmount(), buyer);

        // Placing the Bid
        project.placeBid(bid);

        projectService.saveProject(project);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping(value = "/bids")
    public ResponseEntity deleteBid(@RequestBody BidDeleteRequest bidDeleteRequest, @RequestParam(value = "buyerId") Long buyerId) throws EntityNotFoundException, UserUnauthorizedException, UnsupportedOperationException {

        Project project = projectService.getProject(bidDeleteRequest.getProjectId());

        if (!project.isActive()) {
            throw new UnsupportedOperationException(Project.class, "Auction is not Active. Bid cannot be deleted.");
        }

        Buyer buyer = buyerService.getBuyer(buyerId);

        Bid bid = bidService.getBid(bidDeleteRequest.getBidId());

        if (bid == null) {
            throw new EntityNotFoundException(Bid.class, "bidId: " + bidDeleteRequest.getBidId());
        }

        if (bid.getBuyer().getId() != buyer.getId()) {
            throw new UserUnauthorizedException(Buyer.class, "Buyer with id : " + buyerId + " not authorized to delete bid with id : " + bid.getId());
        }

        project.deleteBid(bid);

        bidService.deleteBid(bid);

        projectService.saveProject(project);

        return ResponseEntity.ok().body("Bid Deleted!");
    }

    @PostMapping(value = "/auctions")
    public Project startAuction(@RequestParam(value = "sellerId") Long sellerId, @RequestParam(value = "projectId") Long projectId, @RequestBody @Valid Auction auction) throws EntityNotFoundException, UserUnauthorizedException, UnsupportedOperationException {

        Seller seller = sellerService.getSeller(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId);
        }

        Project project = projectService.getProject(projectId);

        if (project == null) {
            throw new EntityNotFoundException(Seller.class, "projectId: " + projectId);
        }

        if (project.isActive()) {
            throw new UnsupportedOperationException(Auction.class, "Auction is already active!");
        }

        if (project.getSeller().getId() != sellerId) {
            throw new UserUnauthorizedException(Seller.class, "Seller with id : " + sellerId + " not authorized to create auction for Project with id : " + projectId);
        }

        auctionService.createAuction(auction);

        project.setAuction(auction);

        return projectService.saveProject(project);
    }

    @DeleteMapping(value = "/auctions")
    public ResponseEntity stopAuction(@RequestParam(value = "sellerId") Long sellerId, @RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException, UserUnauthorizedException, UnsupportedOperationException {

        Seller seller = sellerService.getSeller(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId);
        }

        Project project = projectService.getProject(projectId);

        if (project == null) {
            throw new EntityNotFoundException(Auction.class, "projectId: " + projectId);
        }

        if (!project.isActive()) {
            return ResponseEntity.ok().body("Auction is NOT Active.");
        }

        if (sellerId != project.getSeller().getId()) {
            throw new UserUnauthorizedException(Seller.class, "Seller with id: " + sellerId + " is not authorized to stop this auction.");
        }

        Auction auction = project.getAuction();

        project.setAuction(null);

        auctionService.deleteAuction(auction);

        return ResponseEntity.ok().body("Auction Stopped");
    }

    @GetMapping(value = "/auctions/status")
    public ResponseEntity getAuctionStatus(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {

        Date currentDate = new Date();

        Project project = projectService.getProject(projectId);

        if (project == null) {
            throw new EntityNotFoundException(Project.class, "projectId:" + projectId);
        }

        Auction auction = project.getAuction();

        if (auction == null) {
            if (project.getBids().size() > 0) {
                return ResponseEntity.ok().body("Buyer " + project.getBuyerWithMinBid().getFullName() + " won the auction for a Bid of " + project.getLowestBid());
            }
            return ResponseEntity.ok().body("Auction is NOT Active yet.");
        }

        if (currentDate.before(auction.getStartDate())) {
            return ResponseEntity.ok().body("Auction is NOT Active yet. It will start accepting bids from : " + auction.getStartDate());
        }

        if (project.isActive()) {
            if (project.getNumberOfBids() == 0) {
                return ResponseEntity.ok().body("No Bids Yet!");
            }
            return ResponseEntity.ok().body("Auction is still Active. Current lowest Bid is by: " + project.getBuyerWithMinBid().getFullName() + " for: $" + project.getLowestBid());
        }

        Buyer buyer = project.getBuyerWithMinBid();

        return ResponseEntity.ok().body("Buyer " + buyer.getFirstName() + " won the auction for a Bid of " + project.getLowestBid());
    }
}
