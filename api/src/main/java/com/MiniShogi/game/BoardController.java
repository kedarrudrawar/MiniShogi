package com.MiniShogi.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class BoardController {
    @Autowired
    private GameRunner runner;

    @Value("${game.name}")
    private String gameName;

//    @GetMapping(value="/initialize")
//    public String initializeGame(){
//        this.runner = new GameRunner();
//        return this.gameName;
//    }

    @GetMapping(value = "/board")
    public Map<String, String> getBoard(){
        Board board = this.runner.getGame().board;

        return board.getBoardMap();
    }

    @GetMapping(value = "/currentPlayer")
    public Player getCurrentPlayer(){
        myShogi game = this.runner.getGame();
        Player current = game.getCurrentPlayer();
        for(int i = 0; i < current.getOnBoard().size(); i++){
            System.out.println(current.getOnBoard().get(i));
        }
//        System.out.println(current.getOnBoard().size());
        return current;

    }



}
