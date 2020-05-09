package com.MiniShogi.game;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.*;

/**
 * Abstract class Piece. Represents all Pieces used to play miniShogi.
 * Maintain and implements movement, promotion, and drop functionality of every piece.
 * @author Kedar Rudrawar
 */
public abstract class Piece {
    static final char PLUS_CHAR = '+';
    static final String PLUS_STRING = "+";
    private static final String PLUS_REGEX = "\\+";

    @JsonBackReference
    Player player;
    String name;
    Piece promotionPiece;
    Location currLoc;

    /**
     * This method returns a Piece object as a String. It returns the name of a Piece.
     * @return a String - name
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * This method returns the name of a Piece object
     * @return a String - name
     */
    public String getName() {
        if (this.player.isUpper())
            return this.name;
        else
            return this.name.toLowerCase();
    }

    /* GETTER methods */

    /**
     * This method returns the owner Player of a Piece
     * @return a Player - owner
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * This method returns the Location of a Piece (null if captured)
     * @return a Location
     */
    public Location getLocation(){
        return this.currLoc;
    }

    /* SETTER methods */

    /**
     * This method promotes a Piece (adds a '+' in front of its name)
     * If already promoted, returns false
     * @return a boolean - true if successfully promoted, false otherwise
     */
    public boolean promote() {
        if(this.getName().charAt(0) == PLUS_CHAR) {
            return false;
        }
        this.setName(PLUS_STRING + this.name);
        return true;
    }

    /**
     * This method is used to forcePromote a piece, even if its not in the right location.
     * This is used to promote a piece when creating Piece objects in File mode at specific locations.
     * @return void
     */
    public void forcePromote(){
        this.setName(PLUS_STRING + this.name);
    }

    /**
     * This method is used to demote a Piece object.
     * @return void
     */
    public void demote(){
        this.name = this.name.split(PLUS_REGEX)[1];
    }

    /**
     * This method returns whether a Piece is promoted.
     * @return a boolean - true if promoted, false otherwise
     */
    public boolean isPromoted(){
        if(this.name.charAt(0) == PLUS_CHAR)
            return true;
        return false;
    }

    /**
     * This method sets the owner of a Piece to an inputted Player
     * @param newPlayer Player owner
     * @return void
     */
    public void setPlayer(Player newPlayer){
        this.player = newPlayer;
        if(player.isUpper()){
            this.setName(this.getName().toUpperCase());
        }
        else{
            this.setName(this.getName().toLowerCase());
        }
    }

    /**
     * This method sets the Location of a piece to an inputted Location.
     * @param loc   Location object
     * @return void
     */
    public void setLocation(Location loc){
        this.currLoc = loc;
    }

    /**
     * This method sets the name field of a Piece to a String name
     * @param name  String name
     * @return void
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * This method returns whether it is legal for a piece to be dropped in this Location.
     * (Mainly created for Pawn class, maintains abstraction of the Piece).
     * @param dropLoc   Location to be dropped
     * @return a boolean - true if can be dropped, false otherwise
     */
    public boolean canDrop(Location dropLoc){
        return true;
    }


    /* Abstract methods */

    /**
     * This method returns a List of Locations, which form a valid path for a Piece to move from a startLoc to endLoc.
     * If the path is invalid, the returned List is empty.
     * @param startLoc  Location start position
     * @param endLoc    Location end position
     * @return  List of Locations - paths
     */
    abstract List<Location> findValidPath(Location startLoc, Location endLoc);

    /**
     * This method returns a List of Locations that a Piece can move to in general given a position Location.
     * This method does not take account of obstructions.
     * (Mainly created for King class, maintains abstraction of the Piece).
     * @param pos   Location position
     * @return  List of Location - validMoves
     */
    abstract List<Location> getValidMoves(Location pos);

}

/**
 * This class initializes a Bishop object on the board. Enables functionality of checking requested movement.
 */
class Bishop extends Piece {
    /**
     * Constructor - Generates Bishop object, sets promotable piece to King object, initializes name and Player
     * @param player    Player owner
     */
    public Bishop(Player player){
        this.name = "B";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    /**
     * Constructor - Generates Bishop object, sets promotable piece to King object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     * @param player    Player owner of Bishop object
     * @param pos   Location of the Bishop on the board
     */
    public Bishop(Player player, Location pos) {
        this.name = "B";
        this.player = player;
        this.promotionPiece = new King(player);
        this.currLoc = pos;
    }

    /**
     * This method returns the valid positions a bishop can go through from its start to end position.
     * If not promoted, can move diagonally in any direction
     * If promoted, absorb functionality of King movement
     * @param startLoc  Location start position
     * @param endLoc    Location end position
     * @return  List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<Location>();
        int colDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());

        int startCol = startLoc.getCol();
        int startRow = startLoc.getRow();
        int endCol = endLoc.getCol();

        // source and destination are the same, Illegal move
        if(startLoc.equals(endLoc))
            return retList;

        if (this.isPromoted()) {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            if(validPromotedMoves.size() != 0){
                return validPromotedMoves;
            }
        }


        if (colDiff != rowDiff)
            return retList;

        // Check whether the bishop is moving up or down, left or right (up/right are +, left/down are -)
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

    /**
     * This method returns list of Locations Bishop can move to.
     * (Incomplete, because used only for King)
     * @param pos   Location position
     * @return  List of Locations
     */
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}

/**
 * This class initializes a Rook object on the board. Enables functionality of checking requested movement.
 */
class Rook extends Piece {
    /**
     * Constructor - Generates Rook object, sets promotable piece to King object, initializes name and Player
     * @param player    Player owner
     */
    public Rook(Player player){
        this.name = "R";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    /**
     * Constructor - Generates Rook object, sets promotable piece to King object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     * @param player    Player owner of Rook object
     * @param pos   Location of the Rook on the board
     */
    public Rook(Player player, Location pos) {
        this.name = "R";
        this.player = player;
        this.promotionPiece = new King(player);
        this.currLoc = pos;
    }

    /**
     * This method returns the valid positions a rook can go through from its start to end position.
     * If not promoted, can move vertically OR horizontally in any direction
     * If promoted, absorb functionality of King movement
     * @param startLoc  Location start position
     * @param endLoc    Location end position
     * @return  List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == PLUS_CHAR) {
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

        return retList;
    }

    /**
     * This method returns list of Locations Rook can move to.
     * (Incomplete, because used only for King)
     * @param pos   Location position
     * @return a List of Locations
     */
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}

/**
 * This class initializes a Pawn object on the board. Enables functionality of checking requested movement.
 * Also checks droppability.
 */
class Pawn extends Piece {
    /**
     * Constructor - Generates  Pawn object, sets promotable piece to King object, initializes name and Player
     * @param player    Player owner
     */
    public Pawn(Player player) {
        this.name = "P";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
    }

    /**
     * Constructor - Generates Pawn object, sets promotable piece to King object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     * @param player    Player owner of Pawn object
     * @param pos   Location of the Pawn on the board
     */
    public Pawn(Player player, Location pos){
        this.name = "P";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
        this.currLoc = pos;
    }

    /**
     * This method returns the valid positions a Pawn can go through from its start to end position.
     * If not promoted, can move one unit forward
     * If promoted, absorb functionality of King movement
     * @param startLoc  Location start position
     * @param endLoc    Location end position
     * @return  List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();
        if(startLoc.equals(endLoc))
            return new ArrayList<>();

        // Check if promoted:
        if (this.isPromoted()) {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            return validPromotedMoves;
        }

        // Indicates horizontal movement, Illegal
        if (endLoc.getCol() != startLoc.getCol()) {
            return retList;
        }

        // Check for one unit movement forward
        if (Math.abs(endLoc.getRow() - startLoc.getRow()) == 1)
            retList.add(endLoc);
        return retList;
    }

    /**
     * This method checks whether for illegal  promotions (prevents accidental double promotions)
     * If move was "move a4 a5 promote", super method would cause double promotion and end
     * @return a boolean - true if promoted
     */
    @Override
    public boolean promote(){
        if(this.getPlayer().isUpper()){
            if(this.getLocation().getRow() == 0) {
                if(this.getName().charAt(0) != PLUS_CHAR)
                    this.setName(PLUS_STRING + this.name);
                return true;
            }
        }
        else{
            if(this.getLocation().getRow() == 4){
                if(this.getName().charAt(0) != PLUS_CHAR)
                    this.setName(PLUS_STRING + this.name);
                return true;
            }
        }
        return false;
    }


    /**
     * This method returns whether it is legal for a Pawn to be dropped in this Location.
     * Pawns cannot be dropped in promotion zone
     * @param dropLoc   Location to be dropped
     * @return a boolean - true if can be dropped, false otherwise
     */
    @Override
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

    /**
     * This method sets the Location of a Pawn object. If the Location is in the promotion zone, it automatically
     * promotes the Pawn.
     * @param loc   Location object
     */
    @Override
    public void setLocation(Location loc){
        this.currLoc = loc;
        if(loc != null) {
            if (this.getPlayer().isUpper()) {
                if (loc.getRow() == 0)
                    this.promote();
            } else {
                if (loc.getRow() == 4)
                    this.promote();
            }
        }
    }

    /**
     * This method returns list of Locations Pawn can move to.
     * (Incomplete, because used only for King)
     * @param pos   Location position
     * @return a List of Locations
     */
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}

/**
 * This class initializes a King object on the board. Enables functionality of checking requested movement.
 */
class King extends Piece {

    /**
     * Constructor - Generates King object, initializes name and Player
     * @param player    Player owner
     */
    public King(Player player) {
        this.name = "K";
        this.player = player;
        this.promotionPiece = null;
    }

    /**
     * Constructor - Generates King object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     * @param player    Player owner of Pawn object
     * @param pos   Location of the Pawn on the board
     */
    public King(Player player, Location pos) {
        this.name = "K";
        this.player = player;
        this.promotionPiece = null;
        this.currLoc = pos;
    }

    /**
     * This method returns false, because a King cannot be promoted. Other pieces can be.
     * @return a boolean - false
     */
    @Override
    public boolean promote(){
        return false;
    }

    /**
     * This method returns the valid positions a King can go through from its start to end position.
     * A King can move one unit in any direction.
     * @param startLoc  Location start position
     * @param endLoc    Location end position
     * @return  List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<Location>();

        // Check for exactly one unit of movement
        if (Math.abs(startLoc.getCol() - endLoc.getCol()) <= 1 && Math.abs(startLoc.getRow() - endLoc.getRow()) <= 1)
            retList.add(endLoc);

        return retList;
    }

    /**
     * This method returns whether a King can be dropped at a specific Location.
     * A King cannot be captured, so it cannot be dropped. Return false.
     * @param dropLoc   Location to be dropped
     * @return a boolean - false
     */
    public boolean canDrop(Location dropLoc) {
        return false;
    }

    /**
     * This method returns a List of Locations that a King can move to in general given a position Location.
     * This method does not take account of obstructions.
     * @param pos   Location position
     * @return  List of Location - validMoves
     */
    public List<Location> getValidMoves(Location pos) {
        int row = pos.getRow();
        int col = pos.getCol();
        List<Location> possibleMoves = new ArrayList<>();
        // Use -1 to include the Location to the left, bottom, and bottom-left
        // Use +2 to include the Location to the right, top, and top-right
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

/**
 * This class initializes a SilverGeneral object on the board. Enables functionality of checking requested movement.
 */
class SilverGeneral extends Piece {

    /**
     * Constructor - Generates SilverGeneral object, sets promotable piece to GoldGeneral object, initializes name and Player
     *
     * @param player Player owner
     */
    public SilverGeneral(Player player) {
        this.name = "S";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
    }

    /**
     * Constructor - Generates SilverGeneral object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     *
     * @param player Player owner of Pawn object
     * @param pos    Location of the Pawn on the board
     */
    public SilverGeneral(Player player, Location pos) {
        this.name = "S";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
        this.currLoc = pos;
    }

    /**
     * This method returns the valid positions a SilverGeneral can go through from its start to end position.
     * A SilverGeneral can move one unit in any diagonal direction, or one unit forward.
     *
     * @param startLoc Location start position
     * @param endLoc   Location end position
     * @return List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc) {
        List<Location> retList = new ArrayList<>();

        if (startLoc == null)
            System.out.println("STARTLOC = null");
        if (endLoc == null)
            System.out.println("endLoc = null");

        if (startLoc.equals(endLoc))
            return retList;
        // If promoted, replace movement functionality with Gold General's
        if (this.isPromoted()) {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startLoc, endLoc);
            return validPromotedMoves;
        }

        int columnDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());

        // Check if movement is 1 unit
        if (columnDiff > 1 || rowDiff > 1)
            return retList;

        // Check for diagonal movement, legal in any direction
        if (columnDiff != 0) {
            if (rowDiff != 0) {
                retList.add(endLoc);
                return retList;
            }
        }

        // Cannot move backwards (negative for lower piece, positive for upper piece)
        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        if (multiplier * (endLoc.getRow() - startLoc.getRow()) > 0)
            retList.add(endLoc);

        return retList;
    }

    /**
     * This method returns list of Locations SilverGeneral can move to.
     * (Incomplete, because used only for King)
     * @param pos Location position
     * @return List of Location - validMoves
     */
    public List<Location> getValidMoves(Location pos) {
        return new ArrayList<Location>();
    }
}

/**
 * This class initializes a GoldGeneral object on the board. Enables functionality of checking requested movement.
 */
class GoldGeneral extends Piece {

    /**
     * Constructor - Generates GoldGeneral object, initializes name and Player
     * @param player    Player owner
     */
    public GoldGeneral(Player player) {
        this.name = "G";
        this.player = player;
        this.promotionPiece = null;
    }

    /**
     * Constructor - Generates SilverGeneral object, initializes name, Player, Location
     * This is used in file mode, when initializing the Piece at a certain location
     *
     * @param player Player owner of Pawn object
     * @param pos    Location of the Pawn on the board
     */
    public GoldGeneral(Player player, Location pos) {
        this.name = "G";
        this.player = player;
        this.promotionPiece = null;
        this.currLoc = pos;
    }

    /**
     * This method returns false, because a GoldGeneral cannot be promoted. Other pieces can be.
     * @return a boolean - false
     */
    @Override
    public boolean promote(){
        return false;
    }

    /**
     * This method returns the valid positions a GoldGeneral can go through from its start to end position.
     * A GoldGeneral can move one unit in any direction, except backwards diagonals.
     *
     * @param startLoc Location start position
     * @param endLoc   Location end position
     * @return List of Locations - path
     */
    public List<Location> findValidPath(Location startLoc, Location endLoc){
        List<Location> retList = new ArrayList<Location>();
        int columnDiff = Math.abs(startLoc.getCol() - endLoc.getCol());
        int rowDiff = Math.abs(startLoc.getRow() - endLoc.getRow());

        if(startLoc.equals(endLoc))
            return retList;

        // check if end position is greater than one unit away from start
        if (columnDiff > 1 || rowDiff > 1)
            return retList;

        // Cannot move to backwards diagonal (negative for lower piece, positive for upper piece)
        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        // Only applies if there is horizontal AND vertical displacement
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

    /**
     * This method returns list of Locations GoldGeneral can move to.
     * (Incomplete, because used only for King)
     * @param pos Location position
     * @return List of Location - validMoves
     */
    public List<Location> getValidMoves(Location pos){
        return new ArrayList<Location>();
    }
}
