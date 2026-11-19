package com.grev.gamesreview.repository;

import com.grev.gamesreview.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = """
            SELECT * FROM games g
            WHERE (CAST(:nome AS text) IS NULL OR
                    REPLACE(TRANSLATE (LOWER(g.nome), 'áàâãéèíìóòôõúùûç', 'aaaaeeiioooouuuc'), ' ', '')
                            LIKE CONCAT('%', REPLACE(TRANSLATE (LOWER(CAST(:nome AS text)), 'áàâãéèíìóòôõúùûç', 'aaaaeeiioooouuuc'), ' ', ''), '%'))
              AND (CAST(:genero AS text) IS NULL OR
                    REPLACE(TRANSLATE (LOWER(g.genero), 'áàâãéèíìóòôõúùûç', 'aaaaeeiioooouuuc'), ' ', '')
                            LIKE CONCAT('%', REPLACE(TRANSLATE (LOWER(CAST(:genero AS text)), 'áàâãéèíìóòôõúùûç', 'aaaaeeiioooouuuc'), ' ', ''), '%'))
              AND (CAST(:metacritic AS integer) IS NULL OR g.metacritic = CAST(:metacritic AS integer))
            ORDER BY g.id
            """, nativeQuery = true)
    List<Game> filtrar(
            @Param("nome") String nome,
            @Param("genero") String genero,
            @Param("metacritic") Integer metacritic);
}
