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

        board[1][4] = new Bishop(upper, new Location(1, 4));
        upper.addToBoard(board[1][4]);
        board[3][0] = new Bishop(lower, new Location(3, 0));
        lower.addToBoard(board[3][0]);

        board[2][4] = new SilverGeneral(upper, new Location(2, 4));
        upper.addToBoard(board[2][4]);
        board[2][0] = new SilverGeneral(lower, new Location(2, 0));
        lower.addToBoard(board[2][0]);

        board[3][4] = new GoldGeneral(upper, new Location(3, 4));
        upper.addToBoard(board[3][4]);
        board[1][0] = new GoldGeneral(lower, new Location(1, 0));
        lower.addToBoard(board[1][0]);

        board[4][4] = new King(upper, new Location(4, 4));
        upper.addToBoard(board[4][4]);
        this.upperKingPos = board[4][4].getLocation();

        board[0][0] = new King(lower, new Location(0, 0));
        lower.addToBoard(board[0][0]);
        this.lowerKingPos = board[0][0].getLocation();

        board[4][3] = new Pawn(upper, new Location(4, 3));
        upper.addToBoard(board[4][3]);
        board[0][1] = new Pawn(lower, new Location(0, 1));
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
     * @param owner
     * @param kingLoc
     * @return
     */
    public boolean isInCheck(Player owner, Location kingLoc) {

//    public boolean isInCheck(Piece king) {
        Player opponent;
        if (owner.getName().equals("UPPER"))
            opponent = this.getLower();
        else
            opponent = this.getUpper();


//        System.out.println(opponent.getName() + "'s on board : " + opponent.printOnBoard());
//        System.out.println(opponent.getName() + "'s captured : " + opponent.printCaptured());


        for (Piece p : opponent.getOnBoard().values()) {
            List<Location> moves = p.findValidPath(p.getLocation(), kingLoc);
//            System.out.println(p.toString() + " - " + p.getLocation().toString() + " : " + moves.toString());
            if (moves.size() == 0)
                continue;
//            Check whether there is a piece in the way of the king and the attacking piece
//            If there is, the king will not be in check from this piece, regardless of the type of piece blocking it.
            boolean blocked = false;
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).equals(kingLoc))
                    continue; // We can continue, because we already know the king's location can be accessed. This is why it's in check.
                if (this.getPiece(moves.get(i)) != null) {
//                    System.out.println("currently blocked by : " + this.getPiece(moves.get(i)).toString());
                    blocked = true;
                }
            }
            if (!blocked) {
                System.out.println("in check by piece: " + p.getName() + " at location: " + p.getLocation().toString());
                return true;
            }
        }

        return false;
    }


    /**
     * TODO: Maybe call isInCheck on each position returned by getValidMoves?
     */
    /**
     * This method will return all valid moves the king can make to remove himself from check.
     *
     * @param king
     * @param kingPos
     * @return List - all locations the king can move to
     */

    public List<Location> listValidMoves(Piece king, Location kingPos) {

        Player owner = king.getPlayer();
        System.out.println("called listValidMoves() on " + owner.getName());

        String movesString = "";
        List<Location> moves = king.getValidMoves(kingPos);
        List<Location> retList = new ArrayList<>();

        System.out.println("valid moves before checking: " + moves.toString());

        for (Location l : moves) {
            if (this.getPiece(l) != null) {
                if (this.getPiece(l).getPlayer() == owner) {
                    System.out.println("Continuing on position: " + l.toString() + " because owned by same piece");
                    continue;
                }
            }
            if (isInCheck(owner, l)) {
                System.out.println("Continuing on position: " + l.toString() + " because puts king in check");
                continue;
            }
            retList.add(l);
        }
        System.out.println("valid moves after checking: " + retList.toString());
        return retList;
    }


// ACTION methods

    public void capture(Location startPos, Location endPos) {
        Piece captorPiece = this.getPiece(startPos);
        Piece capturedPiece = this.getPiece(endPos);
        Player captorPlayer = captorPiece.getPlayer();
        Player opponentPlayer;

        if(captorPlayer.isUpper())
            opponentPlayer = this.getLower();
        else
            opponentPlayer = this.getUpper();

        capturedPiece.setPlayer(captorPlayer);
        capturedPiece.setLocation(null);
        captorPlayer.addToCaptured(capturedPiece);
        opponentPlayer.removeFromBoard(capturedPiece);


//        System.out.println(captorPlayer.getName() + "'s on board : " + captorPlayer.printOnBoard());
//        System.out.println(captorPlayer.getName() + "'s captured: " + captorPlayer.printCaptured());
//        System.out.println(opponentPlayer.getName() + "'s on board : " + opponentPlayer.printOnBoard());
//        System.out.println(opponentPlayer.getName() + "'s captured: " + opponentPlayer.printCaptured());



        this.setPiece(endPos, captorPiece);
        this.setPiece(startPos, null);
    }

    /**
     * @param start
     * @param end
     * @return boolean - to check whether moving the piece puts a piece in check
     * @throws IllegalArgumentException
     */
    public void move(Location start, Location end) throws IllegalArgumentException {
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


    }


    //TODO: need to implement drop so that the piece gets correctly pushed back into the onBoard map, and taken out of captured.

    public void drop(Player captor, String pieceName, Location dropLoc) throws IllegalArgumentException {
        Piece checkPiece = this.getPiece(dropLoc);

        if (checkPiece != null) {
            throw new IllegalArgumentException("Desired position to drop piece is currently occupied by: " +
                    checkPiece.getName());
        }

        if (captor.getName().equals("UPPER")) {
            if (dropLoc.getRow() == 0)
                throw new IllegalArgumentException("Cannot drop into promotion zone.");
        } else {
            if (dropLoc.getRow() == 4)
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
        if (!dropPiece.canDrop(dropLoc)) {
//
        }

        this.setPiece(dropLoc, dropPiece);

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