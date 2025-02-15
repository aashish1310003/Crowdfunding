package com.s8.Crowdfunding.repository;

import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT p FROM Project p WHERE p.status = :status AND p.user.userId = :userId")
    List<Project> findUserByUserIdAndStatus(@Param("status") String status, @Param("userId") Long userId);

    @Query("SELECT p FROM Project p WHERE p.status = :status AND p.user.userId = :userId " +
            "AND (SELECT COALESCE(SUM(d.amount),0) FROM Donation d WHERE d.project = p) < p.goalAmount")
    List<Project> listOfApprovedAndGoalNotReachedProject(@Param("status") String status, @Param("userId") Long userId);

    @Query("SELECT p FROM Project p WHERE p.status = :status AND p.user.userId = :userId " +
            "AND (SELECT COALESCE(SUM(d.amount),0) FROM Donation d WHERE d.project = p) >= p.goalAmount")
    List<Project> listOfApprovedAndGoalReachedProject(@Param("status") String status, @Param("userId") Long userId);
}
