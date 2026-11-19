package com.grev.gamesreview.controller;

import com.grev.gamesreview.DTO.*;
import com.grev.gamesreview.model.Avaliacoes;
import com.grev.gamesreview.model.Game;
import com.grev.gamesreview.repository.AvaliacoesRepository;
import com.grev.gamesreview.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/game")
@Transactional
public class GameReview {
    private final GameRepository repository;
    private final AvaliacoesRepository avaliacoesRepository;

    public GameReview(GameRepository repository, AvaliacoesRepository avaliacoesRepository) {
        this.repository = repository;
        this.avaliacoesRepository = avaliacoesRepository;
    }

    // Gets

    @GetMapping("/review/listar-jogos")
    public ResponseEntity<List<GameResponseDTO>> listarJogos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer metacritic) {

        List<Game> jogos;

        if (titulo == null && genero == null && metacritic == null) {
            jogos = repository.findAll();
        } else {
            jogos = repository.filtrar(titulo, genero, metacritic);
        }

        List<GameResponseDTO> resposta = jogos.stream()
                .map(GameResponseDTO::new)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/review/id/{id}")
    public ResponseEntity<GameResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Game game = repository.findById(id).orElse(null);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GameResponseDTO(game));
    }

    @GetMapping("/review/listar-avaliacoes/{gameId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> gameAvaliacoes(
            @PathVariable Long gameId) {

        Game game = repository.findById(gameId).orElse(null);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        List<AvaliacaoResponseDTO> avaliacoesDTO = game.getAvaliacoes().stream()
                .map(AvaliacaoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(avaliacoesDTO);
    }

    // Posts

    @PostMapping("/review/gravar-jogo")
    public ResponseEntity<GameResponseDTO> gravarJogo(
            @RequestBody GameRequestDTO dados) {

        Game novoJogo = new Game();
        novoJogo.setNome(dados.titulo());
        novoJogo.setGenero(dados.genero());
        novoJogo.setMetacritic(dados.metacritic());
        novoJogo.setSinopse(dados.sinopse());

        Game jogoSalvo = repository.save(novoJogo);

        return ResponseEntity.ok(new GameResponseDTO(jogoSalvo));
    }

    @PostMapping("/review/gravar-avaliacao/{gameId}")
    public ResponseEntity<AvaliacaoResponseDTO> gravarAvaliacao(
            @PathVariable Long gameId,
            @RequestBody AvaliacaoRequestDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }
        Game game = repository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        Avaliacoes novaAvaliacao = new Avaliacoes();
        novaAvaliacao.setAvaliador(dados.avaliador());
        novaAvaliacao.setNota(dados.nota());
        novaAvaliacao.setComentario(dados.comentario());
        novaAvaliacao.setGame(game);

        Avaliacoes avaliacaoSalva = avaliacoesRepository.save(novaAvaliacao);

        return ResponseEntity.ok(new AvaliacaoResponseDTO(avaliacaoSalva));
    }

    // Puts

    @PutMapping("/review/editar-jogo/{gameId}")
    public ResponseEntity<GameResponseDTO> editarJogo(
            @PathVariable Long gameId,
            @RequestBody GameRequestDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }
        Game game = repository.findById(gameId).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        if (dados.titulo() != null) game.setNome(dados.titulo());
        if (dados.metacritic() != null) game.setMetacritic(dados.metacritic());
        if (dados.genero() != null) game.setGenero(dados.genero());
        if (dados.sinopse() != null) game.setSinopse(dados.sinopse());

        Game jogoAtualizado = repository.save(game);

        return ResponseEntity.ok(new GameResponseDTO(jogoAtualizado));
    }

    @PutMapping("/review/editar-avaliacao/{idAvaliacao}")
    public ResponseEntity<AvaliacaoResponseDTO> editarAvaliacao(
            @PathVariable Integer idAvaliacao,
            @RequestBody AvaliacaoUpdateDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }

        Avaliacoes avaliacao = avaliacoesRepository.findById(idAvaliacao).orElse(null);
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }

        if (dados.nota() != null) avaliacao.setNota(dados.nota());
        if (dados.comentario() != null) avaliacao.setComentario(dados.comentario());

        Avaliacoes avaliacaoAtualizada = avaliacoesRepository.save(avaliacao);

        return  ResponseEntity.ok(new AvaliacaoResponseDTO(avaliacaoAtualizada));
    }

    // Deletes

    @DeleteMapping("/review/deletar-jogo/{gameId}")
    public ResponseEntity<String> deletarJogo(
            @PathVariable Long gameId) {

        Game game = repository.findById(gameId).orElse(null);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        String nomeJogo = game.getNome();

        repository.delete(game);
        return ResponseEntity.ok("O jogo " + nomeJogo + " foi deletado!");
    }

    @DeleteMapping("/review/deletar-avaliacao/{idAvaliacao}")
    public ResponseEntity<String> deletarAvaliacao(
            @PathVariable Integer idAvaliacao) {

        Avaliacoes avaliacao = avaliacoesRepository.findById(idAvaliacao).orElse(null);

        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }

        String nomeJogo = avaliacao.getGame().getNome();
        String avaliador = avaliacao.getAvaliador();

        avaliacoesRepository.delete(avaliacao);

        return ResponseEntity.ok("Olá, " + avaliador + "! Sua avaliação de " + nomeJogo + " foi removida!");
    }

}

