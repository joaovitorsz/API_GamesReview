package com.grev.gamesreview.DTO;

import com.grev.gamesreview.model.Avaliacoes;

public record AvaliacaoResponseDTO(long id, String avaliador, Double nota, String comentario) {
    public AvaliacaoResponseDTO(Avaliacoes avaliacao) {
        this(avaliacao.getId(), avaliacao.getAvaliador(), avaliacao.getNota(), avaliacao.getComentario());
    }
}
