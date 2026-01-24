package pl.facility_rental.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.User;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationPeriod;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getLogin())
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationPeriod)))
                .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {

        Date tokenExpirationDate = extractExpirationDate(token);
        if(tokenExpirationDate.before(Date.from(Instant.now()))) {
            return false;
        }
        String[] rawToken = token.split(Pattern.quote("."));
        return verificationSignature(rawToken[1]).equals(rawToken[2]);
    }



    private Map<String, Object> extractClaims(String token) {
        return Jwts.parser()
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private Date extractExpirationDate(String token) {

         return  Jwts.parser()
                .build()
                .parseSignedClaims(token).getPayload().getExpiration();
    }

    public String getUsernameFromToken(String token) {

        return Jwts.parser().build().parseSignedClaims(token).getPayload().getSubject();
    }
    private String verificationSignature(String rawPayload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            return new String(mac.doFinal(rawPayload.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }



}
