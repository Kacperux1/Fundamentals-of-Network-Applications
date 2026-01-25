package pl.facility_rental.user.dto.client.mappers;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.UpdateUserDto;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.exceptions.ValidationViolationUserException;

@Component
public class ClientMapper {

    private final PasswordEncoder passwordEncoder;

    public ClientMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Client createClientRequest(CreateClientDto createClientDto) {
        return new Client(createClientDto.getLogin(), createClientDto.getEmail(),
                createClientDto.isActive(),
                passwordEncoder.encode(createClientDto.getPassword()),
                createClientDto.getFirstName(),
                createClientDto.getLastName(),
                createClientDto.getPhone());
    }

    public ReturnedClientDto getClientDetails(Client client) {
        return new ReturnedClientDto(client.getId(), client.getLogin(), client.getEmail(),
                client.isActive(), client.getFirstName(), client.getLastName(), client.getPhone());
    }

    public Client updateClient(UpdateClientDto updateClientDto) {
        return new Client(updateClientDto.getLogin(), updateClientDto.getEmail(), false, "",updateClientDto.getFirstName(),
                updateClientDto.getLastName(), updateClientDto.getPhone());
    }


}
