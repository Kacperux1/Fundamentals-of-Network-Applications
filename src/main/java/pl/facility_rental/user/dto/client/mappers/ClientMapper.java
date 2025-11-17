package pl.facility_rental.user.dto.client.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;

@Component
public class ClientMapper {

    public Client createClientRequest(CreateClientDto createClientDto) {

        String login = createClientDto.getLogin();
        String email = createClientDto.getEmail();
        boolean active = createClientDto.isActive();
        String firstName = createClientDto.getFirstName();
        String lastName = createClientDto.getLastName();
        String phone = createClientDto.getPhone();

        if(login.isBlank()){
            throw new IllegalArgumentException("login nie może być pusty");
        }

        if(email.isBlank()){
            throw new IllegalArgumentException("email nie może być pusty");
        }

        if(firstName.isBlank()){
            throw new IllegalArgumentException("Imię nie może być pusty");
        }

        if(lastName.isBlank()){
            throw new IllegalArgumentException("Nazwisko nie może być pusty");
        }

        if(phone.isBlank()){
            throw new IllegalArgumentException("numer telefonu nie może być pusty");
        }

        if(!phone.matches("\\d{3} \\d{3} \\d{3}")){
            throw new IllegalArgumentException("numer telefonu musi być w formacie 000 000 000");
        }


        return new Client(createClientDto.getLogin(), createClientDto.getEmail(), createClientDto.isActive(),
                createClientDto.getFirstName(), createClientDto.getLastName(), createClientDto.getPhone());
    }

    public ReturnedClientDto getClientDetails(Client client) {
        return new ReturnedClientDto(client.getId(), client.getLogin(), client.getEmail(),
                client.isActive(), client.getFirstName(), client.getLastName(), client.getPhone());
    }


}
