package pl.facility_rental.user.endpoints;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.UpdateUserDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;
import pl.facility_rental.user.dto.manager.mappers.ResourceMgrMapper;
import pl.facility_rental.user.exceptions.*;
import pl.facility_rental.user.jws.JwsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ClientMapper clientMapper;
    private final AdminMapper adminMapper;
    private final ResourceMgrMapper resourceManagerMapper;

    private final JwsUtil jwsUtil;

    UserController(UserService userService, ClientMapper clientMapper, AdminMapper adminMapper, ResourceMgrMapper resourceManagerMapper, JwsUtil jwsUtil) {

        this.userService = userService;
        this.clientMapper = clientMapper;
        this.adminMapper = adminMapper;
        this.resourceManagerMapper = resourceManagerMapper;
        this.jwsUtil = jwsUtil;
    }


    @PutMapping("/self")
    @PreAuthorize("hasRole('Client')")
    public ResponseEntity<ReturnedUserDto> updateSelf(
            @RequestBody UpdateClientDto updatedUserDto,
            @RequestHeader("If-Match") String etag
    ) {

        String login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userService.getUserByLoginStrict(login)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Zalogowany użytkownik nie istnieje"
                ));

        totalEtagCheck(user.getId(), etag);

        User updated = userService.update(user.getId(),
                clientMapper.updateClient(updatedUserDto));

        return ResponseEntity.ok(mapSubtypes(updated));
    }


    @PostMapping
    @PreAuthorize("hasRole('Administrator')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedUserDto createUser(@RequestBody CreateUserDto createUserDto) throws Exception {
        return mapSubtypes(userService.createUser(mapSubtypesToBusinessLayer(createUserDto)));
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedClientDto> getAllClients() {
        return userService.getAllClients().stream().map(clientMapper::getClientDetails).toList();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    public List<ReturnedUserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::mapSubtypes).toList();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    public ResponseEntity<ReturnedUserDto> getUserById(@PathVariable String userId)  {

        var user  = userService.getUserById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with given id was not found"));
        return ResponseEntity.ok()
                .eTag(jwsUtil.generateJws(user.getId(), user.getLogin()))
                .body(mapSubtypes(user));
    }

    @GetMapping("/login/{login}")
    public ReturnedUserDto getUserByLoginStrict(@PathVariable String login) {
        return userService.getUserByLoginStrict(login).map(this::mapSubtypes).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with login " + login + " was not found"));
    }

    @GetMapping("/login_matching/{loginPart}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    public List<ReturnedUserDto> getUserByLoginPart(@PathVariable String loginPart) {
        return userService.getUsersIfLoginMatchesValue(loginPart).stream().map(this::mapSubtypes).toList();
    }


    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('Administrator')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReturnedUserDto> updateUser(@PathVariable String userId, @RequestBody UpdateUserDto updatedUserDto,
                                      @RequestHeader("If-Match") String etag)  throws ResponseStatusException{
        totalEtagCheck(userId, etag);

        return ResponseEntity.ok()
                .body(mapSubtypes(userService.update(userId, mapUpdatedSubtypes(updatedUserDto))));
    }

    @PutMapping("/activate/{userId}")
    @PreAuthorize("hasRole('Administrator')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReturnedUserDto> activateUser(@PathVariable String userId,
                                                        @RequestHeader("If-Match") String etag) throws ResponseStatusException{
        totalEtagCheck(userId, etag);

        return ResponseEntity.ok()
                .body(mapSubtypes(userService.activate(userId)));
    }

    @PutMapping("/deactivate/{userId}")
    @PreAuthorize("hasRole('Administrator')")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReturnedUserDto> deactivateUser(@PathVariable String userId,
                                                          @RequestHeader("If-Match") String etag) throws ResponseStatusException{

        totalEtagCheck(userId, etag);

        return ResponseEntity.ok()
                .body(mapSubtypes(userService.deactivate(userId)));
    }

    private void totalEtagCheck(@PathVariable String userId, @RequestHeader("If-Match") String etag) {
        if(etag == null || etag.isEmpty()) {
            System.out.println("etag is null");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, "ETag is pusty");

        }
        if(!checkEtag(etag, userId)) {
            System.out.println("etag is null");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "ETag is not poprawny");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Administrator')")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedUserDto deleteUser(@PathVariable String id,
                                      @RequestHeader("If-Match") String etag) throws ResponseStatusException{
        totalEtagCheck(id, etag);

        return mapSubtypes(userService.delete(id));
    }

    private boolean checkEtag(String etag, String urlUserId) {
        String tokenUrl ="";
        try {
            tokenUrl = jwsUtil.parseId(etag);
            if(!tokenUrl.equals(urlUserId)) {
                return false;
            }

        }  catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

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

    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(ValidationViolationUserException.class)
        public ResponseEntity<Map<String, String>> handleValidationViolation(ValidationViolationUserException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }


        @ExceptionHandler(DeletingActiveUserException.class)
        public ResponseEntity<Map<String, String>> handleDeletingActiveUser(DeletingActiveUserException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        @ExceptionHandler(RecognizingUserTypeException.class)
        public ResponseEntity<Map<String, String>> handleRecognizingUserType(RecognizingUserTypeException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        @ExceptionHandler(UserDBException.class)
        public ResponseEntity<Map<String, String>> handleUserDB(UserDBException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
        @ExceptionHandler(BadIdFormatException.class)
        public ResponseEntity<Map<String, String>> handleBadIdFormatException(BadIdFormatException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        @ExceptionHandler(UserWithLoginExistsException.class)
        public ResponseEntity<Map<String, String>> handleUserWithLoginExists(UserWithLoginExistsException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }



    }



}
