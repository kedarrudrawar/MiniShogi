import java.*;
import java.util.*;

public class Player{
    public String name;
    public int turnCounter = 0;
    List<Piece> captured;


 public Player(String name){
     this.name = name;
     this.turnCounter = 0;
     this.captured = new ArrayList<Piece>();
 }



// GETTER methods


 public int getTurnCount(){ return this.turnCounter; }

 public List<Piece> getCaptured(){ return this.captured; }

 public String getName(){ return this.name; }




 public boolean dropPiece(){
    return false;
 }


 public void printCaptured(){
     System.out.println(this.captured.toString());
 }



 public void capture(Piece piece){
     this.captured.add(piece);
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