package com.MiniShogi.game.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRunner runner;

    public Board getBoard(){
        return this.runner.getGame().board;
    }

    public Player getCurrentPlayer(){
        myShogi game = this.runner.getGame();
        return game.getCurrentPlayer();
    }

    public boolean dropPiece(String piece, String dest){
        String command = String.format("drop %s %s", piece, dest);
        System.out.println(command);
        return this.runner.getGame().performTurn(command);
    }

    public boolean movePiece(String src, String dest){
        String command = String.format("move %s %s", src, dest);
        System.out.println(command);
        return this.runner.getGame().performTurn(command);
    }

    public void resetBoard(){
        this.runner.resetGame();
    }

    public List<Piece> getCaptured(String player) {
        return this.runner.getGame().board.getPlayer(player).getCaptured();
    }
}
