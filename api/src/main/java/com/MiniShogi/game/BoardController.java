package com.MiniShogi.game;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BoardController {
    private Board board = new Board();

    @RequestMapping("/board")
    public Map<String, String> getBoard(){
        Piece[][] board = this.board.getBoard();
        Map<String, String> boardMap = new HashMap<>();

        for(int i = 0 ; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                String pos = String.format("%d,%d", i, j);
                String pieceName = board[i][j] != null ? board[i][j].toString() : "";
                boardMap.put(pos, pieceName);
            }
        }
        return boardMap;
    }

}
