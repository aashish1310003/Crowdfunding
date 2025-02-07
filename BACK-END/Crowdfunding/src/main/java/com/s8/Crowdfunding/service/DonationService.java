package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class DonationService {

    private DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Double sumOfAllDonationsById(long donationId) {
        return donationRepository.sumOfAllDonationsById(donationId);
    }
}
