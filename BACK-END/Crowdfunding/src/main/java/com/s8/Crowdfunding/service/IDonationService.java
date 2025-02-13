package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Donation;
import com.s8.Crowdfunding.repository.DonationRepository;

import java.util.List;

public interface IDonationService {

    // Donation createDonation();
    Donation getDonationByProject(long projectId);
    List<Donation> getAllDonationsByProjectId(long projectId);
    List<Donation> getAllDonationsByUserId(long donationId);
}
