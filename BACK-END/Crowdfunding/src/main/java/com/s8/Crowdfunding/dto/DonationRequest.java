package com.s8.Crowdfunding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonationRequest {
    private String donationId;
    private Double amount;
    private String donorVisibility; // PUBLIC or ANONYMOUS
    private String status;
    private Long userId; // Donor ID
    private Long projectId; // Project ID
}
