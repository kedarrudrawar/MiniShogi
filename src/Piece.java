import java.util.*;

public abstract class Piece {
    //    Fields
    protected Player player;
    protected String name;
    protected Piece promotionPiece;
    protected Location currPos;


    //    Defined methods
    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        if (this.player.getName().equals("UPPER"))
            return this.name;
        else
            return this.name.toLowerCase();

    }


//    Getter methods

    /**
     *
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }

    public Location getLocation(){
        return this.currPos;
    }



//    Setter methods

    public void promote() {
        this.name = "+" + this.name;
    }

    public void demote(){
        this.name = this.name.split("\\+")[1];
    }

    public boolean isPromoted(){
        if(this.name.charAt(0) == '+')
            return true;
        return false;
    }


    public void setPlayer(Player newPlayer){
        this.player = newPlayer;
    }

    public void setLocation(Location pos){
        this.currPos = pos;
    }

    //    Abstract methods
    abstract List<Location> findValidPath(Location startPos, Location endPos);
    abstract List<Location> getValidMoves(Location pos);
    abstract boolean canDrop(Location dropPos);
}


class Bishop extends Piece {
    public Bishop(Player player){
        this.name = "B";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    public Bishop(Player player, Location pos) {
        this.name = "B";
        this.player = player;
        this.promotionPiece = new King(player);
        this.currPos = pos;
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<Location>();

        int colDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if (colDiff != rowDiff) {
            return retList;
        }

        int startCol = startPos.getCol();
        int startRow = startPos.getRow();

        int endCol = endPos.getCol();


        int colAdder = 1;
        int rowAdder = 1;
        if(startPos.getCol() > endPos.getCol())
            colAdder= -1;
        if(startPos.getRow() > endPos.getRow())
            rowAdder = -1;


        while(startCol != endCol){
            startCol += colAdder;
            startRow += rowAdder;
            retList.add(new Location(startCol, startRow));
        }


        for(Location loc : retList){
            System.out.println(loc.getCol() + " , " + loc.getRow());
        }

        return retList;
    }


    public boolean canDrop(Location dropPos){
        return false;
    }


    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}


class Rook extends Piece {
    public Rook(Player player){
        this.name = "R";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    public Rook(Player player, Location pos) {
        this.name = "R";
        this.player = player;
        this.promotionPiece = new King(player);
        this.currPos = pos;
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            for (Location l : validPromotedMoves) {
                retList.add(l);
            }
        }

        if (startPos.getCol() != endPos.getCol()) {
            if (startPos.getRow() != endPos.getRow()) {
                return retList;
            }
        }


//        Check if movement is horizontal or vertical:

        boolean horizontal = false;
        if (startPos.getRow() == endPos.getRow())
            horizontal = true;


        /**
         * TODO: Maybe modularize the code below and create a method?
         */

        int adder = 1;

        if(horizontal){
            int startCol = startPos.getCol();
            int endCol = endPos.getCol();
            if(startCol > endCol)
                adder = -1;

            while(startCol != endCol){
                startCol += adder;
                retList.add(new Location(startCol, startPos.getRow()));
            }
        }
        else{
            int startRow = startPos.getRow();
            int endRow = endPos.getRow();
            if(startRow > endRow)
                adder = -1;
            while(startRow != endRow){
                startRow += adder;
                retList.add(new Location(startPos.getCol(), startRow));
            }
        }



//        if (horizontal) {
//            int startCol = Math.min(startPos.getCol(), endPos.getCol()) + 1;
//            int endCol = Math.max(startPos.getCol(), endPos.getCol());
//
//            int row = startPos.getRow();
//
//            for (int col = startCol; col <= endCol; col++) {
//                retList.add(new Location(col, row));
//            }
//        } else {
//            int startRow = Math.min(startPos.getRow(), endPos.getRow()) + 1;
//            int endRow = Math.max(startPos.getRow(), endPos.getRow());
//
//            int col = startPos.getCol();
//
//            for (int row = startRow; row <= endRow; row++) {
//                retList.add(new Location(col, row));
//            }
//        }
        return retList;
    }
    public boolean canDrop(Location dropPos){
        return false;
    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}


class Pawn extends Piece {
    public Pawn(Player player) {
        this.name = "P";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
    }

    public Pawn(Player player, Location pos){
        this.name = "P";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
        this.currPos = pos;
    }


    public List<Location> findValidPath(Location startPos, Location endPos) {
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            return validPromotedMoves;
        }

        List<Location> retList = new ArrayList<>();

        if (endPos.getCol() != startPos.getCol()) {
            return retList;
        }
        if (Math.abs(endPos.getRow() - startPos.getRow()) == 1)
            retList.add(endPos);
        return retList;
    }
    public boolean canDrop(Location dropPos){
        return false;
    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}


class King extends Piece {
    public King(Player player) {
        this.name = "K";
        this.player = player;
        this.promotionPiece = null;
    }

    public King(Player player, Location pos) {
        this.name = "K";
        this.player = player;
        this.promotionPiece = null;
        this.currPos = pos;
    }


    @Override
    public void promote() throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot promote King. Illegal move.");
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<Location>();
        if (Math.abs(startPos.getCol() - endPos.getCol()) <= 1 && Math.abs(startPos.getRow() - endPos.getRow()) <= 1) {
            retList.add(endPos);
        }
        return retList;

    }

    public boolean canDrop(Location dropPos) {
        return false;
    }

    public List<Location> getValidMoves(Location pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        List<Location> possibleMoves = new ArrayList<>();
        for(int i = col - 1; i < col + 2; i++){
            for(int j = row - 1; j < row + 2; j++){
                if(i < 0 || i >= 5)
                    break;
                if(j < 0 || j >= 5)
                    continue;
                possibleMoves.add(new Location(j, i));
            }
        }







//        for (int i = Math.max(0, col - 1); i < Math.min(col + 2, 4); i++) {
//            for (int j = Math.max(0, row - 1); j < Math.min(row + 2, 4); j++) {
//                if(i == pos.getCol()){
//                    if(j == pos.getRow())
//                        continue;
//                }
//                possibleMoves.add(new Location(i, j));
//            }
//        }
        return possibleMoves;

    }
}



class SilverGeneral extends Piece {
    public SilverGeneral(Player player) {
        this.name = "S";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
    }

    public SilverGeneral(Player player, Location pos){
        this.name = "S";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
        this.currPos = pos;
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            return validPromotedMoves;
        }

        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());

//        if movement is more than 1 unit, move is illegal
        if (columnDiff > 1 || rowDiff > 1)
            return retList;

//        this indicates diagonal movement, which is okay in any direction
        if (columnDiff != 0) {
            if (rowDiff != 0) {
                retList.add(endPos);
                return retList;
            }
        }

        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        if (multiplier * (endPos.getRow() - startPos.getRow()) > 0)
            retList.add(endPos);

        return retList;

    }
    public boolean canDrop(Location dropPos){
        return false;
    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}

class GoldGeneral extends Piece {
    public GoldGeneral(Player player) {
        this.name = "G";
        this.player = player;
        this.promotionPiece = null;
    }

    public GoldGeneral(Player player, Location pos) {
        this.name = "G";
        this.player = player;
        this.promotionPiece = null;
        this.currPos = pos;
    }




    @Override
    public void promote() throws IllegalArgumentException {
        throw new IllegalArgumentException("Gold General cannot be promoted. Illegal Move");
    }

    public List<Location> findValidPath(Location startPos, Location endPos) throws IllegalArgumentException {
        List<Location> retList = new ArrayList<Location>();
//        check if end position is greater than one unit away from start
        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if (columnDiff > 1 || rowDiff > 1)
            return retList;

//        if player is UPPER, their piece's forward movement is vertically negative, so we will use a negative multipler
//          to identify validity of the move
        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;


        if (columnDiff != 0) {
            if ((multiplier * (endPos.getRow() - startPos.getRow())) > 0)
                retList.add(endPos);
        }
        return retList;
    }
    public boolean canDrop(Location dropPos){
        return false;
    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}
