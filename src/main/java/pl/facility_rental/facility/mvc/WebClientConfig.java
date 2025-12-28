package pl.facility_rental.facility.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient facilityWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/facilities")
                .build();
    }
}
