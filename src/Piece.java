import javafx.util.*;
import java.util.*;

public abstract class Piece{
//    Fields
    protected Player player;
    protected String name;


//    Defined methods
    public String toString(){
        return this.name;
    }

    public void drop(String location){
    }

    public String getName(){
        if(this.player.getName().equals("UPPER"))
            return this.name;
        else
            return this.name.toLowerCase();

    }

    public Player getPlayer(){
        return this.player;
    }


    public void promoteName(){
        this.name = "+" + this.name;
    }


//    Abstract methods
    abstract void promoteMovement();
//    abstract boolean isValidMove(Location startPos, Location endPos);
    abstract List<Location> findValidPath(Location startPos, Location endPos);


}

class Bishop extends Piece{
    public Bishop(Player player){
        this.name = "B";
        this.player = player;
    }


    public void promoteMovement() {
        this.promoteName();
    }

    public List<Location> findValidPath(Location startPos, Location endPos){

        List<Location> retList = new ArrayList<Location>();

        int colDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if(colDiff != rowDiff){
            return retList;
        }

        int startCol = Math.min(startPos.getCol(), endPos.getCol());
        int startRow = Math.min(startPos.getRow(), endPos.getRow());

        int endCol = Math.max(startPos.getCol(), endPos.getCol());

        int j = startRow;
        for(int i = startCol; i < endCol; i++) {
            retList.add(new Location(i, j));
            j++;
        }


        return retList;
    }
}


class Rook extends Piece {

    public Rook(Player player) {
        this.name = "R";
        this.player = player;
    }


    public void promoteMovement() {

        this.promoteName();
    }




    public List<Location> findValidPath(Location startPos, Location endPos){
        List<Location> retList = new ArrayList<Location>();

        if(startPos.getCol() != endPos.getCol()){
            if(startPos.getRow() != endPos.getRow()){
                return retList;
            }
        }

        boolean horizontal = false;
//        Check if movement is horizontal or vertical:
        if(startPos.getRow() == endPos.getRow()){
            horizontal = true;
        }

        if(horizontal){
            int startCol = Math.min(startPos.getCol(), endPos.getCol());
            int endCol = Math.max(startPos.getCol(), endPos.getCol());

            int row = startPos.getRow();

            for(int col = startCol; col < endCol; col++){
                retList.add(new Location(col, row));
            }
        }

        else{
            int startRow = Math.min(startPos.getRow(), endPos.getRow());
            int endRow = Math.max(startPos.getRow(), endPos.getRow());

            int col = startPos.getCol();

            for(int row = startRow; row < endRow; row++){
                retList.add(new Location(col, row));
            }
        }
        return retList;
    }

}




class Pawn extends Piece {
    public Pawn(Player player) {
        this.name = "P";
        this.player = player;
    }


    public void promoteMovement() {



        this.promoteName();
    }


    public List<Location> findValidPath(Location startPos, Location endPos){
        List<Location> retList = new ArrayList<Location>();

        System.out.println("start col: " + startPos.getCol());
        System.out.println("start row: " + startPos.getRow());

        if (endPos.getCol() != startPos.getCol()) {
            return retList;
        }

        if (Math.abs(endPos.getRow() - startPos.getRow()) == 1)
            retList.add(endPos);

        return retList;

    }
}





class King extends Piece{
    public King(Player player){
        this.name = "K";
        this.player = player;
    }



    public void promoteMovement() {



        this.promoteName();
    }



//    This method will check whether the move from start to end is valid
//    public boolean isValidMove(Location startPos, Location endPos){
//
////        The king can move in any direction, so the absolute value
////          of difference between startPos and endPos for each position must be 1 or 0.
//        if(Math.abs(startPos.getCol() - endPos.getCol()) <= 1 && Math.abs(startPos.getRow() - endPos.getRow()) <= 1)
//            return true;
//        return false;
//    }



    public List<Location> findValidPath(Location startPos, Location endPos){
        List<Location> retList = new ArrayList<Location>();
        if(Math.abs(startPos.getCol() - endPos.getCol()) <= 1 && Math.abs(startPos.getRow() - endPos.getRow()) <= 1){
            retList.add(endPos);
        }
        return retList;

    }
}





class SilverGeneral extends Piece {
    public SilverGeneral(Player player) {
        this.name = "S";
        this.player = player;
    }


    public void promoteMovement() {


        this.promoteName();
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if (columnDiff > 1 || rowDiff > 1)
            throw new IllegalArgumentException("Illegal move");

        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;


        List<Location> retList = new ArrayList<Location>();

        if (columnDiff != 0) {
            if (rowDiff != 0) {
                retList.add(endPos);
            }
        } else {
            if (endPos.getRow() > startPos.getRow()) {
                if (this.player.getName().equals("lower")) {
                    retList.add(endPos);
                    return retList;
                } else {
                    throw new IllegalArgumentException("Illegal move");
                }
            }

            else {
                if (this.player.getName().equals("UPPER"))
                    throw new IllegalArgumentException("Illegal move");
                else {
                    retList.add(endPos);
                    return retList;
                }
            }
        }
        return retList;

    }
}




//        List<Location> retList = new ArrayList<Location>();
//        if (Math.abs(startPos.getCol() - endPos.getCol()) == 1 && Math.abs(startPos.getRow() - endPos.getRow()) == 1) {
//            retList.add(endPos);
//        }
//        else {
//            if (endPos.getRow() - startPos.getRow() == 1) {
//                if (endPos.getCol() - startPos.getCol() == 0) {
//                    retList.add(endPos);
//                }
//            }
//        }
//        return retList;


class GoldGeneral extends Piece {
    public GoldGeneral(Player player) {
        this.name = "G";
        this.player = player;
    }

    public void promoteMovement() {



        this.promoteName();
    }
   public List<Location> findValidPath(Location startPos, Location endPos) throws IllegalArgumentException{
//        check if end position is greater than one unit away from start
        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if(columnDiff > 1 || rowDiff > 1)
            throw new IllegalArgumentException("Illegal move");

        int multiplier = 1;
        if(this.player.getName().equals("UPPER"))
            multiplier = -1;

        if(columnDiff != 0){
            if((multiplier * (endPos.getRow() - startPos.getRow())) < 0){
                throw new IllegalArgumentException("Illegal move");
            }
            else{
                List<Location> retList = new ArrayList<Location>();
                retList.add(endPos);
            }
        }




        return new ArrayList<Location>();
    }
}
