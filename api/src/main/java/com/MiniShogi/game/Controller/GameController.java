package com.MiniShogi.game.Controller;

import com.MiniShogi.game.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PastOrPresent;
import java.util.List;


@CrossOrigin
@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping(value = "/board")
    public Board getBoard(){
        return this.gameService.getBoard();
    }

    @GetMapping(value = "/currentPlayer")
    public Player getCurrentPlayer(){
        return this.gameService.getCurrentPlayer();
    }

    @PostMapping(value="/move")
    public boolean movePiece(@RequestBody MovePiece move){
        String src = move.getSrc();
        String dest = move.getDest();
        return this.gameService.movePiece(src, dest);
    }

    @PostMapping(value="/drop")
    public boolean dropPiece(@RequestBody DropPiece drop){
        String piece = drop.getPiece();
        String dest = drop.getDest();
        return this.gameService.dropPiece(piece, dest);
    }

    @GetMapping(value = "/resetBoard")
    public void resetBoard(){
        this.gameService.resetBoard();
    }

    @GetMapping(value = "/getCaptured")
    public List<Piece> getCaptured(@RequestParam String player){
        return this.gameService.getCaptured(player);
    }

}
