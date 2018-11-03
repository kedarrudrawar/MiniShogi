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



    public boolean capture(Player captor, int[] endPosArr){
        Piece captured = this.board[endPosArr[0]][endPosArr[1]];
        captor.capture(captured);
        this.board[endPosArr[0]][endPosArr[1]] = null;
        return true;
    }





    public boolean move(String startPos, String endPos) throws IllegalArgumentException{
        int[] startPosArr = getLocation(startPos);
        int[] endPosArr = getLocation(endPos);

//        check if target position is empty
        if(this.board[endPosArr[0]][endPosArr[1]] != null){
//            if not empty, check if occupied by opponent's piece
            if (this.board[endPosArr[0]][endPosArr[1]].getPlayer() == this.board[startPosArr[0]][startPosArr[1]].getPlayer()) {
                System.out.println(this.board[endPosArr[0]][endPosArr[1]]);
                throw new IllegalArgumentException("Desired position is occupied by your player.");
            }
            else{
//                insert capture function call here
            }

        }

        Piece piece = this.board[startPosArr[0]][startPosArr[1]];

        if(piece != null){
            if(piece.isValidMove(startPosArr, endPosArr)){
                this.board[endPosArr[0]][endPosArr[1]] = piece;
                this.board[startPosArr[0]][startPosArr[1]] = null;
            }
            else{
                throw new IllegalArgumentException("Illegal move attempted with " + piece.getName() + "."
                        + System.getProperty("line.separator"));
            }

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

        List<String[]> moves = new ArrayList<String[]>();

        String[] m1 = {"c1", "c2"};
        moves.add(m1);

        String[] m2 = {"c2", "d3"};
        moves.add(m2);
        String[] m3 = {"d3", "d2"};
        moves.add(m3);
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
        }







    }
}