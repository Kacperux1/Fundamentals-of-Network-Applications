package pl.facility_rental.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.facility_rental.auth.exceptions.EditedTokenException;
import pl.facility_rental.auth.exceptions.ExpiredTokenException;


import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtUtils {

    @Value ("${jwt.secret}")

    private String secret;
    @Value("${jwt.expiration}")
    private int jwtExpirationPeriod;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationPeriod)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }


    public boolean validateToken(String token) throws EditedTokenException {
        try {
            Jwts.parser().verifyWith(secretKey).build()
                    .parse(token);
        } catch(MalformedJwtException | SignatureException me) {
            me.printStackTrace();
            throw new EditedTokenException("Treść żetonu logowania została naruszona i nie może zstać uznana za poprawną."
                    + "\nZaloguj się ponownie.");
        }  catch (ExpiredJwtException ee) {
            ee.printStackTrace();
            throw new ExpiredTokenException("Sesja logowania wygasła. Zaloguj się ponownie.");
        }
        return true;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

}

