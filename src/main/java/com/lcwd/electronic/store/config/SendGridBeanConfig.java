package com.lcwd.electronic.store.config;



import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridBeanConfig {
    public SendGridBeanConfig() {
    }

    @Bean
    public SendGrid sendGrid(SendGridConfig config) {
        return new SendGrid(config.getApiKey());
    }
}
