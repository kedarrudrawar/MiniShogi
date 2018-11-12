import java.*;
import java.util.*;

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

    // GETTER methods

    /**
     * Getter for name of Player
     * @return String - name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for number of turns Player has played
     * @return int - number of turns
     */
    public int getTurnCount() {
        return this.turnCounter;
    }

    /**
     * Getter for list of captured Pieces
     * @return List - Captured Pieces
     */
    public List<Piece> getCaptured() {
        return this.capturedList;
    }

    /**
     * Getter for list of Pieces on the board
     * @return List - Pieces on board
     */
    public List<Piece> getOnBoard() {
        return this.onBoardList;
    }

    /**
     * Getter for King Piece
     * @return Piece - King
     */
    public Piece getKing() {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase("k")) {
                return piece;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj){
        Player p = (Player) obj;
        if(this.getName().equals(p.getName())){
            return true;
        }
        return false;
    }

    public Piece getPieceFromBoard(String pieceName) {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase(pieceName)) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPieceFromCaptured(String pieceName) {
        for (Piece piece : this.capturedList) {
            if (piece.getName().equalsIgnoreCase(pieceName)) {
                return piece;
            }
        }
        return null;
    }

    public int getIndexFromCaptured(String pieceName){
        for(int i = 0; i < this.capturedList.size(); i++){
            if(this.capturedList.get(i).getName().equalsIgnoreCase(pieceName)){
                return i;
            }
        }
        return 0;
    }

    public boolean isUpper() {
        if (this.getName().equals("UPPER"))
            return true;
        else
            return false;
    }

// SETTER methods

    public void moveToBoardList(Piece piece) {
        this.capturedList.remove(piece);
        this.onBoardList.add(piece);
    }

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

    public void addToCapturedList(Piece piece) {
        this.capturedList.add(piece);
        piece.setPlayer(this);
    }

    public void addToCapturedList(int index, Piece piece) {
        this.capturedList.add(index, piece);
        piece.setPlayer(this);
    }

    public void removeFromBoard(Piece piece) {
        this.onBoardList.remove(piece);
    }

    public void removeFromCaptured(Piece piece){
        this.capturedList.remove(piece);
    }

    public void incrementTurn() {
        this.turnCounter += 1;
    }

    @Override
    public String toString() {
        return this.getName();
    }


}