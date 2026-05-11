package com.lcwd.electronic.store.services;

public interface EmailService {



    void sendEmail(String to, String subject, String body);
}
