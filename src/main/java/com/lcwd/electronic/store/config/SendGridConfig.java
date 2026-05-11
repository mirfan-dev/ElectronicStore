package com.lcwd.electronic.store.config;




import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sendgrid")
public class SendGridConfig {
    private String apiKey;
    private String fromEmail;
}
