package pl.facility_rental.user.jws;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwsUtil {

    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKey;

    public JwsUtil() {

    }


    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJws(String userId, String login) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("login", login)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String parseId(String jws) {
        var claims =  Jwts.parser()
                .verifyWith(secretKey)
                .build().parseSignedClaims(jws);
        return claims.getPayload().get("userId").toString();

    }



}


