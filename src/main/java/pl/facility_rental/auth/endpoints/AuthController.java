package pl.facility_rental.auth.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.auth.dto.*;
import pl.facility_rental.auth.exceptions.UserWithSuchLoginNotFoundException;
import pl.facility_rental.auth.service.AuthService;

@RestController
@RequestMapping("/")
class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessfulRegistrationDto register(@RequestBody RegistrationDto  registrationDto) throws Exception {
        return authService.register(registrationDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessfulLoginDto login(@RequestBody LoginDto loginDto) throws UserWithSuchLoginNotFoundException {
        return new SuccessfulLoginDto(authService.authenticate(loginDto.login(), loginDto.rawPassword()));
    }

    @PutMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        authService.changePassword(changePasswordDto);
    }
}
