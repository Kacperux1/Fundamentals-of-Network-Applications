package pl.facility_rental.user.endpoints;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.mappers.ResourceMgrMapper;

import java.util.List;
import java.util.Optional;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedUserDto createUser(@RequestBody CreateUserDto createUserDto) throws Exception {
        return mapSubtypes(userService.createUser(mapSubtypesToBusinessLayer(createUserDto)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto getUser(@RequestParam UUID id) throws Exception {
        return mapSubtypes(userService.getUserById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User not found")));

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto getUserByLogin(@RequestParam String login) throws Exception {
        return mapSubtypes(userService.findByLoginStrict(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User with login " + login + " not found")));

    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto updateUser(@RequestParam UUID clientId,
                                      @RequestBody CreateUserDto updatedUserDto) throws Exception {
        return mapSubtypes(userService.update(clientId, mapSubtypesToBusinessLayer(updatedUserDto)));
    }
    @SneakyThrows
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedUserDto> getUsersIfLoginContainsValue(@RequestParam String value) throws Exception {
        return userService.findIfLoginContainsValue(value).stream().map(this::mapSubtypes).toList();
    }

    @GetMapping("/clients")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedClientDto> getAllClients() {
        return userService.getAllClients().stream().map(clientMapper::getClientDetails).toList();
    }

//    @DeleteMapping
//    @ResponseStatus(HttpStatus.OK)
//    public ReturnedUserDto deleteUser(@RequestParam UUID id) throws Exception {
//        return mapSubtypes(userService.delete(id));
//    }

    @PatchMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto activateUser(@RequestParam UUID clientId) throws Exception {
        return mapSubtypes(userService.activate(clientId));
    }
    @PatchMapping("/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto deactivateUser(@RequestParam UUID clientId) throws Exception {
        return mapSubtypes(userService.deactivate(clientId));
    }
    private ReturnedUserDto mapSubtypes(User user) throws Exception {
        if (user instanceof Client) {
            return clientMapper.getClientDetails((Client) user);
        } if (user instanceof Administrator) {
            return adminMapper.getAdminDetails((Administrator) user);
        } if (user instanceof ResourceMgr) {
            return resourceManagerMapper.getManagerDetails((ResourceMgr) user);
        }
        throw new Exception("Error retrieving the user's type.");
    }

    private User mapSubtypesToBusinessLayer(CreateUserDto createUserDto) throws Exception {
        if (createUserDto instanceof CreateClientDto) {
            return clientMapper.createClientRequest((CreateClientDto) createUserDto);
        } if (createUserDto instanceof CreateResourceMgrDto) {
            return resourceManagerMapper.createManagerRequest((CreateResourceMgrDto) createUserDto);
        } if (createUserDto instanceof CreateAdminDto) {
            return adminMapper.createAdminRequest((CreateAdminDto) createUserDto);
        }
        throw new Exception("Error recognizing the new user's type.");

    }


}
