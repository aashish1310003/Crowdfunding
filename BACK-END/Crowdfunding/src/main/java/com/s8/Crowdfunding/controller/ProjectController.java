package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.ApiResponse;
import com.s8.Crowdfunding.dto.ProjectRequest;
import com.s8.Crowdfunding.dto.ProjectResponse;
import com.s8.Crowdfunding.exceptions.AppealLimitExceededException;
import com.s8.Crowdfunding.exceptions.InvaildStatusException;
import com.s8.Crowdfunding.exceptions.InvalidUserAccessException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ModelMapper mapper;

    // Constructor-based dependency injection
    public ProjectController(ProjectService projectService, ModelMapper mapper, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.mapper = mapper;
    }

    // Endpoint to get all projects
    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects().stream()
                .map(project -> mapper.map(project, ProjectResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    // Endpoint to get a project by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(mapper.map(project, ProjectResponse.class));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("Project Not Found with this ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    // Endpoint to get a project by its title
    @GetMapping("/title/{title}")
    public ResponseEntity<?> getProjectByTitle(@PathVariable String title) {
        try {
            Project project = projectService.getProjectByName(title);
            return ResponseEntity.ok(mapper.map(project, ProjectResponse.class));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("Project Not Found with this ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    // Endpoint to get projects by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getProjectsByStatus(@PathVariable String status) {

        try {
            List<ProjectResponse> projects = projectService.getProjectsByStatus(status).stream()
                    .map(project -> mapper.map(project, ProjectResponse.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getProjectsforAdmin() {
        try {
            List<ProjectResponse> projects = projectService.getProjectForAdmin().stream()
                    .map(project -> mapper.map(project, ProjectResponse.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    @GetMapping("/admin/evaluated")
    public ResponseEntity<?> getProjectsforAdminEvaluated() {
        try {
            List<ProjectResponse> projects = projectService.getProjectForAdminEvaluated().stream()
                    .map(project -> mapper.map(project, ProjectResponse.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedProjects() {
        try {
            List<ProjectResponse> projects = projectService.getProjectForAdmin().stream()
                    .map(project -> mapper.map(project, ProjectResponse.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    // Endpoint to get projects with approved status and goal reached
    @GetMapping("/goal/reached")
    public ResponseEntity<?> getProjectsByGoalReached() {
        List<ProjectResponse> projects = projectService.getProjectsByGoalReached().stream()
                .map(project -> mapper.map(project, ProjectResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    // Endpoint to get projects with approved status and goal not reached
    @GetMapping("/goal/not-reached")
    public ResponseEntity<?> getProjectsByGoalNotReached() {
        List<ProjectResponse> projects = projectService.getProjectsByGoalNotReached().stream()
                .map(project -> mapper.map(project, ProjectResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/project/by/user/{userId}")
    public ResponseEntity<?> getProjectsByUser(@PathVariable("userId") Long userId) {
        try {
            List<ProjectResponse> projects = projectService.getProjectByUser(userId).stream()
                    .map(project -> mapper.map(project, ProjectResponse.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("Project Not Found with this USER_ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Project", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        try {
            Project updatedProject = projectService.updateProjectStatusById(id, status.get("status"));
            return ResponseEntity.ok(mapper.map(updatedProject, ProjectResponse.class));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("can't update status", e.getMessage()));
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        }catch (AppealLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("can't update status", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't update status", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectRequest projectRequest) {
        System.out.println(projectRequest.toString());
        try {
            Project project = projectService.createProject(projectRequest);
            return ResponseEntity.ok(mapper.map(project, ProjectResponse.class));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't create project", e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProject(@RequestBody ProjectRequest projectRequest) {
        System.out.println(projectRequest.toString());
        try {
            Project project = projectService.updateProjectById(projectRequest);
            return ResponseEntity.ok(mapper.map(project, ProjectResponse.class));
        }catch (InvalidUserAccessException e){
            return ResponseEntity.status(409).body(new ApiResponse("can't update project", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("can't update ", e.getMessage()));
        } catch (InvaildStatusException e) {
            return ResponseEntity.status(409).body(new ApiResponse("invalid status", e.getMessage()));
        }catch (AppealLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Appeal limit exceeds", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't update project", e.getMessage()));
        }
    }
}
