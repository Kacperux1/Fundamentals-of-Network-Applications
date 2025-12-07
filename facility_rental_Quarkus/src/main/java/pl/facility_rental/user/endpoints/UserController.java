package pl.facility_rental.user.endpoints;


import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.UpdateUserDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;
import pl.facility_rental.user.dto.manager.mappers.ResourceMgrMapper;
import pl.facility_rental.user.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserController {

    private final UserService userService;

    private final ClientMapper clientMapper;
    private final AdminMapper adminMapper;
    private final ResourceMgrMapper resourceManagerMapper;

    UserController(UserService userService, ClientMapper clientMapper, AdminMapper adminMapper, ResourceMgrMapper resourceManagerMapper) {

        this.userService = userService;
        this.clientMapper = clientMapper;
        this.adminMapper = adminMapper;
        this.resourceManagerMapper = resourceManagerMapper;
      }


    @POST
    public Response createUser(@Valid CreateUserDto createUserDto) throws Exception {
        var created =  mapSubtypes(userService.createUser(mapSubtypesToBusinessLayer(createUserDto)));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/clients")
    public List<ReturnedClientDto> getAllClients() {
        return userService.getAllClients().stream().map(clientMapper::getClientDetails).toList();
    }

    @GET
    public List<ReturnedUserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::mapSubtypes).toList();
    }

    @GET
    @Path("/{userId}")
    public ReturnedUserDto getUserById(@PathParam("userId") String userId) throws Exception {
        return userService.getUserById(userId).map(this::mapSubtypes).orElseThrow(() -> new UserNotFoundException(
                "User with given id was not found"));
    }

    @GET
    @Path("/login/{login}")
    public ReturnedUserDto getUserByLoginStrict(@PathParam("login") String login) throws Exception {
        return userService.getUserByLoginStrict(login).map(this::mapSubtypes).orElseThrow(() ->
                new UserNotFoundException("User with login " + login + " was not found"));
    }

    @GET
    @Path("/login_matching/{loginPart}")
    public List<ReturnedUserDto> getUserByLoginPart(@PathParam("loginPart") String loginPart) throws Exception {
        return userService.getUsersIfLoginMatchesValue(loginPart).stream().map(this::mapSubtypes).toList();
    }


    @PUT
    @Path("/{userId}")
    public ReturnedUserDto updateUser(@PathParam("userId") String userId, @Valid UpdateUserDto updatedUserDto) throws Exception {
        return mapSubtypes(userService.update(userId, mapUpdatedSubtypes(updatedUserDto)));
    }

    @PATCH
    @Path("/activate/{userId}")
    public ReturnedUserDto activateUser(@PathParam("userId") String userId) throws Exception {
        return mapSubtypes(userService.activate(userId));
    }

    @PATCH
    @Path("/deactivate/{userId}")
    public ReturnedUserDto deactivateUser(@PathParam("userId") String userId) throws Exception {
        return mapSubtypes(userService.deactivate(userId));
    }


//    @DeleteMapping
//    @ResponseStatus(HttpStatus.OK)
//    public ReturnedUserDto deleteUser(@RequestParam String id) throws Exception {
//        return mapSubtypes(userService.delete(id));
//    }

    private ReturnedUserDto mapSubtypes(User user) throws RecognizingUserTypeException {
        if (user instanceof Client) {
            return clientMapper.getClientDetails((Client) user);
        }
        if (user instanceof Administrator) {
            return adminMapper.getAdminDetails((Administrator) user);
        }
        if (user instanceof ResourceMgr) {
            return resourceManagerMapper.getManagerDetails((ResourceMgr) user);
        }
        throw new RecognizingUserTypeException("Error retrieving the user's type.");
    }

    private User mapSubtypesToBusinessLayer(CreateUserDto createUserDto) throws RecognizingUserTypeException {
        if (createUserDto instanceof CreateClientDto) {
            return clientMapper.createClientRequest((CreateClientDto) createUserDto);
        }
        if (createUserDto instanceof CreateResourceMgrDto) {
            return resourceManagerMapper.createManagerRequest((CreateResourceMgrDto) createUserDto);
        }
        if (createUserDto instanceof CreateAdminDto) {
            return adminMapper.createAdminRequest((CreateAdminDto) createUserDto);
        }
        throw new RecognizingUserTypeException("Error retrieving the user's type.");

    }

    private User mapUpdatedSubtypes(UpdateUserDto updateUserDto) throws RecognizingUserTypeException {
        if (updateUserDto instanceof UpdateClientDto) {
            return clientMapper.updateClient((UpdateClientDto) updateUserDto);
        }
        if (updateUserDto instanceof UpdateAdminDto) {
            return adminMapper.updateAdmin((UpdateAdminDto) updateUserDto);
        }
        if (updateUserDto instanceof UpdateResourceMgrDto) {
            return resourceManagerMapper.updateManager((UpdateResourceMgrDto) updateUserDto);
        }
        throw new RecognizingUserTypeException("Error retrieving the user's type.");
    }




}
