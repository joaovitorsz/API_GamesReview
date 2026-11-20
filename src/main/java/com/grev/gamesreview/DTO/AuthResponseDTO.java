package com.grev.gamesreview.DTO;

import com.grev.gamesreview.model.Role;

public record AuthResponseDTO (
        String token,
        String nome,
        String email,
        Role role
){}
