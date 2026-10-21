package com.grev.gamesreview.repository;

import com.grev.gamesreview.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findFirstByNomeOrderByIdAsc(String nome);

    @Query("""
        SELECT g FROM Game g
        WHERE (:nome IS NULL OR LOWER(g.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:genero IS NULL OR LOWER(g.genero) = LOWER(:genero))
          AND (:metacritic IS NULL OR g.metacritic = :metacritic)
        ORDER BY g.id
        """)
    List<Game> filtrar(@Param("nome") String nome,
                      @Param("genero") String genero,
                      @Param("metacritic") Integer metacritic);
}
