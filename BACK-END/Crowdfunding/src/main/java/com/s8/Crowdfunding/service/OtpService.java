package com.s8.Crowdfunding.service;

import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import java.util.concurrent.*;
import java.util.*;

@Service
public class OtpService {
    private final JavaMailSender mailSender;
    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private static final int OTP_EXPIRY_MINUTES = 5;

    public OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        startOtpExpiryScheduler();
    }

    public void generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        otpStorage.put(email, new OtpEntry(otp, System.currentTimeMillis()));
        sendOtpEmail(email, otp);
    }

    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpStorage.get(email);
        if (entry != null && otp.equals(entry.getOtp()) && !isOtpExpired(entry)) {
            // otpStorage.remove(email); // OTP should be used only once
            return true;
        }
        return false;
    }

    private boolean isOtpExpired(OtpEntry entry) {
        return System.currentTimeMillis() - entry.getTimestamp() > OTP_EXPIRY_MINUTES * 60 * 10000;
    }

    private void sendOtpEmail(String email, String otp) throws RuntimeException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis OTP is valid for " + OTP_EXPIRY_MINUTES + " minutes.");
        mailSender.send(message);
    }

    private void startOtpExpiryScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            otpStorage.entrySet().removeIf(entry -> isOtpExpired(entry.getValue()));
        }, 1, 1, TimeUnit.MINUTES); // Runs every 1 minute
    }

    private static class OtpEntry {
        private final String otp;
        private final long timestamp;

        public OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
