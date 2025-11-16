package pl.facility_rental.user.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.manager.mappers.ResourceMgrMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService;

    private final ClientMapper clientMapper;
    private final AdminMapper adminMapper;
    private final ResourceMgrMapper resourceManagerMapper;

    public UserController(UserService userService, ClientMapper clientMapper, AdminMapper adminMapper, ResourceMgrMapper resourceManagerMapper) {

        this.userService = userService;
        this.clientMapper = clientMapper;
        this.adminMapper = adminMapper;
        this.resourceManagerMapper = resourceManagerMapper;
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
    public ReturnedUserDto deleteUser(@RequestParam UUID id) throws Exception {
        return mapSubtypes(userService.delete(id));
    }

    private ReturnedUserDto mapSubtypes(User user) throws Exception {
        if(user instanceof Client) {
            return clientMapper.getClientDetails((Client) user);
        }
        if(user instanceof Administrator) {
            return adminMapper.getAdminDetails((Administrator) user);
        }
        if(user instanceof ResourceMgr) {
            return resourceManagerMapper.getManagerDetails((ResourceMgr) user);
        }
        throw new Exception("Error retrieving the user's type.");
    }




}
