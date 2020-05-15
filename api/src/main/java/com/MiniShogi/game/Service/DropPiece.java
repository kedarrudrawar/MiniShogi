package com.MiniShogi.game.Service;

public class DropPiece {
    private String piece;
    private String dest;

    DropPiece(String src, String dest){
        this.piece = src;
        this.dest = dest;
    }

    public String getPiece() {
        return this.piece;
    }

    public String getDest() {
        int row, col;
        row = Integer.parseInt(this.dest.split(",")[0]);
        col = Integer.parseInt(this.dest.split(",")[1]);

        return new Location(col, row).toString();
    }
}
