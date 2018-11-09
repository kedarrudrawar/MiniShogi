import java.util.*;


public class Board {
    private Piece[][] board;
    private Location upperKingPos;
    private Location lowerKingPos;
    private Player upper;
    private Player lower;
    private boolean upperInCheck;
    private boolean lowerInCheck;

    public Board() {
        this.board = this.initializeBoard();
    }


    private Piece[][] initializeBoard() {
        Piece[][] board = new Piece[5][5];

        Player lower = new Player("lower");
        Player upper = new Player("UPPER");
        this.lower = lower;
        this.upper = upper;

        board[0][4] = new Rook(upper, new Location(0, 4));
        upper.addToBoard(board[0][4]);
        board[4][0] = new Rook(lower, new Location(4, 0));
        lower.addToBoard(board[4][0]);

        board[1][4] = new Bishop(upper, new Location(1,4));
        upper.addToBoard(board[1][4]);
        board[3][0] = new Bishop(lower, new Location(3,0));
        lower.addToBoard(board[3][0]);

        board[2][4] = new SilverGeneral(upper, new Location(2,4));
        upper.addToBoard(board[2][4]);
        board[2][0] = new SilverGeneral(lower, new Location(2,0));
        lower.addToBoard(board[2][0]);

        board[3][4] = new GoldGeneral(upper, new Location(3,4));
        upper.addToBoard(board[3][4]);
        board[1][0] = new GoldGeneral(lower, new Location(1,0));
        lower.addToBoard(board[1][0]);

        board[4][4] = new King(upper, new Location(4,4));
        upper.addToBoard(board[4][4]);
        this.upperKingPos = board[4][4].getLocation();

        board[0][0] = new King(lower, new Location(0, 0));
        lower.addToBoard(board[0][0]);
        this.lowerKingPos = board[0][0].getLocation();

        board[4][3] = new Pawn(upper, new Location(4,3));
        upper.addToBoard(board[4][3]);
        board[0][1] = new Pawn(lower, new Location(0,1));
        lower.addToBoard(board[0][1]);

        return board;
    }


// GETTER methods

    public Player getLower() {
        return this.lower;
    }

    public Player getUpper() {
        return this.upper;
    }


    public Piece[][] getBoard() {
        return this.board;
    }

    public Piece getPiece(Location pos) {
        return this.board[pos.getCol()][pos.getRow()];
    }


    // Setter method
    public void setPiece(Location pos, Piece piece) {
        this.board[pos.getCol()][pos.getRow()] = piece;
    }

    //  HELPER methods
    public void canPromote(Location endPos) throws IllegalArgumentException {
        Piece piece = this.getPiece(endPos);
        if (piece.getPlayer().getName() == "UPPER") {
            if (endPos.getRow() != 0)
                throw new IllegalArgumentException("Cannot promote.");
        } else {
            if (endPos.getRow() != 1)
                throw new IllegalArgumentException("Cannot promote.");
        }

        piece.promote();
    }

    /**
     * TODO: Implement so I only have to feed in the location of the king, so I can use it in listValidMoves method on the other positions.
     */

    /**
     *
     * @param owner
     * @param kingLoc
     * @return
     */
    public boolean isInCheck(Player owner, Location kingLoc){

//    public boolean isInCheck(Piece king) {
        Player opponent;
        if (owner.getName().equals("UPPER"))
            opponent = this.getLower();
        else
            opponent = this.getUpper();


        for(Piece p : opponent.getOnBoard().values()){
            List<Location> moves = p.findValidPath(p.getLocation(), kingLoc);
            System.out.println(p.toString() + " : " + moves.toString());
            if(moves.size() == 0)
                continue;
//            Check whether there is a piece in the way of the king and the attacking piece
//            If there is, the king will not be in check from this piece, regardless of the type of piece blocking it.
            boolean blocked = false;
            for(int i = 0; i < moves.size() - 1; i++){
                if(moves.get(i) != null)
                    blocked = true;
            }
            if(!blocked)
                return true;
        }

        return false;
    }


    /**
     * TODO: Maybe call isInCheck on each position returned by getValidMoves?
     */
    /**
     *
     * @param king
     * @param kingPos
     * @return
     */

    public String listValidMoves(Piece king, Location kingPos){
        Player owner = king.getPlayer();
        String movesString = "";
        List<Location> moves = king.getValidMoves(kingPos);

        for(Location l : moves){
            if(this.getPiece(l) != null) {
                if (this.getPiece(l).getPlayer() != owner) {
//                    test whether king will be in check at position L: if(isInCheck(kingPos)){}
                    movesString += " " + l.toString();
                }
            }
            else{
//                test whether king will be in check at position L:  if(isInCheck(kingPos)){}
                movesString += " " + l.toString();
            }
        }
        return movesString;
    }


// ACTION methods

    public void capture(Location startPos, Location endPos) {
        Piece captorPiece = this.getPiece(startPos);
        Piece capturedPiece = this.getPiece(endPos);
        Player captorPlayer = captorPiece.getPlayer();
        capturedPiece.setPlayer(captorPlayer);
        captorPlayer.capture(capturedPiece);
//        capturedPiece.setLocation(null);

        this.setPiece(endPos, captorPiece);
        this.setPiece(startPos, null);
    }

    /**
     *
     * @param startPos
     * @param endPos
     * @throws IllegalArgumentException
     */
    public void move(String startPos, String endPos) throws IllegalArgumentException {
        Location start = new Location(startPos);
        Location end = new Location(endPos);

        Piece startPiece = this.getPiece(start);

        if (startPiece == null)
            throw new IllegalArgumentException("Illegal move.");

        List<Location> positions = startPiece.findValidPath(start, end);
        if (positions.size() == 0)
            throw new IllegalArgumentException("Illegal move.");

//        This for loop iterates through all the positions except the final position to check if they are all empty.
        for (int i = 0; i < positions.size() - 1; i++) {
            Location currLoc = positions.get(i);
            Piece currPiece = this.getPiece(currLoc);
            if (currPiece != null) {
                throw new IllegalArgumentException("Obstructed by: " + currPiece.getName());
            }
        }

        Piece endPiece = this.getPiece(end);
        if (endPiece == null) {
            this.setPiece(end, startPiece);
            this.setPiece(start, null);
        } else {
            if (endPiece.getPlayer() == startPiece.getPlayer()) {
                if (endPiece.getPlayer().getName().equals("UPPER"))
                    System.out.println("lower player wins. Illegal move.");
                else
                    System.out.println("UPPER player wins. Illegal move.");
                System.exit(0);
            }
            if (endPiece.isPromoted())
                endPiece.demote();
            this.capture(start, end);
        }


        startPiece.setLocation(end);


//        UPDATE KINGS' LOCATIONS
        if (start.equals(upperKingPos))
            upperKingPos = end;
        else if (start.equals(lowerKingPos))
            lowerKingPos = end;


        Piece lowerKing = this.getPiece(lowerKingPos);
        Piece upperKing = this.getPiece(upperKingPos);


        //TODO: need to check for every single available move, including killing their pieces or blocking their paths.
        //TODO: need to also take into account that we can drop pieces back into the game to block checks

//        Check whether lower player is in check
        if (isInCheck(lower, lowerKingPos)) {
//            System.out.println("calling listValidMoves on lowerKingPos");
            String movesString =  listValidMoves(lowerKing, lowerKingPos);
            if(movesString.length() == 0){
                System.out.println("upper player has won.");
                System.exit(0);
            }

            System.out.println("lower player is in check!");
            System.out.print("Available moves: ");
            System.out.println(movesString);
        }


//        Check whether upper player is in check
        if (isInCheck(upper, upperKingPos)) {
//            System.out.println("calling listValidMoves on upperKingPos");
            String movesString =  listValidMoves(upperKing, upperKingPos);
            if(movesString.length() == 0){
                System.out.println("lower player has won.");
                System.exit(0);
            }

            System.out.println("UPPER player is in check!");
            System.out.print("Available moves: ");
            System.out.println(movesString);
        }
    }

    public void drop(Player captor, String pieceName, String position) throws IllegalArgumentException {
        Location dropPos = new Location(position);
        Piece checkPiece = this.getPiece(dropPos);

        if (checkPiece != null) {
            throw new IllegalArgumentException("Desired position to drop piece is currently occupied by: " +
                    checkPiece.getName());
        }

        if (captor.getName().equals("UPPER")) {
            if (dropPos.getRow() == 0)
                throw new IllegalArgumentException("Cannot drop into promotion zone.");
        } else {
            if (dropPos.getRow() == 4)
                throw new IllegalArgumentException("Cannot drop into promotion zone");
        }


        Map<String, Piece> capturedMap = captor.getCaptured();

        Piece dropPiece = capturedMap.get(pieceName);

        if (dropPiece == null)
            throw new IllegalArgumentException("You have not captured this piece.");

        /**
         * TODO: Check for droppability
         */

//        Check for dropping validity here (pawns cannot be in same column)
        if (!dropPiece.canDrop(dropPos)) {
//
        }

        this.setPiece(dropPos, dropPiece);

        captor.moveToBoard(dropPiece);
    }

    public void promote(String position) {
        Location promPos = new Location(position);
        Piece piece = this.getPiece(promPos);
        if (piece.getPlayer().getName() == "UPPER") {
            if (promPos.getRow() != 0)
                throw new IllegalArgumentException("Cannot promote.");
        } else {
            if (promPos.getRow() != 4)
                throw new IllegalArgumentException("Cannot promote.");
        }

        piece.promote();
    }


}