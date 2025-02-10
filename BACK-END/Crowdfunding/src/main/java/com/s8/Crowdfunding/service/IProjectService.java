package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Project;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IProjectService {

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    Project getProjectByName(String name);

    List<Project> getProjectsByStatus(String status);

    List<Project> getProjectsByGoalReached();

    List<Project> getProjectsByGoalNotReached();

    // Optional<Project> updateProjectById(Long id, String status);

    Project updateProjectStatusById(Long id, String status);

    Optional<Project> updateProjectById(Long id, String description, Double goalAmount, Date date, String reportUrl);
}
