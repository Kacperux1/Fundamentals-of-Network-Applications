package pl.facility_rental.user.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.dto.ClientMapper;
import pl.facility_rental.user.dto.CreateClientDto;
import pl.facility_rental.user.dto.ReturnedClientDto;
import pl.facility_rental.user.model.Client;

import java.util.List;

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
}
