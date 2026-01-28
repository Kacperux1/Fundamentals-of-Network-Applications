package pl.facility_rental.facility.mvc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;

import java.util.List;

@Service
public class FacilityRestClient {
    private final WebClient webClient;

    public FacilityRestClient(@Qualifier("facilityWebClient") WebClient facilityWebClient) {
        this.webClient = facilityWebClient;
    }

    public List<ReturnedFacilityDto> getAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(ReturnedFacilityDto.class)
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

    public ReturnedFacilityDto addFacility(CreateFacilityDto facility){
        return webClient.post()
                .bodyValue(facility)
                .retrieve()
                .bodyToMono(ReturnedFacilityDto.class)
                .block();
    }

    public ReturnedFacilityDto getById(String id) {
        return webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ReturnedFacilityDto.class)
                .block();
    }

    public ReturnedFacilityDto updateFacility(String id, UpdateFacilityDto dto) {
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ReturnedFacilityDto.class)
                .block();
    }
}
