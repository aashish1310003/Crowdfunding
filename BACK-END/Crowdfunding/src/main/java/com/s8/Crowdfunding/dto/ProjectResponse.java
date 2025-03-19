package com.s8.Crowdfunding.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ProjectResponse {
    private Long projectId;
    private Long userId;
    private String title;
    private String description;
    private Double goalAmount;
    private Date deadline;
    private String reportPdfUrl;
    private int appealCount;
    private String status;
}
