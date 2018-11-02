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

//    helper method to convert position string to actual indices (a2 --> [0][1])
    public int[] getLocation(String location){
        int row = location.charAt(0) - 97;
        int col = Character.getNumericValue(location.charAt(1)) - 1;
        int[] pos = {row, col};
        return pos;
    }

    public Piece[][] getBoard(){
        return this.board;
    }

    public boolean move(String startPos, String endPos) throws IllegalArgumentException{
        int[] startPosArr = getLocation(startPos);
        int[] endPosArr = getLocation(endPos);

        if(this.board[endPosArr[0]][endPosArr[1]] != null){
            throw new IllegalArgumentException("Desired position is occupied.");
        }

        Piece piece = this.board[startPosArr[0]][startPosArr[1]];
        System.out.println(startPosArr[0] + "," + startPosArr[1]);
        System.out.println(piece.getName());

        if(piece != null){
            if(piece.isValidMove(startPosArr, endPosArr)){
//                System.out.println("entered");
                this.board[endPosArr[0]][endPosArr[1]] = piece;
                this.board[startPosArr[0]][startPosArr[1]] = null;
            }
            String b = Utils.stringifyBoard(this.board);
            System.out.println(b);

            return true;
        }






        return false;
    }

    public static void main(String[] args){
//        Initialize board
        Board board = new Board();
        Piece[][] boardArray = board.getBoard();

//        Initialize players
        Player lower = boardArray[4][0].getPlayer();
        Player UPPER = boardArray[0][4].getPlayer();

        try {
            board.move("a1", "a2");
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }

        System.out.println(Utils.stringifyBoard(board.getBoard()));


    }
}