package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.ApiResponse;
import com.s8.Crowdfunding.dto.DonationRequest;
import com.s8.Crowdfunding.dto.DonationResponse;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.model.Donation;
import com.s8.Crowdfunding.service.DonationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;
    private final ModelMapper mapper;

    @Autowired
    public DonationController(DonationService donationService, ModelMapper mapper) {
        this.donationService = donationService;
        this.mapper = mapper;
    }

    @GetMapping("/donation/{id}/project")
    public ResponseEntity<?> getDonationByProjectId(@PathVariable long id) {
        try {
            return ResponseEntity.ok(donationService.getAllDonationsByProjectId(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("Project Not Found with this ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Donation", e.getMessage()));
        }
    }

    @GetMapping("/donation/sum/{id}/project")
    public ResponseEntity<?> getSumOfDonationByProjectId(@PathVariable long id) {
        try {
            Map<String,Double> mp = new HashMap<>();
            mp.put("amount" , donationService.sumOfAllDonationsById(id));
            return ResponseEntity.ok(mp);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("Project Not Found with this ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Donation", e.getMessage()));
        }
    }

    @GetMapping("/donation/{id}/user")
    public ResponseEntity<?> getDonationByUserId(@PathVariable long id) {
        try {
            return ResponseEntity.ok(donationService.getAllDonationsByUserId(id));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(409).body(new ApiResponse("User Not Found with this ID", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Donation", e.getMessage()));
        }
    }

    @PostMapping("/addDonation")
    public ResponseEntity<?> addDonation(@RequestBody DonationRequest donation) {
        try {
            return ResponseEntity.ok(mapper.map(donationService.createDonation(donation), DonationResponse.class));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("can't get Donation", e.getMessage()));
        }
    }
}
