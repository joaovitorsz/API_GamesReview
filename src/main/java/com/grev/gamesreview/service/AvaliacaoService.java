package com.grev.gamesreview.service;


import com.grev.gamesreview.DTO.AvaliacaoRequestDTO;
import com.grev.gamesreview.DTO.AvaliacaoResponseDTO;
import com.grev.gamesreview.DTO.AvaliacaoUpdateDTO;
import com.grev.gamesreview.model.Avaliacao;
import com.grev.gamesreview.repository.AvaliacoesRepository;
import com.grev.gamesreview.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvaliacaoService {

    private final GameRepository gameRepository;
    private final AvaliacoesRepository avaliacoesRepository;

    public AvaliacaoService(GameRepository gameRepository, AvaliacoesRepository avaliacoesRepository){
        this.gameRepository = gameRepository;
        this.avaliacoesRepository = avaliacoesRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<AvaliacaoResponseDTO>> listarPorJogo(Long gameId){
        return gameRepository.findById(gameId).map(game ->
                game.getAvaliacoes().stream()
                        .map(AvaliacaoResponseDTO::new)
                        .toList()
        );
    }

    public Optional<AvaliacaoResponseDTO> gravarAvaliacao(Long gameId, AvaliacaoRequestDTO dados){
        return gameRepository.findById(gameId).map(game -> {

            Avaliacao novaAvaliacao = new Avaliacao();
            novaAvaliacao.setAvaliador(dados.avaliador());
            novaAvaliacao.setNota(dados.nota());
            novaAvaliacao.setComentario(dados.comentario());
            novaAvaliacao.setGame(game);

            Avaliacao avaliacaoSalva = avaliacoesRepository.save(novaAvaliacao);
            return new AvaliacaoResponseDTO(avaliacaoSalva);
        });
    }

    public Optional<AvaliacaoResponseDTO> editarAvaliacao(Long idAvaliacao, AvaliacaoUpdateDTO dados){
        return avaliacoesRepository.findById(idAvaliacao).map(avaliacao -> {
            if (dados.nota() != null) avaliacao.setNota(dados.nota());
            if (dados.comentario() != null) avaliacao.setComentario(dados.comentario());

            Avaliacao avaliacaoAtualizada = avaliacoesRepository.save(avaliacao);
            return new AvaliacaoResponseDTO(avaliacaoAtualizada);
        });
    }

    public Optional<String> deletarAvaliacao(Long idAvaliacao) {
        return avaliacoesRepository.findById(idAvaliacao).map(avaliacao -> {
            String nomeJogo = avaliacao.getGame().getNome();
            String avaliador = avaliacao.getAvaliador();

            avaliacoesRepository.delete(avaliacao);
            return "Olá " + avaliador + "! Sua avaliação de " + nomeJogo + " foi removida!";
        });

    }

}
