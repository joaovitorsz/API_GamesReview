package com.grev.gamesreview.repository;


import com.grev.gamesreview.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacoesRepository  extends JpaRepository<Avaliacao, Long> {
}
