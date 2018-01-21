package com.pithadia.marketplace.request;

import com.pithadia.marketplace.entity.Project;

public class ProjectRequest {

    private Long sellerId;

    private Project project;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
