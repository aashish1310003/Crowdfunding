package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.exceptions.AppealLimitExceededException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.repository.ProjectRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final DonationService donationService;
    private final UserService userService;
    private final CommonService commonService;

    public ProjectService(ProjectRepository projectRepository, DonationService donationService, @Lazy UserService userService, CommonService commonService) {
        this.projectRepository = projectRepository;
        this.donationService = donationService;
        this.userService = userService;
        this.commonService = commonService;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Override
    public List<Project> getProjectByUser(Long id) {
        return projectRepository.findProjectByUser(userService.getUsersById(id));
    }

    @Override
    public Project getProjectByName(String title) {
        return projectRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with title: " + title));
    }

    @Override
    public List<Project> getProjectsByStatus(String status) {
        commonService.checkStatus(status);
        return projectRepository.findByStatus(status);
    }

    @Override
    public List<Project> getProjectsByGoalNotReached() {
        return projectRepository.findByStatus("APPROVED").stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) < project.getGoalAmount())
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getProjectsByGoalReached() {
        return projectRepository.findByStatus("APPROVED").stream()
                .filter(project -> donationService.sumOfAllDonationsById(project.getProjectId()) >= project.getGoalAmount())
                .collect(Collectors.toList());
    }

    @Override
    public Project updateProjectStatusById(Long id, String status) {
        commonService.checkStatus(status);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));

        if (project.getAppealCount() >= 3) {
            throw new AppealLimitExceededException("Appeal count exceeded for project with id " + id);
        }

        if (project.getStatus().equals("REJECTED") && status.equals("CREATED")) {
            project.setAppealCount(project.getAppealCount() + 1);
        }

        project.setStatus(status);
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> updateProjectById(Long id, String description, Double goalAmount, Date date, String reportUrl) {
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
