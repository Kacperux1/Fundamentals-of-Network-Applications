package pl.facility_rental.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.facility_rental.auth.config.CustomUserDetailsService;
import pl.facility_rental.auth.exceptions.EditedTokenException;
import pl.facility_rental.auth.exceptions.ExpiredTokenException;
import org.springframework.http.HttpStatus;
import java.io.IOException;

@Component("jwt")
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;


    public JwtTokenFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException, ExpiredTokenException, EditedTokenException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authorization header: " + authorizationHeader);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authorizationHeader.substring(7);

       if (SecurityContextHolder.getContext().getAuthentication() == null) {
           try {
               if (jwtUtils.validateToken(jwt)) {
                   String username = jwtUtils.getUsernameFromToken(jwt);
                   UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

                   UsernamePasswordAuthenticationToken authentication =
                           new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                   authentication.setDetails(new WebAuthenticationDetails(request));

                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           } catch (EditedTokenException | ExpiredTokenException e) {
               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
               response.getWriter().write(e.getMessage());
               return;
           }
       }


        filterChain.doFilter(request, response);
    }
}
