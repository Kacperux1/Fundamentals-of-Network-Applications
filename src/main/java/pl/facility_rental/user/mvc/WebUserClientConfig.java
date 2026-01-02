package pl.facility_rental.user.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebUserClientConfig {
    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/users")
                .build();
    }
}
