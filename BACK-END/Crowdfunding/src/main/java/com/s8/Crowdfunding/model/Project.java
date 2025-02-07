package com.s8.Crowdfunding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String title;
    private String description;
    private Double goalAmount;
    private Date deadline;
    private String reportPdfUrl;
    private int appealCount;
//    @Enumerated(EnumType.STRING)
    private String status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<Donation> donations;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<Report> reports;
}
