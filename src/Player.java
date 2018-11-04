import java.*;
import java.util.*;

public class Player{
    private String name;
    private int turnCounter = 0;
    private Map<String, Piece> captured;


 public Player(String name){
     this.name = name;
     this.turnCounter = 0;
     this.captured = new HashMap<String, Piece>();
 }



// GETTER methods


 public int getTurnCount(){ return this.turnCounter; }

 public Map<String, Piece> getCaptured(){ return this.captured; }

 public String getName(){ return this.name; }




 public boolean dropPiece(){
    return false;
 }


 public void printCaptured(){
     System.out.println(this.captured.toString());
 }



 public void capture(Piece piece){
     this.captured.put(piece.getName(), piece);
     this.incrementTurn();
 }

 public void move(Piece piece, String location) {
//     piece.move(location);

     this.incrementTurn();

 }

 public void incrementTurn(){
     this.turnCounter += 1;
 }


}