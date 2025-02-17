package com.s8.Crowdfunding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    String message;
    Object data;
}
