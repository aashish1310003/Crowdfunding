package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.StripeRequest;
import com.s8.Crowdfunding.dto.StripeResponse;
import com.s8.Crowdfunding.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class StripeCheckoutController {
    private StripeService stripeService;
    public StripeCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe/checkout")
    public ResponseEntity<StripeResponse> checkoutStripe(@RequestBody StripeRequest stripeRequest){
        StripeResponse stripeResponse = stripeService.checkoutPayment(stripeRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
}
