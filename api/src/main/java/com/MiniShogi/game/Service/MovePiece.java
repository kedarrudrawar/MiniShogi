package com.MiniShogi.game.Service;

public class MovePiece {
    private String src;
    private String dest;

    public String getSrc() {
        int row, col;
        row = Integer.parseInt(this.src.split(",")[0]);
        col = Integer.parseInt(this.src.split(",")[1]);

        return new Location(col, row).toString();
    }

    public String getDest() {
        int row, col;
        row = Integer.parseInt(this.dest.split(",")[0]);
        col = Integer.parseInt(this.dest.split(",")[1]);

        return new Location(col, row).toString();
    }


    MovePiece(String src, String dest){
        this.src = src;
        this.dest = dest;
    }
}
