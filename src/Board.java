import javafx.util.*;
import java.util.*;


public class Board{
    public Piece[][] board;




    public Board(){
        this.board = initializeBoard();
    }




    public static Piece[][] initializeBoard(){
        Piece[][] board = new Piece[5][5];

        Player lower = new Player("lower");
        Player UPPER = new Player("UPPER");


        board[0][4] = new Rook(UPPER);
        board[4][0] = new Rook(lower);


        board[1][4] = new Bishop(UPPER);
        board[3][0] = new Bishop(lower);

        board[2][4] = new SilverGeneral(UPPER);
        board[2][0] = new SilverGeneral(lower);

        board[3][4] = new GoldGeneral(UPPER);
        board[1][0] = new GoldGeneral(lower);

        board[4][4] = new King(UPPER);
        board[0][0] = new King(lower);

        board[4][3] = new Pawn(UPPER);
        board[0][1] = new Pawn(lower);


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
        Piece captorPiece = this.board[startPosArr[0]][startPosArr[1]];
        Piece capturedPiece = this.board[endPosArr[0]][endPosArr[1]];
        Player captorPlayer = captorPiece.getPlayer();
        captorPlayer.capture(capturedPiece);

        this.board[endPosArr[0]][endPosArr[1]] = captorPiece;

        return true;
    }



    public boolean move(Player lower, String startPos, String endPos) throws IllegalArgumentException{

        int[] startPosArr = getLocation(startPos);
        int[] endPosArr = getLocation(endPos);

        Piece piece = this.board[startPosArr[0]][startPosArr[1]];

//        check if target position is empty

        System.out.println(endPosArr[0] + " , " + endPosArr[1]);

        if(this.board[endPosArr[0]][endPosArr[1]] != null){
//            if not empty, check if occupied by opponent's piece or own piece
            Player startPosPlayer = this.board[endPosArr[0]][endPosArr[1]].getPlayer();
            Player endPosPlayer = this.board[startPosArr[0]][startPosArr[1]].getPlayer();

            if (startPosPlayer == endPosPlayer) {
                System.out.println(this.board[endPosArr[0]][endPosArr[1]]);
                throw new IllegalArgumentException("Desired position is occupied by your player.");
            }
            else{
                if(piece.isValidMove(startPosArr, endPosArr)) {
                    this.capture(startPosArr, endPosArr);
                }


//                -------------------------------------------------------------------------------------------------------------
                System.out.println(lower.getCaptured());
                System.out.println(piece.getPlayer().getCaptured());
//                -------------------------------------------------------------------------------------------------------------

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




//    method will be called after Piece.isValidMove() is called
//    This method checks whether there is an obstruction in the path intended by the player. If there is, it returns false,
//      throwing an IllegalArgumentException.


    public void checkPathAvailability(Player owner, int[] startPos, int[] endPos) throws IllegalArgumentException {
        Piece movingPiece = this.board[startPos[0]][startPos[1]];
        boolean rook = false;

//        check if Piece is bishop or rook
        if (movingPiece instanceof Rook)
            rook = true;

//        If piece is a rook
        if (rook) {
            int movingIndex = 0;
            int stationaryIndex = 1;
            if (startPos[0] == endPos[0]) {
                movingIndex = 1;
                stationaryIndex = 0;
            }

            int start = Math.min(startPos[movingIndex], endPos[movingIndex]);
            int end = Math.max(startPos[movingIndex], endPos[movingIndex]);

            boolean horizontalMovement = false;
            if (movingIndex == 0)
                horizontalMovement = true;


            Piece checkPresence;
            for (int i = start + 1; i < end; i++) {
//                if rook is moving horizontally
                if (horizontalMovement) {
                    checkPresence = this.board[i][startPos[stationaryIndex]];
                }
//                if rook is moving vertically
                else {
                    checkPresence = this.board[startPos[stationaryIndex]][i];
                }

                if (checkPresence == null)
                    continue;

                else {
                    throw new IllegalArgumentException("Obstruction in path: " + checkPresence.getName());
                }
            }
        }


//        If piece is a bishop

        else {
            int startColumn = Math.min(startPos[0], endPos[0]);
            int endColumn = Math.max(startPos[1], endPos[1]);
            int startRow = Math.min(startPos[0], endPos[0]);
            int endRow = Math.max(startPos[1], endPos[1]);

            int j = startRow + 1;


            for (int i = startColumn + 1; i < endColumn; i++) {
                Piece checkPresence = this.board[i][j];
                if (checkPresence == null) {
                    j++;
                    continue;
                } else {
                    throw new IllegalArgumentException("Obstruction in path: " + checkPresence.getName());
                }


            }

        }
    }


    public boolean isValidMove(int[] startPos, int[] endPos){
        if(startPos[0] != endPos[0]){
            if(startPos[1] == endPos[1]){
                return true;
            }
        }
        else if(startPos[0] == endPos[0]){
            if(startPos[1] != endPos[1]){
                return true;
            }
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

        String[] m2 = {"e3", "e2"};
        moves.add(m2);

        String[] m3 = {"d1", "e2"};
        moves.add(m3);




        for(String[] move : moves) {
            try {
                System.out.println("moving from " + move[0] + " to " + move[1]);
                board.move(lower, move[0], move[1]);
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }


            System.out.println(Utils.stringifyBoard(board.getBoard()));
//
//            System.out.println("lower length : " + lower.getCaptured().size());
//            System.out.println("upper length : " + UPPER.getCaptured().size());
//


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