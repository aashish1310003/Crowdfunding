package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.exceptions.AppealLimitExceededException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        // return projectRepository.listOfApprovedAndGoalNotReachedProject();
        List<Project> projects = getProjectsByStatus("APPROVED");
        return projects.stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) < project
                        .getGoalAmount())
                .peek(project -> System.out.println(project.getTitle())) // Logs project titles
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getProjectsByGoalReached() {
        // return projectRepository.listOfApprovedAndGoalNotReachedProject();
        List<Project> projects = getProjectsByStatus("APPROVED");
        return projects.stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) >= project
                        .getGoalAmount())
                .peek(project -> System.out.println(project.getTitle())) // Logs project titles
                .collect(Collectors.toList());
    }

    @Override
    public Project updateProjectStatusById(Long id, String status) {
        return projectRepository.findById(id)
                // .filter(project -> project.getAppealCount() < 3)
                .map(project -> {
                    if (project.getAppealCount() >= 3) {
                        throw new AppealLimitExceededException("Appeal count exceeded for project with id " + id);
                    }
                    if (project.getStatus().equals("REJECTED") && status.equals("CREATED")) {
                        project.setAppealCount(project.getAppealCount() + 1);
                    }
                    project.setStatus(status);
                    return projectRepository.save(project);
                }).orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
    }

    @Override
    public Optional<Project> updateProjectById(Long id, String description, Double goalAmount, Date date,
            String reportUrl) {
        return projectRepository.findById(id)
                .filter(project -> project.getStatus().equals("REJECTED"))
                .map(project -> {
                    project.setDescription(description);
                    project.setGoalAmount(goalAmount);
                    project.setDeadline(date);
                    project.setReportPdfUrl(reportUrl);
                    return projectRepository.save(project);
                });
    }

}
