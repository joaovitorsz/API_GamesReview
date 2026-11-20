package com.grev.gamesreview.service;

import com.grev.gamesreview.DTO.GameRequestDTO;
import com.grev.gamesreview.model.Game;
import com.grev.gamesreview.repository.GameRepository;
import com.grev.gamesreview.DTO.GameResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    @Transactional(readOnly = true)
    public List<GameResponseDTO> listarJogos(String titulo, String genero, Integer metacritic) {
        String tituloRegex = montarRegexFiltro(titulo);
        String generoRegex = montarRegexFiltro(genero);

        List<Game> jogos;
        if(tituloRegex == null && generoRegex == null && metacritic == null) {
            jogos = gameRepository.findAll();
        } else{
            jogos = gameRepository.filtrar(tituloRegex, generoRegex, metacritic);
        }

        return jogos.stream()
                .map(GameResponseDTO::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public Optional<GameResponseDTO> buscarPorId(Long id){
        return gameRepository.findById(id)
                .map(GameResponseDTO::new);
    }

    public GameResponseDTO gravarJogo(GameRequestDTO dados){
        Game novoJogo = new Game();
        novoJogo.setNome(dados.titulo());
        novoJogo.setGenero(dados.genero());
        novoJogo.setMetacritic(dados.metacritic());
        novoJogo.setSinopse(dados.sinopse());

        Game jogoSalvo = gameRepository.save(novoJogo);

        return new GameResponseDTO(jogoSalvo);
    }

    public Optional<GameResponseDTO> editarJogo(Long gameId, GameRequestDTO dados){
        return gameRepository.findById(gameId).map(game -> {
            if (dados.titulo() != null) game.setNome(dados.titulo());
            if (dados.genero() != null) game.setGenero(dados.genero());
            if (dados.metacritic() != null) game.setMetacritic(dados.metacritic());
            if (dados.sinopse() != null) game.setSinopse(dados.sinopse());

            Game jogoAtualizado = gameRepository.save(game);
            return new GameResponseDTO(jogoAtualizado);
        });
    }

    public Optional<String> deletarJogo(Long gameId){
        return gameRepository.findById(gameId).map(game -> {
            String nomeJogo = game.getNome();
            gameRepository.delete(game);

            return "O jogo " + nomeJogo + " foi deletado!";
        });
    }

    private String montarRegexFiltro(String texto){
        if(texto == null || texto.isBlank()){
            return null;
        }

        String limparDigitacao = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", "")
                .toLowerCase();
        limparDigitacao = limparDigitacao.replaceAll("([\\\\+*?\\[\\]^$(){}|.])", "\\\\$1");

        return limparDigitacao
                .replace("a", "[aáàâã]")
                .replace("e", "[eéèê]")
                .replace("i", "[iíìî]")
                .replace("o", "[oóòôõ]")
                .replace("u", "[uúùû]")
                .replace("c", "[cç]");
    }

}