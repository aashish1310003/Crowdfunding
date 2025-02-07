package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.model.Status;

import java.util.List;

public interface IProjectService {

    List<Project> getAllProjects();

    Project getProjectById(Long id);
    Project getProjectByName(String name);
    List<Project> getProjectsByStatus(String status);

    List<Project> getProjectsByGoalReached();
    List<Project> getProjectsByGoalNotReached();

}
