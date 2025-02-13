package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Donation;
import com.s8.Crowdfunding.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {

    private DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Double sumOfAllDonationsById(long projectId) {
        return donationRepository.sumOfAllDonationsByProjectId(projectId);
    }

    public List<Donation> getAllDonationsByProjectId(long projectId) {
        return donationRepository.findDonationsByProjectProjectIdAndStatusEquals(projectId, "SUCCESS");
    }

    public List<Donation> getAllDonationsByUserId(long userId) {
        return donationRepository.findDonationsByDonor_UserIdAndStatusEquals(userId, "SUCCESS");
    }
}
