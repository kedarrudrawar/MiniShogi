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

 public boolean dropPiece(){
    return false;
 }


 public void printCaptured(){
     System.out.println(this.captured.toString());
 }

 public int getTurnCount(){
     return this.turnCounter;
 }

 public void capture(Piece piece){
     piece.capture();
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