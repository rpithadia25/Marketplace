package com.pithadia.marketplace.service;

import com.pithadia.marketplace.entity.Bid;
import com.pithadia.marketplace.entity.Project;
import com.pithadia.marketplace.exception.EntityNotFoundException;
import com.pithadia.marketplace.repository.BidRepository;
import com.pithadia.marketplace.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BidRepository bidRepository;

    public Project getProject(Long projectId) throws EntityNotFoundException {

        Project project = projectRepository.findOne(projectId);

        if (project == null) {
            throw new EntityNotFoundException(Project.class, "projectId: " + projectId.toString());
        }

        return project;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllOpenProjects() throws EntityNotFoundException {

        List<Project> projects = new ArrayList<>();

        for (Project project : projectRepository.findAll()) {
            if (project.isActive()) {
                projects.add(project);
            }
        }

        if (projects.isEmpty()) {
            throw new EntityNotFoundException(Project.class, "All Open Projects");
        }

        return projects;
    }

    public void deleteProject(Project project) {

        // Delete all bids related to Project as well
        for (Bid bid : project.getBids()) {
            bidRepository.delete(bid);
        }

        projectRepository.delete(project);
    }

}
