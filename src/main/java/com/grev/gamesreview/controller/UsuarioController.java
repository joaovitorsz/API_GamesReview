package com.grev.gamesreview.controller;

import com.grev.gamesreview.DTO.AuthResponseDTO;
import com.grev.gamesreview.DTO.UsuarioUpdateDTO;
import com.grev.gamesreview.model.Usuario;
import com.grev.gamesreview.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponseDTO> meuPerfil(
            @AuthenticationPrincipal Usuario usuarioLogado){
        return ResponseEntity.ok(new AuthResponseDTO(
                null,
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getRole()
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<?> editarPerfil(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestBody UsuarioUpdateDTO dados){

        if (dados == null){
            return ResponseEntity.badRequest().body("Informe os dados para atualização.");
        }

        try {
            AuthResponseDTO resposta = usuarioService.editarPerfil(usuarioLogado, dados);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deletarConta(
            @AuthenticationPrincipal Usuario usuarioLogado) {

        String mensagem = usuarioService.deletarConta(usuarioLogado);
        return ResponseEntity.ok(mensagem);
    }

}
