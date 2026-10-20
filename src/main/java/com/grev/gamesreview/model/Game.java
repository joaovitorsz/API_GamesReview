package com.grev.gamesreview.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@JsonPropertyOrder({"id", "nome", "metacritic", "genero", "sinopse"})
public class Game {

    private Long id;
    private String nome;
    private Integer metacritic;
    private String genero;
    private String sinopse;

    private List<Avaliacoes> avaliacoes = new ArrayList<Avaliacoes>();

}
