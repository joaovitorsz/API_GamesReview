package com.grev.gamesreview.service;

import com.grev.gamesreview.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String gerarToken(Usuario usuario){
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("role", usuario.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairTodosClaims(token).getSubject();
    }

    public boolean tokenValido(String token, UserDetails userDetails){
        String email = extrairEmail(token);
        return email.equals(userDetails.getUsername()) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token){
        return extrairTodosClaims(token).getExpiration().before(new Date());
    }

    private Claims extrairTodosClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey(){
       return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) ;
    }

}
