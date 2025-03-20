package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.dto.ProjectRequest;
import com.s8.Crowdfunding.exceptions.AppealLimitExceededException;
import com.s8.Crowdfunding.exceptions.InvaildStatusException;
import com.s8.Crowdfunding.exceptions.InvalidUserAccessException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final DonationService donationService;
    private final UserService userService;
    private final CommonService commonService;
    private final ModelMapper mapper;

    public ProjectService(ProjectRepository projectRepository, DonationService donationService, @Lazy UserService userService, CommonService commonService, ModelMapper mapper) {
        this.projectRepository = projectRepository;
        this.donationService = donationService;
        this.userService = userService;
        this.commonService = commonService;
        this.mapper = mapper;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("❌ Project not found with id: " + id);  // Debugging log
                    return new ResourceNotFoundException("Project not found with id: " + id);
                });
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
    public List<Project> getProjectForAdmin(){
        List<Project> projects = getProjectsByStatus("CREATED");
        projects.addAll(getProjectsByStatus("PENDING"));
        return projects;
    }

    @Override
    public List<Project> getApprovedProjectsn(){
        return getProjectsByStatus("APPROVED");
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


        if ((project.getStatus().equals("CREATED") || project.getStatus().equals("PENDING")) && status.equals("REJECTED")) {
            project.setAppealCount(project.getAppealCount() + 1);
        }

        project.setStatus(status);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProjectById(ProjectRequest projectRequest) {
        Project currProject = getProjectById(projectRequest.getProjectId());
        if (currProject.getAppealCount() >= 3) {
            throw new AppealLimitExceededException("Appeal count exceeded for this project");
        }if(currProject.getUser().getUserId() != projectRequest.getUserId()) {
            throw new InvalidUserAccessException("Only project owner can access");
        }
        return Optional.of(currProject)
                .filter(project -> project.getStatus().equals("REJECTED"))
                .map(project -> {
                    project.setTitle(projectRequest.getTitle());
                    project.setDescription(projectRequest.getDescription());
                    project.setGoalAmount(projectRequest.getGoalAmount());
                    project.setDeadline(projectRequest.getDeadline());
                    project.setReportPdfUrl(projectRequest.getReportPdfUrl());
                    project.setStatus("PENDING");
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new InvaildStatusException("Project must be in 'REJECTED' status to update"));
    }

    @Override
    public Project createProject(ProjectRequest projectRequest){
        Project project = mapper.map(projectRequest, Project.class);
        System.out.println(project.getProjectId());
        project.setAppealCount(0);
        project.setUser(userService.getUsersById(projectRequest.getUserId()));
        project.setStatus("CREATED");
        project.setProjectId(null);
        System.out.println(project.getProjectId());
        return projectRepository.save(project);
    }
}
