package pl.facility_rental.user.mvc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.user.dto.ReturnedUserDto;

import java.util.List;

@Service
public class UserRestClient {
    private final WebClient webClient;

    public UserRestClient(@Qualifier("userWebClient") WebClient userWebClient) {
        this.webClient = userWebClient;
    }

    public List<ReturnedUserDto> getAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(ReturnedUserDto.class)
                .collectList()
                .block();
    }

    public void deleteById(String id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ReturnedUserDto addFacility(CreateFacilityDto facility){
        return webClient.post()
                .bodyValue(facility)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }
}
