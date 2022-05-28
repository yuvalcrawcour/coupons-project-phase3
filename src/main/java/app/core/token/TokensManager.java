package app.core.token;

import app.core.login.ClientType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokensManager {

    private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
    @Value("${jwt.util.secret.key}")
    private String encodedSecretKey;
    private Key key;
    // expiration time
    @Value("${jwt.util.chrono.unit.number}")
    private int unitsNumber;
    @Value("${jwt.util.chrono.unit}")
    private String chronoUnit;
    //

    @PostConstruct
    public void init() {
        // create the key using the secret key (decoded to base-64) and the chosen
        // algorithm
        this.key = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), signatureAlgorithm);
    }
    public String generateAdminToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientType", ClientType.ADMIN);
        claims.put("clientId", 1111);
        return createToken(claims, email);
    }
    public String generateToken(ClientDetails clientDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientType", clientDetails.getClientType());
        claims.put("clientId", clientDetails.getClientId());
        return createToken(claims, clientDetails.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Instant now = Instant.now();
        Instant expiration = now.plus(unitsNumber, ChronoUnit.valueOf(this.chronoUnit));
        return Jwts.builder()

                .setClaims(claims)

                .setSubject(subject)

                .setIssuedAt(Date.from(now))

                .setExpiration(Date.from(expiration))

                .signWith(key)

                .compact();
    }

    public String extractSubject(String token) {
        return exctractAllClaims(token).getSubject();
    }

    // add a method to extract the token expiration date
    public Date extractExpiration(String token) {
        return exctractAllClaims(token).getExpiration();
    }

    // add a method to extract the token issue date
    public Date extractIssuedAt(String token) {
        return exctractAllClaims(token).getIssuedAt();
    }

    // add method that can check for us if the token is expired or not
    public boolean isTokenExpired(String token) {
        try {
            exctractAllClaims(token);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public ClientDetails extractClient(String token) {
        Claims claims = exctractAllClaims(token);
        int clientId = claims.get("clientId", Integer.class);
        String clientEmail = claims.getSubject();
        ClientType clientType = ClientType.valueOf(claims.get("clientType", String.class));
        ClientDetails clientDetails = new ClientDetails(clientId, clientEmail, clientType);
        return clientDetails;
    }

    private Claims exctractAllClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        return jwtParser.parseClaimsJws(token).getBody();
    }



    @Data
    @AllArgsConstructor
    public static class ClientDetails {
        public int clientId;
        public String email;
        public ClientType clientType;


    }

}

