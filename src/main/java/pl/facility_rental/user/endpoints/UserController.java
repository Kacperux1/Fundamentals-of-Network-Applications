package pl.facility_rental.user.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.business.model.Client;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService;

    private final ClientMapper clientMapper;

    public UserController(UserService userService,  ClientMapper clientMapper) {

        this.userService = userService;
        this.clientMapper = clientMapper;
    }


    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedClientDto createClient(@RequestBody CreateClientDto createClientDto)  {
        try {
            return clientMapper.getClientDetails((Client) userService.createUser(clientMapper.createClientRequest(createClientDto)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/clients")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedClientDto> getAllClients() {
        return userService.getAllUsers().stream().filter(user -> user instanceof Client)
                .map(u -> clientMapper.getClientDetails((Client) u))
                .toList();

    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ReturnedClientDto deleteClient(@RequestParam UUID id) {
        return user
    }
}
