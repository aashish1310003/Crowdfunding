package com.s8.Crowdfunding.dto;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeRequest {

    private Long amount;
    private Long quantity;
    private String name;
    private String currency;

}
