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

    public Piece getPiece(Location pos){
        return this.board[pos.getCol()][pos.getRow()];
    }


// ACTION methods


    public void capture(Location startPos, Location endPos){
        Piece captorPiece = this.board[startPos.getCol()][startPos.getRow()];
        Piece capturedPiece = this.board[endPos.getCol()][endPos.getRow()];
        Player captorPlayer = captorPiece.getPlayer();
        captorPlayer.capture(capturedPiece);

        this.board[endPos.getCol()][endPos.getRow()] = captorPiece;
        this.board[startPos.getCol()][startPos.getRow()] = null;
    }



    public void move(String startPos, String endPos) throws IllegalArgumentException {

        Location start = new Location(startPos);
        Location end = new Location(endPos);

        Piece startPiece = this.board[start.getCol()][start.getRow()];

        List<Location> positions = startPiece.findValidPath(start, end);

//        This for loop iterates through all the positions except the final position to check if they are all empty.
        for (int i = 0; i < positions.size() - 1; i++) {
            Location currLoc = positions.get(i);
            Piece currPiece = this.board[currLoc.getCol()][currLoc.getRow()];
            if (currPiece != null) {
                throw new IllegalArgumentException("Obstruction detection - " + currPiece.getName());
            }
        }

        Piece endPiece = this.board[end.getCol()][end.getRow()];
        if (endPiece == null) {
            this.board[end.getCol()][end.getRow()] = startPiece;
            this.board[start.getCol()][start.getRow()] = null;
        }
        else {
            this.capture(start, end);

        }
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

//        Initialize players (all pieces of one team are initialized to same player)
        Player lower = boardArray[4][0].getPlayer();
        Player UPPER = boardArray[0][4].getPlayer();

        List<String[]> moves = new ArrayList<String[]>();

        String[] m1 = {"e4", "e3"};
        moves.add(m1);

        String[] m2 = {"e3", "e2"};
        moves.add(m2);

        String[] m3 = {"d1", "e2"};
        moves.add(m3);

        String[] m4 = {"e1", "e3"};
        moves.add(m4);




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