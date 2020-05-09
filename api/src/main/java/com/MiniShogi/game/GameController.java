package com.MiniShogi.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @Autowired
    private GameRunner runner;

    @Value("${game.name}")
    private String gameName;

    @GetMapping(value = "/board")
    public Board getBoard(){
        return this.runner.getGame().board;
    }

    @GetMapping(value = "/currentPlayer")
    public Player getCurrentPlayer(){
        myShogi game = this.runner.getGame();
        return game.getCurrentPlayer();
    }





}
