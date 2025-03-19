package com.s8.Crowdfunding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    private Long userId;
    private Long projectId;
    private String title;
    private String description;
    private Double goalAmount;
    private Date deadline;
    private String reportPdfUrl;

}
