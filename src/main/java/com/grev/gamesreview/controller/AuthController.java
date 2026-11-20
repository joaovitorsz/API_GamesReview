package com.grev.gamesreview.controller;

import com.grev.gamesreview.DTO.AuthResponseDTO;
import com.grev.gamesreview.DTO.LoginRequestDTO;
import com.grev.gamesreview.DTO.RegistroRequestDTO;
import com.grev.gamesreview.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(
            @RequestBody RegistroRequestDTO dados) {

        if (dados == null || dados.nome() == null || dados.email() == null || dados.senha() == null) {
            return ResponseEntity.badRequest().body("Nome, email e senha são obrigatórios.");
        }

        try {
            AuthResponseDTO resposta = authService.registrar(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO dados) {

        if (dados == null || dados.email() == null || dados.senha() == null) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios.");
        }

        try {
            AuthResponseDTO resposta = authService.login(dados);
            return ResponseEntity.ok(resposta);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
        }
    }
}
