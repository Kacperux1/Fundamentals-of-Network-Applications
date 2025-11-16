package pl.facility_rental.user.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.mappers.ClientDataMapper;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
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
    private final ClientDataMapper clientDataMapper;

    UserController(UserService userService, ClientMapper clientMapper, AdminMapper adminMapper, ResourceMgrMapper resourceManagerMapper, ClientDataMapper clientDataMapper) {

        this.userService = userService;
        this.clientMapper = clientMapper;
        this.adminMapper = adminMapper;
        this.resourceManagerMapper = resourceManagerMapper;
        this.clientDataMapper = clientDataMapper;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedUserDto createUser(@RequestBody CreateClientDto createUserDto) throws Exception {
        return mapSubtypes(userService.createUser(mapSubtypesToBusinessLayer(createUserDto)));
    }

    @GetMapping("/clients")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedClientDto> getAllClients() {
        return userService.getAllClients().stream().map(clientMapper::getClientDetails).toList();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedUserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::mapSubtypes).toList();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto updateUser(@PathVariable String userId, @RequestBody CreateUserDto updatedUserDto) throws Exception {
        return mapSubtypes(userService.update(userId, mapSubtypesToBusinessLayer(updatedUserDto)));
    }

    @PatchMapping("/activate/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto activateUser(@PathVariable String userId) throws Exception {
        return mapSubtypes(userService.activate(userId));
    }

    @PatchMapping("/deactivate/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto deactivateUser(@PathVariable String userId) throws Exception {
        return mapSubtypes(userService.deactivate(userId));
    }


//    @DeleteMapping
//    @ResponseStatus(HttpStatus.OK)
//    public ReturnedUserDto deleteUser(@RequestParam String id) throws Exception {
//        return mapSubtypes(userService.delete(id));
//    }

    private ReturnedUserDto mapSubtypes(User user) throws RuntimeException {
        if (user instanceof Client) {
            return clientMapper.getClientDetails((Client) user);
        }
        if (user instanceof Administrator) {
            return adminMapper.getAdminDetails((Administrator) user);
        }
        if (user instanceof ResourceMgr) {
            return resourceManagerMapper.getManagerDetails((ResourceMgr) user);
        }
        throw new RuntimeException("Error retrieving the user's type.");
    }

    private User mapSubtypesToBusinessLayer(CreateUserDto createUserDto) throws Exception {
        if (createUserDto instanceof CreateClientDto) {
            return clientMapper.createClientRequest((CreateClientDto) createUserDto);
        }
        if (createUserDto instanceof CreateResourceMgrDto) {
            return resourceManagerMapper.createManagerRequest((CreateResourceMgrDto) createUserDto);
        }
        if (createUserDto instanceof CreateAdminDto) {
            return adminMapper.createAdminRequest((CreateAdminDto) createUserDto);
        }
        throw new Exception("Error retrieving the user's type.");

    }


}
