package com.s8.Crowdfunding.repository;

import com.s8.Crowdfunding.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.project.projectId = :id")
    Double sumOfAllDonationsById(@Param("id") Long id);
}
