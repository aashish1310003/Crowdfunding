package com.s8.Crowdfunding.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;
    private Double amount;
    private Date donationDate;
    private String donorVisibility;
    private String status;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users donor;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}
