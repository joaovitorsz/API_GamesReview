package com.grev.gamesreview.service;

import com.grev.gamesreview.DTO.AuthResponseDTO;
import com.grev.gamesreview.DTO.LoginRequestDTO;
import com.grev.gamesreview.DTO.RegistroRequestDTO;
import com.grev.gamesreview.model.Role;
import com.grev.gamesreview.model.Usuario;
import com.grev.gamesreview.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO registrar(RegistroRequestDTO dados) {
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        Role roleDefinido = (dados.role() != null) ? dados.role() : Role.USER;

        Usuario novoUsuario = new Usuario(
                dados.nome(),
                dados.email(),
                passwordEncoder.encode(dados.senha()),
                roleDefinido
        );

        usuarioRepository.save(novoUsuario);
        String token = jwtService.gerarToken(novoUsuario);

        return new AuthResponseDTO(
                token,
                novoUsuario.getNome(),
                novoUsuario.getEmail(),
                novoUsuario.getRole()
        );
    }

        public AuthResponseDTO login(LoginRequestDTO dados){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dados.email(), dados.senha())
            );

            Usuario usuario = usuarioRepository.findByEmail(dados.email())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

            String token = jwtService.gerarToken(usuario);

            return new AuthResponseDTO(
                    token,
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole()
            );
        }
    }


