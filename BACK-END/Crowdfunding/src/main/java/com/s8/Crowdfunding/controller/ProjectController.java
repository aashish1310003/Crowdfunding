package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.exceptions.AppealLimitExceededException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    // Constructor-based dependency injection
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Endpoint to get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        System.out.println(projects);
        return ResponseEntity.ok(projects);
    }

    // Endpoint to get a project by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // Endpoint to get a project by its title
    @GetMapping("/title/{title}")
    public ResponseEntity<Project> getProjectByTitle(@PathVariable String title) {
        Project project = projectService.getProjectByName(title);
        return ResponseEntity.ok(project);
    }

    // Endpoint to get projects by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable String status) {
        List<Project> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok(projects);
    }

    // Endpoint to get projects with approved status and goal reached
    @GetMapping("/goal/reached")
    public ResponseEntity<List<Project>> getProjectsByGoalReached() {
        List<Project> projects = projectService.getProjectsByGoalReached();
        return ResponseEntity.ok(projects);
    }

    // Endpoint to get projects with approved status and goal not reached
    @GetMapping("/goal/not-reached")
    public ResponseEntity<List<Project>> getProjectsByGoalNotReached() {
        List<Project> projects = projectService.getProjectsByGoalNotReached();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        try {
            Project updatedProject = projectService.updateProjectStatusById(id, status.get("status"));
            return ResponseEntity.ok(updatedProject);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AppealLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
