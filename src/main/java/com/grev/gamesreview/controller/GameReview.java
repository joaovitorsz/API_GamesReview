package com.grev.gamesreview.controller;

import com.grev.gamesreview.DTO.AvaliacaoRequestDTO;
import com.grev.gamesreview.DTO.AvaliacaoResponseDTO;
import com.grev.gamesreview.DTO.GameRequestDTO;
import com.grev.gamesreview.DTO.GameResponseDTO;
import com.grev.gamesreview.model.Avaliacoes;
import com.grev.gamesreview.model.Game;
import com.grev.gamesreview.repository.AvaliacoesRepository;
import com.grev.gamesreview.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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

    @GetMapping("/review/{nome}")
    public String gameNome(@PathVariable String nome) {
        return "Jogo selecionado: " + nome;
    }

    @GetMapping("/review/{nome}/metacritic")
    public String gameMetacritic(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return "Jogo não encontrado!";
        }
        return "A avaliação no Metacritic de " + game.getNome() + " é " + game.getMetacritic() + "!";
    }

    @GetMapping("/review/{nome}/genero")
    public String gameGenero(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return "Jogo não encontrado!";
        }
        return "O gênero de " + game.getNome() + " é " + game.getGenero();
    }

    @GetMapping("/review/{nome}/sinopse")
    public String gameSinopse(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return "Jogo não encontrado!";
        }
        return game.getSinopse() + "!";
    }

    @GetMapping("/review/listar-jogos")
    public ResponseEntity<List<GameResponseDTO>> listarJogos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer metacritic) {

        List<Game> jogos;
        if (nome == null && genero == null && metacritic == null) {
            jogos = repository.findAll();
        } else {
            jogos = repository.filtrar(nome, genero, metacritic);
        }

        List<GameResponseDTO> resposta = jogos.stream()
                .map(GameResponseDTO::new)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/review/id/{id}")
    public ResponseEntity<GameResponseDTO> buscarPorId(@PathVariable Long id) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GameResponseDTO(game));
    }

    @GetMapping("/review/{nome}/listar-avaliacoes")
    public ResponseEntity<List<AvaliacaoResponseDTO>> gameAvaliacoes(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }

        List<AvaliacaoResponseDTO> avaliacoesDTO = game.getAvaliacoes().stream()
                .map(AvaliacaoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(avaliacoesDTO);
    }

    // Posts

    @PostMapping("/review/gravar-jogo")
    public ResponseEntity<GameResponseDTO> gravarJogo(@RequestBody GameRequestDTO dados) {

        Game novoJogo = new Game();
        novoJogo.setNome(dados.titulo());
        novoJogo.setGenero(dados.genero());
        novoJogo.setMetacritic(dados.metacritic());
        novoJogo.setSinopse(dados.sinopse());

        Game jogoSalvo = repository.save(novoJogo);

        return ResponseEntity.ok(new GameResponseDTO(jogoSalvo));
    }

    @PostMapping("/review/{nome}/gravar-avaliacao")
    public ResponseEntity<AvaliacaoResponseDTO> gravarAvaliacao(@PathVariable String nome,
                                                                @RequestBody AvaliacaoRequestDTO dados) {
        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
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

    @PutMapping("/review/{nome}/editar-jogo")
    public ResponseEntity<GameResponseDTO> editarJogo(
            @PathVariable String nome,
            @RequestBody GameRequestDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        game.setNome(dados.titulo());
        game.setMetacritic(dados.metacritic());
        game.setGenero(dados.genero());
        game.setSinopse(dados.sinopse());

        Game jogoAtualizado = repository.save(game);

        return ResponseEntity.ok(new GameResponseDTO(jogoAtualizado));
    }

    @PutMapping("/review/editar-avaliacao/{idAvaliacao}")
    public ResponseEntity<AvaliacaoResponseDTO> editarAvaliacao(
            @PathVariable Integer idAvaliacao,
            @RequestBody AvaliacaoRequestDTO dados) {

        if (dados == null) {
            return ResponseEntity.badRequest().build();
        }

        Avaliacoes avaliacao = avaliacoesRepository.findById(idAvaliacao).orElse(null);
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }

        avaliacao.setAvaliador(dados.avaliador());
        avaliacao.setNota(dados.nota());
        avaliacao.setComentario(dados.comentario());

        Avaliacoes avaliacaoAtualizada = avaliacoesRepository.save(avaliacao);

        return  ResponseEntity.ok(new AvaliacaoResponseDTO(avaliacaoAtualizada));
    }

    // Deletes

    @DeleteMapping("/review/{nome}/deletar-jogo")
    public ResponseEntity<String> deletarJogo(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        repository.delete(game);
        return ResponseEntity.ok("O jogo " + nome + " foi deletado!");
    }

    @DeleteMapping("/review/{nome}/deletar-avaliacao/{index}")
    public ResponseEntity<String> deletarAvaliacao(@PathVariable String nome, @PathVariable int index) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        if (index < 0 || index >= game.getAvaliacoes().size()) {
            return ResponseEntity.notFound().build();
        }
        game.getAvaliacoes().remove(index);
        repository.save(game);
        return ResponseEntity.ok("A avaliação " + index + " do jogo " + nome + " foi removida!");
    }
}

