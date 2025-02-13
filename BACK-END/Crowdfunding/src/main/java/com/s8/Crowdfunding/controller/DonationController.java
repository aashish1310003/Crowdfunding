package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.model.Donation;
import com.s8.Crowdfunding.service.DonationService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/donation")
public class DonationController {

    private DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/donation/{id}/project")
    public ResponseEntity<List<Donation>> getDonationByProjectId(@PathVariable long id) {
            return ResponseEntity.ok(donationService.getAllDonationsByProjectId(id));
    }

    @PostMapping("/donation/{id}/user")
    public ResponseEntity<List<Donation>> getDonationByUserId(@PathVariable long id) {
        return ResponseEntity.ok(donationService.getAllDonationsByUserId(id));
    }

}
