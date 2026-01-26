package pl.facility_rental.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.facility_rental.auth.config.CustomUserDetails;
import pl.facility_rental.auth.config.CustomUserDetailsService;
import pl.facility_rental.auth.dto.ChangePasswordDto;
import pl.facility_rental.auth.exceptions.InactiveUserLoginAttemptException;
import pl.facility_rental.auth.exceptions.InvalidCredentialsException;
import pl.facility_rental.auth.exceptions.PasswordsDontMatchException;
import pl.facility_rental.auth.exceptions.UserWithSuchLoginNotFoundException;
import pl.facility_rental.auth.jwt.JwtUtils;
import pl.facility_rental.auth.dto.RegistrationDto;
import pl.facility_rental.auth.dto.SuccessfulRegistrationDto;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils, PasswordEncoder passwordEncoder
                       ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) throws UserWithSuchLoginNotFoundException {
        Optional<User> user = userService.getUserByLoginStrict(username);
        System.out.println(passwordEncoder.encode("admin"));
        System.out.println(passwordEncoder.matches("admin", "$2a$12$c0VwB9.RSYqpi78kLALDLeROaxHKik1BcrG/0Ax6snMvNjdQkabPW"));

        if(user.isEmpty()){
            throw new UserWithSuchLoginNotFoundException("Nie istnieje użytkownik o takim loginie!");
        }
        if(!user.get().isActive()){
            throw  new InactiveUserLoginAttemptException("To konto jest nieaktywne. Skontaktuj się z administracją.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (BadCredentialsException e){
            e.printStackTrace();
            throw new InvalidCredentialsException("Niepoprawne dane logowania.");
        }
        return jwtUtils.generateToken(new CustomUserDetails(user.get()));
    }


    /**co prawdla moglo byc tak

     public void register(Client client)

        wiem ze to nie jest czyste architekturowo
     */
    public SuccessfulRegistrationDto register(RegistrationDto registrationDto) throws Exception {
        Client newClient = new Client(registrationDto.login(), registrationDto.email(),
               true,  passwordEncoder.encode(registrationDto.rawPassword()),registrationDto.firstName(),
                registrationDto.lastName(), registrationDto.phone() );
        var client =  userService.createUser(newClient);
        return SuccessfulRegistrationDto.builder().login(client.getLogin()).id(client.getId()).build();
    }


    public User changePassword(ChangePasswordDto changePasswordDto){
        var user = userService.getUserByLoginStrict(changePasswordDto.login());
        if(user.isEmpty()){
            throw new UserWithSuchLoginNotFoundException("Nie istnieje użytkownik o takim loginie!");
        }
        if(!passwordEncoder.matches(changePasswordDto.password(), user.get().getPassword())){
            throw new PasswordsDontMatchException("Podano niepoprawne hasło!");
        }
        return userService.updatePassword(changePasswordDto.login(), passwordEncoder.encode(changePasswordDto.newPassword()));
    }

}
