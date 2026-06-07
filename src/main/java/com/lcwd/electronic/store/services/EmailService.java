package com.lcwd.electronic.store.services;

public interface EmailService {



    void sendOtp(String email, String otp);

    void sendResetOtp(String email, String otp);

    void sendEmail(String toEmail, String name);

    public void sendWelcomeEmail(String toEmail, String subject, String body);

}
