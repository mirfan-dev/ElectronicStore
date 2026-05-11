package com.lcwd.electronic.store.config;



import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email:mirfan916152@gmail.com}")
    private String fromEmail;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(apiKey);
    }

    public String getFromEmail() {
        return fromEmail;
    }
}
