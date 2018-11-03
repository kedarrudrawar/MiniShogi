import javafx.util.*;
import java.util.*;

public abstract class Piece{
//    Fields
    public Player player;
    String name;
//    public boolean captured = false;


//    Defined methods
    public String toString(){
        return this.name;
    }


//    public void capture(){
//        this.captured = true;
//    }

    public void drop(String location){
    }

    public String getName(){
        if(this.player.name.equals("UPPER"))
            return this.name;
        else
            return this.name.toLowerCase();

    }

    public Player getPlayer(){
        return this.player;
    }




//    Abstract methods
    abstract void promote();
    abstract boolean isValidMove(int[] startPos, int[] endPos);
}


class Pawn extends Piece {
    public Pawn(Player player) {
        this.name = "P";
        this.player = player;
    }


    public void promote() {
    }

    public boolean isValidMove(int[] startPos, int[] endPos) {
//        This method will check whether the move from start to end is valid
        if (endPos[0] != startPos[0]) {
            return false;
        }
        if (Math.abs(endPos[1] - startPos[1]) == 1)
            return true;
        return false;
    }
}

class Bishop extends Piece{
    public Bishop(Player player){
        this.name = "B";
        this.player = player;
    }


    public void checkPathAvailability(){}

    public void promote(){}

    public boolean isValidMove(int[] startPosArr, int[] endPosArr){
//        This method will check whether the move from start to end is valid
        if(Math.abs(startPosArr[0] - endPosArr[0]) != Math.abs(startPosArr[1] - endPosArr[1])){
            return false;
        }



        return true;
    }
}



class King extends Piece{
    public King(Player player){
        this.name = "K";
        this.player = player;
    }



    public void promote(){}



//    This method will check whether the move from start to end is valid
    public boolean isValidMove(int[] startPos, int[] endPos){

//        The king can move in any direction, so the absolute value
//          of difference between startPos and endPos for each position must be 1 or 0.
        if(Math.abs(startPos[0] - endPos[0]) <= 1 && Math.abs(startPos[1] - endPos[1]) <= 1)
            return true;
        return false;
    }
}


class Rook extends Piece {

    public Rook(Player player) {
        this.name = "R";
        this.player = player;
    }


    public void promote() {
    }

    public boolean isValidMove(int[] startPos, int[] endPos){
        if(startPos[0] != endPos[0]){
            if(startPos[1] == endPos[1]){
                return true;
            }
        }
        else if(startPos[0] == endPos[0]){
            if(startPos[1] != endPos[1]){
                return true;
            }
        }
        return false;
    }

}


class SilverGeneral extends Piece {
    public SilverGeneral(Player player) {
        this.name = "S";
        this.player = player;
    }


    public void promote() {
    }


    public boolean isValidMove(int[] startPos, int[] endPos) {
//        This method will check whether the move from start to end is valid
        if (Math.abs(startPos[0] - endPos[0]) == 1 && Math.abs(startPos[1] - endPos[1]) == 1) {
            return true;
        } else {
            if (endPos[1] - startPos[1] == 1) {
                if (endPos[0] - startPos[0] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}

class GoldGeneral extends Piece {
    public GoldGeneral(Player player) {
        this.name = "G";
        this.player = player;
    }

    public void promote() {
    }

    public boolean isValidMove(int[] startPos, int[] endPos) {
//        This method will check whether the move from start to end is valid
        return false;
    }
}
