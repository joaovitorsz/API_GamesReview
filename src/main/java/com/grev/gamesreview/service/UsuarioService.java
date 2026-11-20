package com.grev.gamesreview.service;

import com.grev.gamesreview.DTO.AuthResponseDTO;
import com.grev.gamesreview.DTO.UsuarioUpdateDTO;
import com.grev.gamesreview.model.Usuario;
import com.grev.gamesreview.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO editarPerfil(Usuario usuarioLogado, UsuarioUpdateDTO dados){
        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado."));

        if (dados.nome() != null && !dados.nome().isBlank()){
            usuario.setNome(dados.nome());
        }

        if(dados.email() != null && !dados.email().isBlank() && !dados.email().equalsIgnoreCase(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dados.email())){
                throw new IllegalArgumentException("Este email já está em uso por outra conta.");
            }
            usuario.setEmail(dados.email());
        }

        if (dados.senha() != null && !dados.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        String novoToken = jwtService.gerarToken(usuarioAtualizado);

        return new AuthResponseDTO(
                novoToken,
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getRole()
        );

    }

    public String deletarConta(Usuario usuarioLogado){
        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        String nome = usuario.getNome();
        usuarioRepository.delete(usuario);

        return "Olá, " + nome + "! Sua conta foi excluída com sucesso.";
    }

}
