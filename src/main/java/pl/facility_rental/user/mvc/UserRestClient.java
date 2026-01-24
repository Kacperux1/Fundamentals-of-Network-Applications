package pl.facility_rental.user.mvc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.UpdateUserDto;

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

    public ReturnedUserDto getUserById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }

    public void deleteById(String id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ReturnedUserDto addUser(CreateUserDto user){
        return webClient.post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }

    public ReturnedUserDto editUser(UpdateUserDto user, String id){
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }

    public ReturnedUserDto activate(String id) {
        return webClient.patch()
                .uri("/activate/{id}", id)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }

    public ReturnedUserDto deactivate(String id) {
        return webClient.patch()
                .uri("/deactivate/{id}", id)
                .retrieve()
                .bodyToMono(ReturnedUserDto.class)
                .block();
    }
}
