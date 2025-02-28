package com.s8.Crowdfunding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String role; // Or use an Enum for roles

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "donor")
    private List<Donation> donations;

    // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> role); // Convert role to GrantedAuthority
    }

    @Override
    public String getUsername() {
        return email; // Use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters and Setters
}