package com.grev.gamesreview.controller;

import com.grev.gamesreview.model.Avaliacoes;
import com.grev.gamesreview.model.Game;
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

    public GameReview(GameRepository repository) {
        this.repository = repository;
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
    public ResponseEntity<List<Game>> listarJogos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer metacritic) {
        if (nome == null && genero == null && metacritic == null) {
            return ResponseEntity.ok(repository.findAll());
        }
        return ResponseEntity.ok(repository.filtrar(nome, genero, metacritic));
    }

    @GetMapping("/review/id/{id}")
    public ResponseEntity<Game> buscarPorId(@PathVariable Long id) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/review/{nome}/listar-avaliacoes")
    public ResponseEntity<List<Avaliacoes>> gameAvaliacoes(@PathVariable String nome) {
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(game.getAvaliacoes());
    }

    // Posts

    @PostMapping("/review/gravar-jogo")
    public String gravarJogo(@RequestBody(required = false) Game novoJogo) {
        if (novoJogo == null) {
            return "Insira um novo jogo para prosseguir!";
        }
        novoJogo.setId(null);
        if (novoJogo.getAvaliacoes() == null) {
            novoJogo.setAvaliacoes(new ArrayList<>());
        }
        for (Avaliacoes avaliacao : novoJogo.getAvaliacoes()) {
            avaliacao.setId(null);
            avaliacao.setGame(novoJogo);
        }
        repository.save(novoJogo);
        return novoJogo.getNome() + " foi adicionado à lista com sucesso!";
    }

    @PostMapping("/review/{nome}/gravar-avaliacao")
    public ResponseEntity<String> gravarAvaliacao(@PathVariable String nome,
            @RequestBody(required = false) Avaliacoes avaliacao) {
        if (avaliacao == null) {
            return ResponseEntity.badRequest().body("Insira uma avaliação para prosseguir!");
        }
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        avaliacao.setId(null);
        avaliacao.setGame(game);
        game.getAvaliacoes().add(avaliacao);
        repository.save(game);
        return ResponseEntity.ok("A avaliação de " + game.getNome() + " foi adicionada com sucesso!");
    }

    // Puts

    @PutMapping("/review/{nome}/editar-jogo")
    public ResponseEntity<String> editarJogo(@PathVariable String nome,
            @RequestBody(required = false) Game dados) {
        if (dados == null) {
            return ResponseEntity.badRequest().body("Insira um jogo para prosseguir!");
        }
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        game.setNome(dados.getNome());
        game.setMetacritic(dados.getMetacritic());
        game.setGenero(dados.getGenero());
        game.setSinopse(dados.getSinopse());
        repository.save(game);
        return ResponseEntity.ok(game.getNome() + " foi editado com sucesso!");
    }

    @PutMapping("/review/{nome}/editar-avaliacao/{index}")
    public ResponseEntity<String> editarAvaliacao(@PathVariable String nome, @PathVariable int index,
            @RequestBody(required = false) Avaliacoes dados) {
        if (dados == null) {
            return ResponseEntity.badRequest().body("Insira uma avaliação para prosseguir!");
        }
        Game game = repository.findFirstByNomeOrderByIdAsc(nome).orElse(null);
        if (game == null) {
            return ResponseEntity.noContent().build();
        }
        if (index < 0 || index >= game.getAvaliacoes().size()) {
            return ResponseEntity.notFound().build();
        }
        Avaliacoes avaliacao = game.getAvaliacoes().get(index);
        avaliacao.setNota(dados.getNota());
        avaliacao.setComentario(dados.getComentario());
        repository.save(game);
        return ResponseEntity.ok("A avaliação de " + game.getNome() + " foi editado com sucesso!");
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

