package pl.facility_rental.user.dto;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.model.Client;

@Component
public class ClientMapper {

    public Client createClientRequest(CreateClientDto createClientDto) {
        return new Client(createClientDto.getLogin(), createClientDto.getEmail(), createClientDto.isActive(),
                createClientDto.getFirstName(), createClientDto.getLastName(), createClientDto.getPhone());
    }

    public ReturnedClientDto getClientDetails(Client client) {
        return new ReturnedClientDto(client.getId(), client.getLogin(), client.getEmail(),
                client.isActive(), client.getFirstName(), client.getLastName(), client.getPhone());
    }


}
