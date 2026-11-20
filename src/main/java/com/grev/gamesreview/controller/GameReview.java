package com.grev.gamesreview.controller;

import com.grev.gamesreview.DTO.*;
import com.grev.gamesreview.service.AvaliacaoService;
import com.grev.gamesreview.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameReview {
    private final GameService gameService;
    private final AvaliacaoService avaliacaoService;

    public GameReview(GameService gameService, AvaliacaoService avaliacaoService) {
        this.gameService = gameService;
        this.avaliacaoService = avaliacaoService;
    }

    // Gets

    @GetMapping("/review/listar-jogos")
    public ResponseEntity<List<GameResponseDTO>> listarJogos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer metacritic) {

        return ResponseEntity.ok(gameService.listarJogos(titulo, genero, metacritic));
    }

    @GetMapping("/review/id/{id}")
    public ResponseEntity<GameResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return gameService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/review/listar-avaliacoes/{gameId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> gameAvaliacoes(
            @PathVariable Long gameId) {

        return avaliacaoService.listarPorJogo(gameId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Posts

    @PostMapping("/review/gravar-jogo")
    public ResponseEntity<GameResponseDTO> gravarJogo(
            @RequestBody GameRequestDTO dados) {

        if (dados == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(gameService.gravarJogo(dados));
    }

    @PostMapping("/review/gravar-avaliacao/{gameId}")
    public ResponseEntity<AvaliacaoResponseDTO> gravarAvaliacao(
            @PathVariable Long gameId,
            @RequestBody AvaliacaoRequestDTO dados) {

            return avaliacaoService.gravarAvaliacao(gameId, dados)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

    // Puts

    @PutMapping("/review/editar-jogo/{gameId}")
    public ResponseEntity<GameResponseDTO> editarJogo(
            @PathVariable Long gameId,
            @RequestBody GameRequestDTO dados) {

        if (dados == null){
            return ResponseEntity.badRequest().build();
        }

        return gameService.editarJogo(gameId, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/review/editar-avaliacao/{idAvaliacao}")
    public ResponseEntity<AvaliacaoResponseDTO> editarAvaliacao(
            @PathVariable Long idAvaliacao,
            @RequestBody AvaliacaoUpdateDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }

        return  avaliacaoService.editarAvaliacao(idAvaliacao, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletes

    @DeleteMapping("/review/deletar-jogo/{gameId}")
    public ResponseEntity<String> deletarJogo(
            @PathVariable Long gameId) {

        return gameService.deletarJogo(gameId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/review/deletar-avaliacao/{idAvaliacao}")
    public ResponseEntity<String> deletarAvaliacao(
            @PathVariable Long idAvaliacao) {

        return avaliacaoService.deletarAvaliacao(idAvaliacao)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

