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
        return this.getName();
    }

    public String getName() {
        if (this.player.isUpper())
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
        this.setName("+" + this.name);
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
        if(player.isUpper()){
            this.setName(this.getName().toUpperCase());
        }
        else{
            this.setName(this.getName().toLowerCase());
        }
    }

    public void setLocation(Location loc){
        this.currPos = loc;
    }

    public void setName(String name){
        this.name = name;
    }


    //    Abstract methods
    abstract List<Location> findValidPath(Location startLoc, Location endLoc);
    abstract List<Location> getValidMoves(Location pos);
    abstract boolean canDrop(Location dropLoc);
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

    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<Location>();
        if(startLoc.equals(endLoc))
            return retList;

        if (this.isPromoted()) {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            if(validPromotedMoves.size() != 0){
                return validPromotedMoves;
            }
        }

        int colDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());
        if (colDiff != rowDiff) {
            return retList;
        }

        int startCol = startLoc.getCol();
        int startRow = startLoc.getRow();

        int endCol = endLoc.getCol();

        int colAdder = 1;
        int rowAdder = 1;
        if(startLoc.getCol() > endLoc.getCol())
            colAdder= -1;
        if(startLoc.getRow() > endLoc.getRow())
            rowAdder = -1;

        while(startCol != endCol){
            startCol += colAdder;
            startRow += rowAdder;
            retList.add(new Location(startCol, startRow));
        }
        return retList;
    }


    public boolean canDrop(Location dropLoc){
        return true;
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

    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            for (Location l : validPromotedMoves) {
                retList.add(l);
            }
        }

        if (startLoc.getCol() != endLoc.getCol()) {
            if (startLoc.getRow() != endLoc.getRow()) {
                return retList;
            }
        }


//        Check if movement is horizontal or vertical:

        boolean horizontal = false;
        if (startLoc.getRow() == endLoc.getRow())
            horizontal = true;


        /**
         * TODO: Maybe modularize the code below and create a method?
         */

        int adder = 1;

        if(horizontal){
            int startCol = startLoc.getCol();
            int endCol = endLoc.getCol();
            if(startCol > endCol)
                adder = -1;

            while(startCol != endCol){
                startCol += adder;
                retList.add(new Location(startCol, startLoc.getRow()));
            }
        }
        else{
            int startRow = startLoc.getRow();
            int endRow = endLoc.getRow();
            if(startRow > endRow)
                adder = -1;
            while(startRow != endRow){
                startRow += adder;
                retList.add(new Location(startLoc.getCol(), startRow));
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
    public boolean canDrop(Location dropLoc){
        return true;
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


    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();

//        Check if promoted:
        if (this.isPromoted()) {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            return validPromotedMoves;
        }

        if (endLoc.getCol() != startLoc.getCol()) {
            return retList;
        }
        if (Math.abs(endLoc.getRow() - startLoc.getRow()) == 1)
            retList.add(endLoc);
        return retList;
    }

    public boolean canDrop(Location dropLoc){
        if(this.getPlayer().isUpper()){
            if(dropLoc.getRow() == 0){
                return false;
            }
        }
        else{
            if(dropLoc.getRow() == 4)
                return false;
        }
        return true;

    }

//    @Override
//    public void setLocation(Location loc){
//        this.currPos = loc;
//        if(this.getPlayer().isUpper()) {
//            if(loc.getRow() == 0)
//                this.promote();
//        }
//        else{
//            if(loc.getRow() == 4)
//                this.promote();
//        }
//    }

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

    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<Location>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();
        if (Math.abs(startLoc.getCol() - endLoc.getCol()) <= 1 && Math.abs(startLoc.getRow() - endLoc.getRow()) <= 1)
            retList.add(endLoc);

        return retList;

    }

    public boolean canDrop(Location dropLoc) {
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
                possibleMoves.add(new Location(i,j));
            }
        }


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

    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();

        if(startLoc == null)
            System.out.println("STARTLOC = null");
        if(endLoc == null)
            System.out.println("endLoc = null");

        if(startLoc.equals(endLoc))
            return retList;
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            return validPromotedMoves;
        }

        int columnDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());

//        if movement is more than 1 unit, move is illegal
        if (columnDiff > 1 || rowDiff > 1)
            return retList;

//        this indicates diagonal movement, which is okay in any direction
        if (columnDiff != 0) {
            if (rowDiff != 0) {
                retList.add(endLoc);
                return retList;
            }
        }

        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        if (multiplier * (endLoc.getRow() - startLoc.getRow()) > 0)
            retList.add(endLoc);

        return retList;

    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }


    public boolean canDrop(Location dropLoc){
        return true;
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
    public void promote(){
        System.out.println("Gold General cannot be promoted. Illegal Move");
        System.exit(0);
    }

    public List<Location> findValidPath(Location startLoc, Location endLoc){
        List<Location> retList = new ArrayList<Location>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();

//        check if end position is greater than one unit away from start
        int columnDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());
        if (columnDiff > 1 || rowDiff > 1) {
            return retList;
        }

//        if player is UPPER, their piece's forward movement is vertically negative, so we will use a negative multipler
//          to identify validity of the move
        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        if (columnDiff != 0) {
            if ((multiplier * (endLoc.getRow() - startLoc.getRow())) >= 0) {
                retList.add(endLoc);
            }
        }
        else{
            retList.add(endLoc);
        }
        return retList;
    }
    public boolean canDrop(Location dropLoc){
        return true;
    }
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}
