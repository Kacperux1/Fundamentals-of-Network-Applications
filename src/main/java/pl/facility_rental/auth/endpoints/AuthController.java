package pl.facility_rental.auth.endpoints;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.auth.dto.*;
import pl.facility_rental.auth.exceptions.InactiveUserLoginAttemptException;
import pl.facility_rental.auth.exceptions.PasswordsDontMatchException;
import pl.facility_rental.auth.exceptions.UnknownUserTypeException;
import pl.facility_rental.auth.exceptions.UserWithSuchLoginNotFoundException;
import pl.facility_rental.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT}
)
class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessfulRegistrationDto register(@RequestBody @Valid RegistrationDto  registrationDto) throws Exception {
        return authService.register(registrationDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDto login(@RequestBody @Valid LoginDto loginDto) throws UserWithSuchLoginNotFoundException {
        System.out.println("Login attempt: " + loginDto.login());
        return new SuccessfulLoginDto(authService.authenticate(loginDto.login(), loginDto.rawPassword()));
    }

    @PutMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto){
        authService.changePassword(changePasswordDto);
    }


}
