package com.grev.gamesreview.DTO;

import com.grev.gamesreview.model.Role;

public record RegistroRequestDTO(
        String nome,
        String email,
        String senha,
        Role role
){}
