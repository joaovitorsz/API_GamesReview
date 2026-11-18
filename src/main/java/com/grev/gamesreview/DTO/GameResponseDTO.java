package com.grev.gamesreview.DTO;

import com.grev.gamesreview.model.Game;

public record GameResponseDTO(Long id, String titulo, String genero, Integer metacritic, String sinopse) {
    public GameResponseDTO(Game game) {
        this (game.getId(), game.getNome(), game.getGenero(), game.getMetacritic(), game.getSinopse());
    }
}
