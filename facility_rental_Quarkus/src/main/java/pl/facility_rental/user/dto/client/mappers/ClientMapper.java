package pl.facility_rental.user.dto.client.mappers;


import jakarta.enterprise.context.ApplicationScoped;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;

@ApplicationScoped
public class ClientMapper {

    public Client createClientRequest(CreateClientDto createClientDto) {
        return new Client(createClientDto.getLogin(), createClientDto.getEmail(), createClientDto.isActive(),
                createClientDto.getFirstName(), createClientDto.getLastName(), createClientDto.getPhone());
    }

    public ReturnedClientDto getClientDetails(Client client) {
        return new ReturnedClientDto(client.getId(), client.getLogin(), client.getEmail(),
                client.isActive(), client.getFirstName(), client.getLastName(), client.getPhone());
    }

    public Client updateClient(UpdateClientDto updateClientDto) {
        return new Client( updateClientDto.getLogin(), updateClientDto.getEmail(), false, updateClientDto.getFirstName(),
                updateClientDto.getLastName(), updateClientDto.getPhone() );
    }


}
