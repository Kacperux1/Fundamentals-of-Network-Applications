package pl.facility_rental.rent.mvc;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.ReturnedRentDto;

import java.util.List;

@Service
public class RentRestClient {

    private final WebClient webClient;

    public RentRestClient(@Qualifier("rentWebClient")WebClient webClient) {
        this.webClient = webClient;
    }

    List<ReturnedRentDto> getAll() {
        return webClient.get().accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(ReturnedRentDto.class).collectList().block();
    }

    public ReturnedRentDto addRent(CreateRentDto createRentDto) {
        return webClient.post().bodyValue(createRentDto).retrieve().bodyToMono(ReturnedRentDto.class).block();
    }

    public ReturnedRentDto endRent( String rentId) {
        System.out.println("dupa4");
        return webClient.put().uri("/" +rentId)
                .retrieve().bodyToMono(ReturnedRentDto.class).block();
    }

}
