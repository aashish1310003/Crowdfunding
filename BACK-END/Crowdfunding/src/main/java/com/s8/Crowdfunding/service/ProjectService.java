package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.model.Status;
import com.s8.Crowdfunding.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    private ProjectRepository projectRepository;
    private DonationService donationService;

    public ProjectService(ProjectRepository projectRepository, DonationService donationService) {
        this.projectRepository = projectRepository;
        this.donationService = donationService;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Override
    public Project getProjectByName(String title) {
        return projectRepository.findByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with id: " + title));
    }

    @Override
    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    @Override
    public List<Project> getProjectsByGoalNotReached() {
//        return projectRepository.listOfApprovedAndGoalNotReachedProject();
        List<Project> projects = getProjectsByStatus("APPROVED");
        return projects.stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) < project.getGoalAmount())
                .peek(project -> System.out.println(project.getTitle())) // Logs project titles
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getProjectsByGoalReached() {
//        return projectRepository.listOfApprovedAndGoalNotReachedProject();
        List<Project> projects = getProjectsByStatus("APPROVED");
        return projects.stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) >= project.getGoalAmount())
                .peek(project -> System.out.println(project.getTitle())) // Logs project titles
                .collect(Collectors.toList());
    }

}
