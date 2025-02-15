package com.s8.Crowdfunding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String password;

//    @Enumerated(EnumType.STRING)
    private String role;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "donor")
    private List<Donation> donations;
}
