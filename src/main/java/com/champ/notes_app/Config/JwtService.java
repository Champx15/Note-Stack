package com.champ.notes_app.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {

    private String secretKey;
    public JwtService(){
        this.secretKey=getTheSecretKey();
    }
    private String getTheSecretKey(){
        try{
            KeyGenerator key = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secret = key.generateKey();
            return Base64.getEncoder().encodeToString(secret.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error while generating key");
        }
    }
    public String generateToken(String name) {
        Map<String, Object> claims= new HashMap<>();
        return Jwts.builder().claims(claims).subject(name).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() +12_960_000_000L)).signWith(getKey(), SignatureAlgorithm.HS256 ).compact();
    }
    private Key getKey(){
        byte[] b= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(b);

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getKey()).build().parseSignedClaims(token).getPayload();
    }

    //for notes
    public String extractEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return extractUserName(authHeader.substring(7));
        }
        throw new RuntimeException("Missing or invalid token");
    }


}
