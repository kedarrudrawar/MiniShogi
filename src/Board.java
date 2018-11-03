import javafx.util.*;
import java.util.*;


public class Board{
    public Piece[][] board;




    public Board(){
        this.board = initializeBoard();
    }




    public static Piece[][] initializeBoard(){
        Piece[][] board = new Piece[5][5];
        board[0][4] = new Rook(new Player("UPPER"));
        board[4][0] = new Rook(new Player("lower"));


        board[1][4] = new Bishop(new Player("UPPER"));
        board[3][0] = new Bishop(new Player("lower"));

        board[2][4] = new SilverGeneral(new Player("UPPER"));
        board[2][0] = new SilverGeneral(new Player("lower"));

        board[3][4] = new GoldGeneral(new Player("UPPER"));
        board[1][0] = new GoldGeneral(new Player("lower"));

        board[4][4] = new King(new Player("UPPER"));
        board[0][0] = new King(new Player("lower"));

        board[4][3] = new Pawn(new Player("UPPER"));
        board[0][1] = new Pawn(new Player("lower"));


        String b = Utils.stringifyBoard(board);
        System.out.println(b);
        return board;
    }



// GETTER methods


//    helper method to convert position string to index array (a2 --> [0][1])
    public int[] getLocation(String location){
        int row = location.charAt(0) - 97;
        int col = Character.getNumericValue(location.charAt(1)) - 1;
        int[] pos = {row, col};
        return pos;
    }



    public Piece[][] getBoard(){
        return this.board;
    }




// ACTION methods


    public boolean capture(int[] startPosArr, int[] endPosArr){
        Piece captor = this.board[startPosArr[0]][startPosArr[1]];
        Piece captured = this.board[endPosArr[0]][endPosArr[1]];
        Player captorPlayer = captor.getPlayer();
        captorPlayer.capture(captured);
        this.board[endPosArr[0]][endPosArr[1]] = captor;

        return true;
    }



    public boolean move(String startPos, String endPos) throws IllegalArgumentException{
        int[] startPosArr = getLocation(startPos);
        int[] endPosArr = getLocation(endPos);

        Piece piece = this.board[startPosArr[0]][startPosArr[1]];

//        check if target position is empty
        if(this.board[endPosArr[0]][endPosArr[1]] != null){
//            if not empty, check if occupied by opponent's piece or own piece
            if (this.board[endPosArr[0]][endPosArr[1]].getPlayer() == this.board[startPosArr[0]][startPosArr[1]].getPlayer()) {
                System.out.println(this.board[endPosArr[0]][endPosArr[1]]);
                throw new IllegalArgumentException("Desired position is occupied by your player.");
            }
            else{
                if(piece.isValidMove(startPosArr, endPosArr)) {
                    this.capture(startPosArr, endPosArr);
                }
            }
        }

        if(piece != null){
            if(piece.isValidMove(startPosArr, endPosArr)){
                this.board[endPosArr[0]][endPosArr[1]] = piece;
                this.board[startPosArr[0]][startPosArr[1]] = null;
            }
            else{
                throw new IllegalArgumentException("Illegal move attempted with " + piece.getName() + "."
                        + System.getProperty("line.separator"));
            }
            piece.getPlayer().incrementTurn();
            return true;
        }
        return false;
    }


    public void drop(Player player, String pieceName, String position) throws IllegalArgumentException{

        int[] posArr = this.getLocation(position);

        if(this.board[posArr[0]][posArr[1]] != null){
            throw new IllegalArgumentException("Desired position to drop piece is currently occupied by: "
                    + this.board[posArr[0]][posArr[1]].getName());
        }

        for(Piece p : player.getCaptured()){
            if(p.getName().equals(pieceName)){
                player.getCaptured().remove(p);
            }
        }



    }

    public static void main(String[] args){
//        Initialize board
        Board board = new Board();
        Piece[][] boardArray = board.getBoard();

//        Initialize players
        Player lower = boardArray[4][0].getPlayer();
        Player UPPER = boardArray[0][4].getPlayer();

        List<String[]> moves = new ArrayList<String[]>();

        String[] m1 = {"e4", "e3"};
        moves.add(m1);

        String[] m2 = {"e1", "e3"};
        moves.add(m2);
//        String[] m3 = {"d3", "d2"};
//        moves.add(m3);
//        String[] m4 = {"c1", "c2"};
//        moves.add(m1);



        for(String[] move : moves) {
            try {
                System.out.println("moving from " + move[0] + " to " + move[1]);
                board.move(move[0], move[1]);
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }

            System.out.println(Utils.stringifyBoard(board.getBoard()));
            System.out.print("Lower's captured: ");
            for(Piece p : lower.getCaptured()){
                System.out.print(p.getName());
            }
            System.out.println();

            System.out.print("UPPER'S captured: ");
            for(Piece p : UPPER.getCaptured()){
                System.out.print(p.getName());
            }
            System.out.println();

        }







    }
}