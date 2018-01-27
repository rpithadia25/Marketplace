package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Bid;
import com.pithadia.marketplace.entity.Buyer;
import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.repository.BidRepository;
import com.pithadia.marketplace.repository.BuyerRepository;
import com.pithadia.marketplace.repository.ProjectRepository;
import com.pithadia.marketplace.repository.SellerRepository;
import com.pithadia.marketplace.request.BidRequest;
import com.pithadia.marketplace.request.ProjectRequest;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.xml.ws.Response;
import java.util.*;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketplaceService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BidRepository bidRepository;

    @PostMapping(value = "/project")
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest createProjectRequest) {

        Seller seller = sellerRepository.findOne(createProjectRequest.getSellerId());

        if (seller == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Project project = createProjectRequest.getProject();

        if (projectRepository.findOne(project.getId()) != null) {
            return new ResponseEntity<>(projectRepository.findOne(project.getId()), HttpStatus.OK);
        }

        project.setSeller(seller);

        projectRepository.save(project);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping(value = "/project")
    public ResponseEntity<Project> getProject(@RequestParam(value = "projectId") Long projectId) {

        Project project = projectRepository.findOne(projectId);

        if (project == null) {
            throw new EntityNotFoundException("Project Not Found");
        }

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping(value = "bid")
    public ResponseEntity<Project> placeBid(@RequestBody BidRequest bidRequest) {

        Buyer buyer = buyerRepository.findOne(bidRequest.getBuyerId());

        if (buyer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Project project = projectRepository.findOne(bidRequest.getProjectId());

        if (project == null) {
            throw new EntityNotFoundException("Project with id: " + bidRequest.getProjectId() + " Not Found");
        }

        Date currentDate = new Date();

        if (!currentDate.after(project.getAuctionStartDate()) || !currentDate.before(project.getAuctionEndDate())) {
            throw new UnsupportedOperationException("Project is Not Active");
        }

        if (bidRequest.getAmount().compareTo(project.getMaxBudget()) > 0) {
            throw new UnsupportedOperationException("Bid amount is greater than max budget!");
        }

        Bid bid = new Bid(bidRequest.getAmount(), buyer);

        // Placing the Bid
        project.placeBid(bid);

        projectRepository.save(project);

        bidRepository.save(bid);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping(value = "/projects")
    public ResponseEntity<List<Project>> getAllOpenProjects() {

        List<Project> projects = new ArrayList<>();

        Date currentDate = new Date();

        for (Project project : projectRepository.findAll()) {
            if (currentDate.after(project.getAuctionStartDate()) && currentDate.before(project.getAuctionEndDate())) {
                projects.add(project);
            }
        }

        if (projects.isEmpty()) {
            throw new EntityNotFoundException("No Open Projects Found");
        }

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping(value = "/status")
    public ResponseEntity getAuctionStatus(@RequestParam(value = "projectId") Long projectId) {

        Project project = projectRepository.findOne(projectId);

        if (project == null) {
            throw new EntityNotFoundException("Project with id: " + projectId + " Not Found");
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
