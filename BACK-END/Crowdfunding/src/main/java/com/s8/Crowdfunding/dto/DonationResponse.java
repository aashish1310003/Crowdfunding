package com.s8.Crowdfunding.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class DonationResponse {
    private String donationId;
    private Double amount;
    private String status;
    private String stripeSessionUrl;
}
