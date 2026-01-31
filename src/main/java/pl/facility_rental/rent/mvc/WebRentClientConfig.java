package pl.facility_rental.rent.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebRentClientConfig {

    @Bean
    public WebClient rentWebClient() {
        WebClient.Builder builder = WebClient.builder();
        builder.baseUrl("http://localhost:8080/rents");
        return builder.build();
    }
}
