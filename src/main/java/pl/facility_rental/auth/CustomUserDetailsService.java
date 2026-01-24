package pl.facility_rental.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.data.UserRepository;


@Service("custom")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;


    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new CustomUserDetails(userService.getUserByLoginStrict(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + " not found!")));
    }
}
