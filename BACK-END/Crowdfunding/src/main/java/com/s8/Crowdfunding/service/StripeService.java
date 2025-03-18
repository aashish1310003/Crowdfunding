package com.s8.Crowdfunding.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import com.s8.Crowdfunding.dto.StripeRequest;
import com.s8.Crowdfunding.dto.StripeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    public StripeResponse checkoutPayment(StripeRequest request) {
        Stripe.apiKey = "sk_test_51QrH9qFR1CtvzQ43AEHTMOBSFpJoXSqzzzpb9lWoqDtLl1GeSp9TtEDRurxdqUxcIyqrYSCoNkGwTQvwppU11etL00qbcHQZnx";

        // Product Data
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", request.getName());

        // Price Data
        Map<String, Object> priceData = new HashMap<>();
        priceData.put("currency", request.getCurrency()); // ✅ FIXED: Use dynamic currency
        priceData.put("unit_amount", request.getAmount());
        priceData.put("product_data", productData);

        // Line Item
        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("quantity", request.getQuantity());
        lineItem.put("price_data", priceData);

        // Session Parameters
        Map<String, Object> params = new HashMap<>();
        params.put("mode", "payment");
        params.put("success_url",
                "http://localhost:3000/payment-success?session_id={CHECKOUT_SESSION_ID}&amount=" + request.getAmount()); // ✅
                                                                                                                         // Redirect
                                                                                                                         // to
                                                                                                                         // frontend
        params.put("cancel_url", "http://localhost:3000/payment-failure"); // ✅ Redirect to frontend
        params.put("line_items", List.of(lineItem));

        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment Session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Payment Session creation failed: " + e.getMessage())
                    .build();
        }
    }
}
