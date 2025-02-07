package com.s8.Crowdfunding.repository;

import com.s8.Crowdfunding.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(String status);

    @Query("SELECT p FROM Project p JOIN FETCH p.user")
    List<Project> findAllWithUser();

    Optional<Project> findByTitle(String name);
    Optional<Project> findById(Long id);  // Corrected to return Optional<Project>

    @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED'")
    List<Project> listOfApprovedProject();

    @Query("SELECT p FROM Project p WHERE p.status = 'PENDING'")
    List<Project> listOfPendingProject();

    @Query("SELECT p FROM Project p WHERE p.status = 'REJECTED'")  // Fixed status
    List<Project> listOfRejectedProject();

    @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED' " +
            "AND (SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.project = p) < p.goalAmount")
    List<Project> listOfApprovedAndGoalNotReachedProject();

    @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED' " +
            "AND (SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.project = p) >= p.goalAmount")
    List<Project> listOfApprovedAndGoalReachedProject();
}
