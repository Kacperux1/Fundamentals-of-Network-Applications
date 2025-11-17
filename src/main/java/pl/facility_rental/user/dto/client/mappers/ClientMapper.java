package pl.facility_rental.user.dto.client.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.UpdateUserDto;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.exceptions.ValidationViolationUserException;

@Component
public class ClientMapper {

    public Client createClientRequest(CreateClientDto createClientDto) {

        String login = createClientDto.getLogin();
        String email = createClientDto.getEmail();
        Boolean active = createClientDto.isActive();
        String firstName = createClientDto.getFirstName();
        String lastName = createClientDto.getLastName();
        String phone = createClientDto.getPhone();

        if(login.isBlank()){
            throw new ValidationViolationUserException("validation failed: login nie może być pusty");
        }

        if(email.isBlank()){
            throw new ValidationViolationUserException("validation failed:email nie może być pusty");
        }

        if(firstName.isBlank()){
            throw new ValidationViolationUserException("validation failed:Imię nie może być pusty");
        }

        if(lastName.isBlank()){
            throw new ValidationViolationUserException("validation failed:Nazwisko nie może być pusty");
        }

        if(phone.isBlank()){
            throw new ValidationViolationUserException("validation failed:numer telefonu nie może być pusty");
        }

        if(!phone.matches("\\d{3} \\d{3} \\d{3}")){
            throw new ValidationViolationUserException("validation failed:numer telefonu musi być w formacie 000 000 000");
        }



        return new Client(createClientDto.getLogin(), createClientDto.getEmail(), createClientDto.isActive(),
                createClientDto.getFirstName(), createClientDto.getLastName(), createClientDto.getPhone());
    }

    public ReturnedClientDto getClientDetails(Client client) {
        return new ReturnedClientDto(client.getId(), client.getLogin(), client.getEmail(),
                client.isActive(), client.getFirstName(), client.getLastName(), client.getPhone());
    }

    public Client updateClient(UpdateClientDto updateClientDto) {

        if(!updateClientDto.getEmail().isBlank() && !updateClientDto.getEmail()
                .matches("^[\\w\\.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")){
            throw new ValidationViolationUserException("validation failed:email dont fit in correct template");
        }
        if(!updateClientDto.getPhone().matches("\\d{3} \\d{3} \\d{3}")){
            throw new ValidationViolationUserException("validation failed:numer telefonu musi być w formacie 000 000 000");
        }
        return new Client( updateClientDto.getLogin(), updateClientDto.getEmail(), false, updateClientDto.getFirstName(),
                updateClientDto.getLastName(), updateClientDto.getPhone() );
    }


}
