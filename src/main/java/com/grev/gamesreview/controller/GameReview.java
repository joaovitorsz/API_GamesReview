package com.grev.gamesreview.controller;

import com.grev.gamesreview.model.Avaliacoes;
import com.grev.gamesreview.model.Game;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;


@RestController
@RequestMapping("/game")
public class GameReview {

    List<Game> bancoDados = new ArrayList<>(); //Simular banco de dados
    
//Gets -------------------------------------------------------------------------------
    
    //Nome do Jogo
    @GetMapping("/review/{nome}")
    public String gameNome(@PathVariable String nome) {
        return "Jogo selecionado: " + nome;
    }
    
    //Avaliação do Metacritic
    @GetMapping("/review/{nome}/metacritic")
    public String gameMetacritic(@PathVariable String nome) {
        for (Game jogoNome : bancoDados){
            if (jogoNome.getNome().equals(nome)){
                return "A avaliação no Metacritic de " + jogoNome.getNome() + " é " + jogoNome.getMetacritic() + "!";
            }
        }
        return "Jogo não encontrado!";
    }
    
    //Gênero do Jogo
    @GetMapping("/review/{nome}/genero")
    public String gameGenero(@PathVariable String nome) {
        for (Game jogoGenero : bancoDados){
            if (jogoGenero.getNome().equals(nome)){
                return "O gênero de " + jogoGenero.getNome() + " é " + jogoGenero.getGenero();
            }
        }
        return "Jogo não encontrado!";
    }
    
    //Sinopse do Jogo
    @GetMapping("/review/{nome}/sinopse")
    public String gameSinopse(@PathVariable String nome) {
        for (Game jogoSinopse : bancoDados){
            if (jogoSinopse.getNome().equals(nome)){
                return jogoSinopse.getSinopse() + "!";
            }
        }
        return "Jogo não encontrado!";
    }

    //Listar todos os jogos
    @GetMapping("/review/listar-jogos")
    public ResponseEntity<List<Game>> listarJogos(){
        return  ResponseEntity.ok(bancoDados);
    }

    //Listar avaliações de um jogo
    @GetMapping("/review/{nome}/listar-avaliacoes")
    public ResponseEntity<List<Avaliacoes>> gameAvaliacoes(@PathVariable String nome){
        for (Game jogoAvaliacoes : bancoDados){
            if (jogoAvaliacoes.getNome().equals(nome)){
                    return ResponseEntity.ok(jogoAvaliacoes.getAvaliacoes());
                }
            }
        return ResponseEntity.noContent().build();
    }



//Posts -------------------------------------------------------------------------------
    
    @PostMapping("/review/gravar-jogo")
    public String gravarJogo(@RequestBody(required = false) Game novoJogo){
        if (novoJogo == null){
            return "Insira um novo jogo para prosseguir!";
        } else{
            bancoDados.add(novoJogo);
            return novoJogo.getNome() + " foi adicionado à lista com sucesso!";
        }
    }

    @PostMapping("/review/{nome}/gravar-avaliacao")
    public ResponseEntity<String> gravarAvaliacao(@PathVariable String nome,
                                                           @RequestBody(required = false) Avaliacoes avaliacoes){
        int index = 0;
        Game game = new Game();
        for (int i = 0; i < bancoDados.size(); i++) {
            if(bancoDados.get(i).getNome().equals(nome)) {
                index = i;
                game = bancoDados.get(i);
            }
        }

        game.getAvaliacoes().add(avaliacoes);
        return ResponseEntity.ok("A avaliação de " + game.getNome() + " foi adicioanda com sucesso!");
    }



//Puts -------------------------------------------------------------------------------

    @PutMapping("/review/{nome}/editar-jogo")
    public ResponseEntity<String> editarJogo(@PathVariable String nome, @RequestBody(required = false) Game game){

        int index = 0;

        for (int i = 0; i < bancoDados.size(); i++){
            if (bancoDados.get(i).getNome().equals(nome)){
                index = i;
                Game g = new Game();
                g.setId(game.getId());
                g.setNome(game.getNome());
                g.setMetacritic(game.getMetacritic());
                g.setGenero(game.getGenero());
                g.setSinopse(game.getSinopse());
                g.setAvaliacoes(bancoDados.get(i).getAvaliacoes());

                bancoDados.set(index, g);

                return ResponseEntity.ok(g.getNome() + " foi editado com sucesso!");
            }
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/review/{nome}/editar-avaliacao/{index}")
    public ResponseEntity<String> editarAvaliacao(@PathVariable String nome, @PathVariable int index, @RequestBody(required = false) Avaliacoes avaliacao){

        for(int i = 0; i < bancoDados.size(); i++){
            if(bancoDados.get(i).getNome().equals(nome)){
                Game editarAvaliacao = bancoDados.get(i);

                Avaliacoes a = editarAvaliacao.getAvaliacoes().get(index);
                a.setId(avaliacao.getId());
                a.setNota(avaliacao.getNota());
                a.setComentario(avaliacao.getComentario());

                return ResponseEntity.ok("A avaliação de " + editarAvaliacao.getNome() + " foi editado com sucesso!");
            }
        }
        return ResponseEntity.noContent().build();
    }


//Delete -------------------------------------------------------------------------------

    @DeleteMapping("/review/{nome}/deletar-jogo")
    public ResponseEntity<String> deletarJogo(@PathVariable String nome){

        for (int i = 0; i < bancoDados.size(); i++){
            if (bancoDados.get(i).getNome().equals(nome)){
                bancoDados.remove(i);

                return ResponseEntity.ok("O jogo " + nome + " foi deletado!");
            }
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/review/{nome}/deletar-avaliacao/{index}")
    public ResponseEntity<String> deletarAvaliacao(@PathVariable String nome,@PathVariable int index){

        for (int i = 0; i < bancoDados.size(); i++){
            if (bancoDados.get(i).getNome().equals(nome)){
                Game deletarAvaliacao = bancoDados.get(i);
                deletarAvaliacao.getAvaliacoes().remove(index);

                return ResponseEntity.ok("A avaliação " + index + " do jogo " + nome + " foi removida!");
            }
        }
        return ResponseEntity.noContent().build();
    }

}
