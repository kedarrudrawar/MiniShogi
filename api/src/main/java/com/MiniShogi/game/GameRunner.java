package com.MiniShogi.game;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GameRunner {
    private myShogi game;

    public GameRunner(){
        this.game = new myShogi();
    }

    public myShogi getGame() {
        return this.game;
    }

}
