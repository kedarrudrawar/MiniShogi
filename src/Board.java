import java.util.*;


public class Board {
    public Piece[][] board;


    public Board() {
        this.board = this.initializeBoard();
    }


    private Piece[][] initializeBoard() {
        Piece[][] board = new Piece[5][5];

        Player lower = new Player("lower");
        Player upper = new Player("UPPER");

        board[0][4] = new Rook(upper);
        board[4][0] = new Rook(lower);


        board[1][4] = new Bishop(upper);
        board[3][0] = new Bishop(lower);

        board[2][4] = new SilverGeneral(upper);
        board[2][0] = new SilverGeneral(lower);

        board[3][4] = new GoldGeneral(upper);
        board[1][0] = new GoldGeneral(lower);

        board[4][4] = new King(upper);
        board[0][0] = new King(lower);

        board[4][3] = new Pawn(upper);
        board[0][1] = new Pawn(lower);


        String b = Utils.stringifyBoard(board);
        System.out.println(b);
        return board;
    }


// GETTER methods

    public Player getLower(){
        return this.board[4][0].getPlayer();
    }

    public Player getUpper(){
        return this.board[0][4].getPlayer();
    }



    //    helper method to convert position string to index array (a2 --> [0][1])
//    public int[] getLocation(String location) {
//        int row = location.charAt(0) - 97;
//        int col = Character.getNumericValue(location.charAt(1)) - 1;
//        int[] pos = {row, col};
//        return pos;
//    }


    public Piece[][] getBoard() {
        return this.board;
    }

    public Piece getPiece(Location pos) {
        return this.board[pos.getCol()][pos.getRow()];
    }


// Setter method
    public void setPiece(Location pos, Piece piece){
        this.board[pos.getCol()][pos.getRow()] = piece;
    }



// ACTION methods


    public void capture(Location startPos, Location endPos) {
        Piece captorPiece = this.getPiece(startPos);
        Piece capturedPiece = this.getPiece(endPos);
        Player captorPlayer = captorPiece.getPlayer();
        captorPlayer.capture(capturedPiece);


        this.setPiece(endPos, captorPiece);
        this.setPiece(startPos, null);
    }


    public void move(String startPos, String endPos) throws IllegalArgumentException {

        Location start = new Location(startPos);
        Location end = new Location(endPos);

        Piece startPiece = this.getPiece(start);

        if(startPiece == null)
            throw new IllegalArgumentException("Illegal move.");

        List<Location> positions = startPiece.findValidPath(start, end);


//        This for loop iterates through all the positions except the final position to check if they are all empty.
        for (int i = 0; i < positions.size() - 1; i++) {
            Location currLoc = positions.get(i);
            Piece currPiece = this.getPiece(currLoc);
            if (currPiece != null) {
                throw new IllegalArgumentException("Obstruction detection - " + currPiece.getName());
            }
        }

        Piece endPiece = this.getPiece(end);
        if (endPiece == null) {
            this.setPiece(end, startPiece);
            this.setPiece(start, null);
        } else {
            this.capture(start, end);
        }
    }

    public void drop(Player player, String pieceName, String position) throws IllegalArgumentException {
        Location dropPos = new Location(position);
        Piece checkPiece = this.getPiece(dropPos);

        if (checkPiece != null) {
            throw new IllegalArgumentException("Desired position to drop piece is currently occupied by: " +
                    checkPiece.getName());
        }

        Map<String, Piece> capturedMap = player.getCaptured();
        if (capturedMap.containsKey(pieceName))
            player.getCaptured().remove(pieceName);
    }




}