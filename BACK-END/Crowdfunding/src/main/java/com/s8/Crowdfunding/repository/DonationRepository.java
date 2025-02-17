package com.s8.Crowdfunding.repository;

import com.s8.Crowdfunding.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.project.projectId = :id")
    Double sumOfAllDonationsById(@Param("id") Long id);

    List<Donation> findDonationsByDonor_UserIdAndStatusEquals(long userId, String status);

    List<Donation> findDonationsByProjectProjectIdAndStatusEquals(long projectId, String status);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.project.projectId = :projectId")
    Double sumOfAllDonationsByProjectId(@Param("projectId") long projectId);
}
