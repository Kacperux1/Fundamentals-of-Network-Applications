package pl.facility_rental.auth.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.facility_rental.user.business.UserService;


@Service("custom")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;


    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("Ładuję użytkownika: " + username);
        var user = new CustomUserDetails(userService.getUserByLoginStrict(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + " not found!")));
        System.out.println("Hasło z bazy: " + user.getPassword());
        return user;
    }
}
