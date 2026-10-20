package com.grev.gamesreview.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@JsonPropertyOrder({"id", "avaliador", "nota", "comentario"})
public class Avaliacoes {

    private Integer id;
    private String avaliador;
    private Double nota;
    private String comentario;

    @JsonIgnore
    private Game game;
}
