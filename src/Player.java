import java.*;
import java.util.*;

public class Player {
    private String name;
    private int turnCounter;
    private List<Piece> capturedList;
    private List<Piece> onBoardList;

    public Player(String name) {
        this.name = name;
        this.turnCounter = 0;
        this.capturedList = new ArrayList<>();
        this.onBoardList = new ArrayList<>();
    }


    // GETTER methods
    public String getName() {
        return this.name;
    }

    public int getTurnCount() {
        return this.turnCounter;
    }

    public List<Piece> getCaptured() {
        return this.capturedList;
    }

    public List<Piece> getOnBoard() {
        return this.onBoardList;
    }

    public Piece getKing() {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase("k")) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPawn() {
        for (Piece piece : this.onBoardList) {
            if (piece.getName().equalsIgnoreCase("p")) {
                return piece;
            }
        }
        return null;
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

    public String printCaptured() {
        return this.capturedList.toString();
    }

    public String printOnBoard() {
        return this.onBoardList.toString();
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