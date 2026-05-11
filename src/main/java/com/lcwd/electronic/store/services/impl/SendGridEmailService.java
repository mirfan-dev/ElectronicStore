package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.SendGridConfig;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendGridEmailService {

    private final SendGrid sendGrid;
    private final SendGridConfig config;

    public void sendEmail(String toEmail, String name) {
        String subject = "Welcome to Our Platform";
        String text = "Hello " + name + ",\n\nThanks for registering with us!\n\nRegards,\nAuthify Team";
        sendEmail(toEmail, subject, text);
    }

    public void sendWelcomeEmail(String toEmail, String subject, String body) {
        sendEmail(toEmail, subject, body);
    }

    public void sendOtp(String email, String otp) {
        String subject = "Your Account Verification OTP";
        String message = "Dear user,\n\nYour OTP is: " + otp +
                "\nValid for 15 minutes.\n\nRegards,\nYour Company";
        sendEmail(email, subject, message);
    }

    public void sendResetOtp(String email, String otp) {
        String subject = "Your Password Reset OTP";
        String message = "Dear user,\n\nYour OTP is: " + otp +
                "\nValid for 15 minutes.\n\nIf not requested, please ignore.\n\nRegards,\nYour Company";
        sendEmail(email, subject, message);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            String fromEmail = config.getFromEmail();   // ✅ Correct way

            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);

            Mail mail = new Mail(
                    from,
                    subject,
                    toEmail,
                    new com.sendgrid.helpers.mail.objects.Content("text/plain", body)
            );

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid failed: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error sending SendGrid email", e);
        }
    }
}

