package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.dto.ProjectRequest;
import com.s8.Crowdfunding.model.Project;

import java.util.List;

public interface IProjectService {

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    List<Project> getProjectByUser(Long id);

    Project getProjectByName(String name);

    List<Project> getProjectsByStatus(String status);

    List<Project> getProjectsByGoalReached();

    List<Project> getProjectForAdmin();

    List<Project> getProjectForAdminEvaluated();
    

    List<Project> getApprovedProjects();

    List<Project> getProjectsByGoalNotReached();

    // Optional<Project> updateProjectById(Long id, String status);

    Project updateProjectStatusById(Long id, String status);

//    Optional<Project> updateProjectById(Long id, String description, Double goalAmount, Date date, String reportUrl);

    Project updateProjectById(ProjectRequest projectRequest);

    Project createProject(ProjectRequest projectRequest);
}
