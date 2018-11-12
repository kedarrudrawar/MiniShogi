import java.*;
import java.util.*;

/**
 * Player Class
 * Stores information regarding players: UPPER & lower.
 *
 * @author Kedar Rudrawar
 */
public class Player {
    private String name;
    private int turnCounter;
    private List<Piece> capturedList;
    private List<Piece> onBoardList;

    /**
     * Constructor - generate Player object
     * @param name  String representing player name (UPPER / lower)
     */
    public Player(String name) {
        this.name = name;
        this.turnCounter = 0;
        this.capturedList = new ArrayList<>();
        this.onBoardList = new ArrayList<>();
    }

    /**
     * This method is a getter for name of Player
     * @return String - name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method is a getter for number of turns Player has played
     * @return int - number of turns
     */
    public int getTurnCount() {
        return this.turnCounter;
    }

    /**
     * This method is a getter for list of captured Pieces
     * @return a List - Captured Pieces
     */
    public List<Piece> getCaptured() {
        return this.capturedList;
    }

    /**
     * This method is a getter for list of Pieces on the board
     * @return a List - Pieces on board
     */
    public List<Piece> getOnBoard() {
        return this.onBoardList;
    }

    /**
     * This method is a getter for King Piece
     * @return a Piece - King
     */
    public Piece getKing() {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase("k")) {
                return piece;
            }
        }
        return null;
    }

    /**
     * This method returns specified piece from onBoard list, if it exists
     * @param pieceName
     * @return a Piece
     */
    public Piece getPieceFromBoard(String pieceName) {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase(pieceName)) {
                return piece;
            }
        }
        return null;
    }

    /**
     * This method returns specified piece from captured list, if it exists
     * @param pieceName
     * @return a Piece
     */
    public Piece getPieceFromCaptured(String pieceName) {
        for (Piece piece : this.capturedList) {
            if (piece.getName().equalsIgnoreCase(pieceName)) {
                return piece;
            }
        }
        return null;
    }

    /**
     * This method is a getter for the index of a specified Piece in the CapturedList.
     * This index is used to re-insert the Piece back into the list, after testing.
     * @param pieceName
     * @return an int - index
     */
    public int getIndexFromCaptured(String pieceName){
        for(int i = 0; i < this.capturedList.size(); i++){
            if(this.capturedList.get(i).getName().equalsIgnoreCase(pieceName)){
                return i;
            }
        }
        return 0;
    }

    /**
     * This method returns whether a Player is the UPPER player or not.
     * @return a boolean - true if Upper, false otherwise
     */
    public boolean isUpper() {
        if (this.getName().equals("UPPER"))
            return true;
        else
            return false;
    }

// SETTER methods

    /**
     * This method moves a Piece to the board from the captured list.
     * @param piece Piece to move to board
     * @return void
     */
    public void moveToBoardList(Piece piece) {
        this.capturedList.remove(piece);
        this.onBoardList.add(piece);
    }

    /**
     * This method adds a Piece to the board.
     * @param piece Piece to add to board
     * @return void
     */
    public void addToBoardList(Piece piece) {
        this.onBoardList.add(piece);
        if(this.isUpper()){
            piece.setName(piece.getName().toUpperCase());
        }
        else{
            piece.setName(piece.getName().toLowerCase());
        }

        piece.setPlayer(this);

    }

    /**
     * This method adds a Piece to the board.
     * @param piece Piece to add to board
     * @return void
     */
    public void addToCapturedList(Piece piece) {
        this.capturedList.add(piece);
        piece.setPlayer(this);
    }

    /**
     * This method adds a Piece to the captured list.
     * @param piece Piece to add to captured list
     * @return void
     */
    public void addToCapturedList(int index, Piece piece) {
        this.capturedList.add(index, piece);
        piece.setPlayer(this);
    }

    /**
     * This method removes a Piece from the board.
     * @param piece Piece to remove from board
     * @return void
     */
    public void removeFromBoard(Piece piece) {
        this.onBoardList.remove(piece);
    }

    /**
     * This method removes a Piece from the captured list.
     * @param piece Piece to remove
     * @return void
     */
    public void removeFromCaptured(Piece piece){
        this.capturedList.remove(piece);
    }

    /**
     * This method increments a player's turn count by 1.
     * @return void
     */
    public void incrementTurn() {
        this.turnCounter += 1;
    }

    /**
     * This method converts the Player to a String.
     * @return a String - name of Player
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * This method checks equality of two players. If they have the same name, they are 'equal'.
     * @param obj   Player to check against 'this'.
     * @return  a boolean - false if unequal, true otherwise
     */
    @Override
    public boolean equals(Object obj){
        Player p = (Player) obj;
        if(this.getName().equals(p.getName())){
            return true;
        }
        return false;
    }

}