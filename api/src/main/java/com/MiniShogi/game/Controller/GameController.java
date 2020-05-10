package com.MiniShogi.game.Controller;

import com.MiniShogi.game.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping(value = "/resetBoard")
    public void resetBoard(){
        this.gameService.resetBoard();
    }




}
