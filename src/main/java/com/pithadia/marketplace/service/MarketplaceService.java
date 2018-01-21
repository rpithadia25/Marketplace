package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.entity.Seller;
import com.pithadia.marketplace.repository.ProjectRepository;
import com.pithadia.marketplace.repository.SellerRepository;
import com.pithadia.marketplace.request.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketplaceService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping(value = "project")
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
    public Project getProject(@RequestParam(value = "projectId") Long projectId) {
        return projectRepository.findOne(projectId);
    }

    @GetMapping(value = "/projects")
    public List<Project> getAllOpenProjects() {

        List<Project> projects = new ArrayList<>();

        Date currentDate = new Date();

        for (Project project : projectRepository.findAll()) {
            if (currentDate.after(project.getAuctionStartDate()) && currentDate.before(project.getAuctionEndDate())) {
                projects.add(project);
            }
        }

        return projects;
    }

}
