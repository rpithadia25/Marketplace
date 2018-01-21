package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketplaceService {

    @Autowired
    ProjectRepository projectRepository;

    @PostMapping(value = "project")
    public void createProject() {
        projectRepository.save(new Project("Sample Project", new Date(), new Date(), new BigDecimal(123),new BigDecimal(100), true));
    }

    @GetMapping(value = "/project")
    public Project getProject(@RequestParam(value = "projectId") Long projectId) {
        return projectRepository.findOne(projectId);
    }

    @GetMapping(value = "/projects")
    public List<Project> getAllOpenProjects() {

        List<Project> projects = new ArrayList<>();

        for (Project project : projectRepository.findAll()) {
            if (project.getOpen()) {
                projects.add(project);
            }
        }

        return projects;
    }

}
