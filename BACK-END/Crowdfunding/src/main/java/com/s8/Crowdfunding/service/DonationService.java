package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.dto.DonationRequest;
import com.s8.Crowdfunding.model.Donation;
import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.repository.DonationRepository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DonationService {

    private DonationRepository donationRepository;
    private ProjectService projectService;
    private UserService userService;

    public DonationService(DonationRepository donationRepository, @Lazy ProjectService projectService,
            @Lazy UserService userService) {
        this.donationRepository = donationRepository;
        this.projectService = projectService;
        this.userService = userService;
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

    public Donation createDonation(DonationRequest donationRequest) {
        if (donationRequest == null) {
            throw new IllegalArgumentException("Donation request cannot be null.");
        }

        if (Objects.isNull(donationRequest.getDonationId()) ||
                Objects.isNull(donationRequest.getAmount()) ||
                Objects.isNull(donationRequest.getDonorVisibility()) ||
                Objects.isNull(donationRequest.getStatus()) ||
                Objects.isNull(donationRequest.getUserId()) ||
                Objects.isNull(donationRequest.getProjectId())) {
            throw new IllegalArgumentException("All donation fields are required and cannot be null.");
        }
        Donation donation = new Donation();
        donation.setDonationId(donationRequest.getDonationId());
        donation.setDonationDate(new Date());
        donation.setProject(projectService.getProjectById(donationRequest.getProjectId()));
        donation.setDonor(userService.getUsersById(donationRequest.getUserId()));
        donation.setAmount(donationRequest.getAmount());
        donation.setStatus(donationRequest.getStatus());
        donation.setDonorVisibility(donationRequest.getDonorVisibility());
        return donationRepository.save(donation);

    }

}
