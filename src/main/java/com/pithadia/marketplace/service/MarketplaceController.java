package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Bid;
import com.pithadia.marketplace.entity.Buyer;
import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.exception.UnsupportedOperationException;
import com.pithadia.marketplace.repository.BidRepository;
import com.pithadia.marketplace.repository.BuyerRepository;
import com.pithadia.marketplace.repository.SellerRepository;
import com.pithadia.marketplace.request.BidRequest;
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
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BidRepository bidRepository;

    @PostMapping(value = "/project")
    public Project createProject(@RequestParam(value = "sellerId") Long sellerId, @RequestBody @Valid Project project) throws EntityNotFoundException {

        Seller seller = sellerRepository.findOne(sellerId);

        if (seller == null) {
            throw new EntityNotFoundException(Seller.class, "sellerId: " + sellerId.toString());
        }

        project.setSeller(seller);

        return projectService.createProject(project);
    }

    @GetMapping(value = "/project")
    public Project getProject(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @PostMapping(value = "bid")
    public ResponseEntity<Project> placeBid(@RequestBody BidRequest bidRequest) throws EntityNotFoundException, UnsupportedOperationException {

        Buyer buyer = buyerRepository.findOne(bidRequest.getBuyerId());

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Project project = projectService.getProject(bidRequest.getProjectId());

        if (project == null) {
            throw new EntityNotFoundException(Project.class, "projectId: " + bidRequest.getProjectId().toString());
        }

        Date currentDate = new Date();

        if ((project.getAuctionStartDate() == null || project.getAuctionEndDate() == null) || (!currentDate.after(project.getAuctionStartDate()) || !currentDate.before(project.getAuctionEndDate()))) {
            throw new UnsupportedOperationException(Project.class, "Project is Not Active");
        }

        if (bidRequest.getAmount().compareTo(project.getMaxBudget()) > 0) {
            throw new UnsupportedOperationException(Project.class, "Bid amount is greater than max budget!");
        }

        Bid bid = new Bid(bidRequest.getAmount(), buyer);

        // Placing the Bid
        project.placeBid(bid);

        projectService.createProject(project);

        bidRepository.save(bid);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping(value = "/projects")
    public List<Project> getAllOpenProjects() throws EntityNotFoundException {
        return projectService.getAllOpenProjects();
    }

    @GetMapping(value = "/status")
    public ResponseEntity getAuctionStatus(@RequestParam(value = "projectId") Long projectId) throws EntityNotFoundException {

        Project project = projectService.getProject(projectId);

        if (project == null) {
            throw new EntityNotFoundException(Project.class, "projectId: " + projectId.toString());
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
