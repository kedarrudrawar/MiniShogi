import java.*;
import java.util.*;

public class Player{
    private String name;
    private int turnCounter = 0;
    private Map<String, Piece> captured;
    private Map<String, Piece> onBoard;


 public Player(String name){
     this.name = name;
     this.turnCounter = 0;
     this.captured = new HashMap<String, Piece>();
     this.onBoard = new HashMap<String, Piece>();
 }



// GETTER methods
 public String getName(){ return this.name; }

 public int getTurnCount(){ return this.turnCounter; }

 public Map<String, Piece> getCaptured(){ return this.captured; }

 public Map<String, Piece> getOnBoard(){ return this.onBoard; }

 public boolean isUpper(){
     if(this.getName().equals("UPPER"))
         return true;
     else
         return false;
 }
// SETTER methods

 public void moveToBoard(Piece piece){
     this.getCaptured().remove(piece.getName());
     this.onBoard.put(piece.getName(), piece);
 }

 public void addToBoard(Piece piece){
     this.onBoard.put(piece.getName(), piece);
 }


 public void printCaptured(){
     System.out.println(this.captured.toString());
 }


 public void capture(Piece piece){
     this.captured.put(piece.getName(), piece);
     this.onBoard.remove(piece.getName());
     this.incrementTurn();
 }


 public void incrementTurn(){
     this.turnCounter += 1;
 }


@Override
 public String toString() {
    return this.getName();
 }



}