import java.util.*;


public class Board {
    private Piece[][] board;
    //    private Location upperKingPos;
//    private Location lowerKingPos;
    private Player upper;
    private Player lower;

    public Board() {
        this.board = this.initializeBoard();
    }

    public Board(List<Utils.InitialPosition> positions) {
        this.board = this.initializeBoard(positions);
    }

    private Piece[][] initializeBoard(List<Utils.InitialPosition> positions) {
        Piece[][] board = new Piece[5][5];
        Player lower = new Player("lower");
        Player upper = new Player("UPPER");
        this.lower = lower;
        this.upper = upper;

        for (Utils.InitialPosition position : positions) {
            String pieceName = position.piece;
            Location loc = new Location(position.position);
            board[loc.getCol()][loc.getRow()] = this.createPiece(pieceName, loc);
        }
        System.out.println(Utils.stringifyBoard(board));
        return board;
    }

    private Piece[][] initializeBoard() {
        Piece[][] board = new Piece[5][5];

        Player lower = new Player("lower");
        Player upper = new Player("UPPER");
        this.lower = lower;
        this.upper = upper;

        board[0][4] = new Rook(upper, new Location(0, 4));
        upper.addToBoardList(board[0][4]);
        board[4][0] = new Rook(lower, new Location(4, 0));
        lower.addToBoardList(board[4][0]);

        board[1][4] = new Bishop(upper, new Location(1, 4));
        upper.addToBoardList(board[1][4]);
        board[3][0] = new Bishop(lower, new Location(3, 0));
        lower.addToBoardList(board[3][0]);

        board[2][4] = new SilverGeneral(upper, new Location(2, 4));
        upper.addToBoardList(board[2][4]);
        board[2][0] = new SilverGeneral(lower, new Location(2, 0));
        lower.addToBoardList(board[2][0]);

        board[3][4] = new GoldGeneral(upper, new Location(3, 4));
        upper.addToBoardList(board[3][4]);
        board[1][0] = new GoldGeneral(lower, new Location(1, 0));
        lower.addToBoardList(board[1][0]);

        board[4][4] = new King(upper, new Location(4, 4));
        upper.addToBoardList(board[4][4]);
//        this.upperKingPos = board[4][4].getLocation();

        board[0][0] = new King(lower, new Location(0, 0));
        lower.addToBoardList(board[0][0]);
//        this.lowerKingPos = board[0][0].getLocation();

        board[4][3] = new Pawn(upper, new Location(4, 3));
        upper.addToBoardList(board[4][3]);
        board[0][1] = new Pawn(lower, new Location(0, 1));
        lower.addToBoardList(board[0][1]);

        return board;
    }

// GETTER methods

    public Player getLower() {
        return this.lower;
    }

    public Player getUpper() {
        return this.upper;
    }

    public Player getOpponent(Player player) {
        if (player.isUpper())
            return this.getLower();
        else
            return this.getUpper();

    }

    public Piece[][] getBoard() {
        return this.board;
    }

    public Piece getPiece(Location pos) {
        return this.board[pos.getCol()][pos.getRow()];
    }

    //    Setter method
    public void setPiece(Location pos, Piece piece) {
        this.board[pos.getCol()][pos.getRow()] = piece;
    }

//    HELPER methods
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

    public boolean isInCheckBoolean(Player owner, Location kingLoc){
        List<Piece> threateningPieces = isInCheck(owner, kingLoc);
        if(threateningPieces.size() != 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * @param owner
     * @param kingLoc
     * @return
     */
    public List<Piece> isInCheck(Player owner, Location kingLoc) {
        List<Piece> threateningPieces = new ArrayList<>();
        Player opponent;
        if (owner.getName().equals("UPPER"))
            opponent = this.getLower();
        else
            opponent = this.getUpper();


//        System.out.println(opponent.getName() + "'s on board : " + opponent.printOnBoard());
//        System.out.println(opponent.getName() + "'s captured : " + opponent.printCaptured());

        System.out.println("OPPONENT : " + opponent.getName());
        for (Piece p : opponent.getOnBoard()) {
            System.out.println("Calling findValidPath: Board.java: line 153 on Piece : " + p.getName());
            System.out.println("Piece location: " + p.getLocation());
            List<Location> moves = p.findValidPath(p.getLocation(), kingLoc);

//            System.out.println(p.toString() + " - " + p.getLocation().toString() + " : " + moves.toString());
            if (moves.size() == 0)
                continue;
//  Check whether there is a piece in the way of the king and the attacking piece
//  If there is, the king will not be in check from this piece, regardless of the type of piece blocking it.
            boolean blocked = false;
            for (Location move : moves) {
                if (move.equals(kingLoc))
                    continue; // We can continue, because we already know the king's location can be accessed. This is why it's in check.
                if (this.getPiece(move) != null) {
//                    System.out.println("currently blocked by : " + this.getPiece(moves.get(i)).toString());
                    blocked = true;
                    break;
                }
            }
            if (!blocked) {
                System.out.println("in check by piece: " + p.getName() + " at location: " + p.getLocation().toString());
                threateningPieces.add(p);
            }
        }
//  If the size of threateningPieces returned by isInCheck is 0, that means the king is not in check.
        return threateningPieces;
    }

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
            if (this.isInCheckBoolean(owner, l)) {
                System.out.println("Continuing on position: " + l.toString() + " because puts king in check");
                continue;
            }
            retList.add(l);
        }
        System.out.println("valid moves after checking: " + retList.toString());
        return retList;
    }

    /**
     * This method will check whether the king will be in check after a specific move has been made.
     * This is to check for double/triple/... check.
     * @param action
     * @return
     */
    public boolean testCheckAfterAction(Player player, String action){
        String[] actionSplit = action.split(" ");
        Location endLoc = new Location (actionSplit[2]);

        if(actionSplit[0].equals("move")){
            Location startLoc = new Location(actionSplit[1]);
            Piece target = this.getPiece(endLoc);
            boolean empty = true;
            if(target != null){
                empty = false;
            }

            this.move(startLoc, endLoc);
            boolean currKingCheck = this.isInCheckBoolean(player, player.getKing().getLocation());

//          REVERSE MOVES MADE TO TEST.
            this.forceMove(endLoc, startLoc);

            if(! empty){
                System.out.println("After moving : \n" + Utils.stringifyBoard(this.board));
                Player originalOwner = this.getOpponent(player);

                player.removeFromCaptured(target);
                target.setPlayer(originalOwner);
                this.setPiece(endLoc, target);
                target.setLocation(endLoc);
                originalOwner.addToBoardList(target);


                System.out.println("After supposedly fixing: \n" + Utils.stringifyBoard(this.board));
                System.out.println("Target's new location : " + target.getLocation());
            }

            if(currKingCheck){
                return true;
            }
            else{
                return false;
            }
        }
        else if(actionSplit[0].equals("drop")){
            String pieceName = actionSplit[1];
            Piece dropPiece = player.getPieceFromCaptured(pieceName);

        }

        return false;
    }



    public List<String> getDropMoves(Player owner, Location kingLoc, List<Piece> threateningPieces){
        List<String> dropStrings = new ArrayList<>();
        for(Piece piece : threateningPieces){
            Location start = piece.getLocation();
            List<Location> positions = piece.findValidPath(start, kingLoc);
            for(Location position : positions){
                if(position.equals(kingLoc))
                    continue;
                for(Piece dropPiece : owner.getCaptured()){
                    String action = String.format("drop %s %s", dropPiece.getName(), position.toString());
                    if(! testCheckAfterAction(owner, action)) {
                        dropStrings.add(action);
                    }
                }
            }
        }
        return dropStrings;
    }

    public List<String> listSacrificeMoves(Player owner, Location kingLoc, List<Piece> threateningPieces){
        List<String> moves = new ArrayList<>();
        for(Piece threateningPiece : threateningPieces){
            Location start = threateningPiece.getLocation();
//            This is the path that the threatening Piece can take to capture the King
            List<Location> positions = threateningPiece.findValidPath(start, kingLoc);
//            We will add the start location of the threatening piece as well, because the owner's pieces may
//              be able to capture this piece to protect the king. However, findValidPath does not include start position
//              in its return list.
            positions.add(start);

            List<Piece> onBoardPieces = new ArrayList<>(owner.getOnBoard());
//            These are the pieces that the owner may be able to move to protect the King
            for(Piece ownerPiece: onBoardPieces){
                if(ownerPiece.getName().equalsIgnoreCase("k"))
                    continue;
//
                Location ownerPieceLoc = ownerPiece.getLocation();
                for(Location position : positions){
//                    This is to check whether the owner's piece can move to any of the positions in the threatening
//                      piece's path to block or capture the threatening piece
                    List<Location> path = ownerPiece.findValidPath(ownerPieceLoc, position);
//                    If valid path exists from owner's piece to one of these positions,
//                      we need to check if it can be blocked by another piece:
                    if(path.size() != 0){
                        boolean obstructed = false;
                        for(Location l : path){
//                          If the path is obstructed by another piece, the owner is not allowed to move their piece
//                              there to save their king.
//                           However, if the piece obstructing it is the threatening piece, it does not matter. The
//                              owner's piece can just capture it
                            if(this.getPiece(l) != null && ! this.getPiece(l).equals(threateningPiece)){
                                obstructed = true;
                                break;
                            }
                        }
                        if(! obstructed) {
                            String action = String.format("move %s %s", ownerPieceLoc, position.toString());
                            if (!testCheckAfterAction(owner, action)) {
                                moves.add(action);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }





// ACTION methods
    private void convertOwner(Player captor, Location endLoc){
        Piece capturedPiece = this.getPiece(endLoc);
    }



    public void capture(Location startPos, Location endPos) {
//        Initialize variables
        Piece captorPiece = this.getPiece(startPos);
        Piece capturedPiece = this.getPiece(endPos);
        Player captorPlayer = captorPiece.getPlayer();
        Player opponentPlayer = this.getOpponent(captorPlayer);

        capturedPiece.setPlayer(captorPlayer);
        capturedPiece.setLocation(null);
        captorPlayer.addToCapturedList(capturedPiece);
        opponentPlayer.removeFromBoard(capturedPiece);


//        System.out.println(captorPlayer.getName() + "'s on board : " + captorPlayer.printOnBoard());
//        System.out.println(captorPlayer.getName() + "'s captured: " + captorPlayer.printCaptured());
//        System.out.println(opponentPlayer.getName() + "'s on board : " + opponentPlayer.printOnBoard());
//        System.out.println(opponentPlayer.getName() + "'s captured: " + opponentPlayer.printCaptured());


        this.setPiece(endPos, captorPiece);
        this.setPiece(startPos, null);
    }

    /**
     * This method is only to be used for moving pieces back to original positions. They would have been moved for
     *  testing (in method testCheckAfterAction()). This method ignores regular conventions of moving pieces.
     * @param start
     * @param end
     */

    private void forceMove(Location start, Location end){
        Piece startPiece = this.getPiece(start);
        this.setPiece(end, startPiece);
        this.setPiece(start, null);
        startPiece.setLocation(end);
    }



    /**
     * @param start
     * @param end
     * @return boolean - to check whether moving the piece puts a piece in check
     * @throws IllegalArgumentException
     */
    public void move(Location start, Location end) throws IllegalArgumentException {
        Piece startPiece = this.getPiece(start);
        Player opponent = this.getOpponent(startPiece.getPlayer());
        if (startPiece == null) {
            System.out.println("Cannot move piece that doesn't exist.");
            System.out.println("Illegal move. " + opponent.toString() + " wins.");
            throw new IllegalArgumentException();
//            System.exit(1);
        }

        List<Location> positions = startPiece.findValidPath(start, end);
        if (positions.size() == 0) {
            System.out.println("Called findValidPath on piece : " + startPiece.getName() + " from pos : " + start.toString() + " to " + end.toString());
            System.out.println("Illegal move. " + opponent.toString() + " wins.");
            throw new IllegalArgumentException();
//            System.exit(1);
        }

//        This for loop iterates through all the positions except the final position (destination) to check if they are all empty.
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
            if (endPiece.getPlayer() != opponent) {
                System.out.println(opponent.getName() + " player wins. Illegal move.");
                System.exit(0);
            }
            if (endPiece.isPromoted())
                endPiece.demote();
            this.capture(start, end);
        }

        startPiece.setLocation(end);


    }


    /**
     * THIS METHOD NEEDS TO BE FIXED. This is for taking in files from -f mode, and running the moves within them.
     * @param moves
     */


    public void movePieces(List<String> moves) {
        boolean lowerTurn = true;
        Player currPlayer = this.lower;
        Player opponentPlayer = this.upper;

//        System.out.println("curr player is null : " + currPlayer == null);

        for (String move : moves) {
            String[] inputSplit = move.split(" ");
            if (inputSplit[0].equals("move")) {
                String startPos = inputSplit[1];
                String endPos = inputSplit[2];

                Location startLoc = new Location(startPos);
                Location endLoc = new Location(endPos);

                boolean promote = false;

                if (inputSplit.length == 4) {
                    if(inputSplit[3].equals("promote"))
                        promote = true;
                }

                if (this.getPiece(startLoc).getPlayer() != currPlayer) {
                    System.out.println("Moving piece that is not yours. Illegal move.");
                    System.exit(0);
                }

                Piece currKing = currPlayer.getKing();

//  This is to check whether the piece that's moving is the king. If it is, we need to check whether the destination
//  of the king will put it in check. If it is not, then we just need to check the original position of the king.
                if (startLoc.equals(currKing.getLocation())) {
                    boolean currKingCheck = this.isInCheckBoolean(currPlayer, endLoc);

//  If moving the king to this location puts king in check, this is illegal.
                    if (currKingCheck) {
                        System.out.println("Moving your piece into check. Illegal move.");
                        System.exit(0);
                    }
                    this.move(startLoc, endLoc);
                } else {
                    this.move(startLoc, endLoc);
                    boolean currKingCheck = this.isInCheckBoolean(currPlayer, currKing.getLocation());
                    if (currKingCheck) {
                        System.out.println("Moving your piece into check. Illegal move.");
                        System.exit(0);
                    }
                }

                if (promote) {
                    System.out.println("about to promote");
                    this.promote(endPos);
                }

                Piece opponentKing = opponentPlayer.getKing();
                Location opponentKingLoc = opponentKing.getLocation();
                List<Piece> threateningPieces = this.isInCheck(opponentPlayer, opponentKingLoc);

                boolean opponentKingCheck = this.isInCheckBoolean(opponentPlayer, opponentKingLoc);
                if (opponentKingCheck) {
//                    System.out.println("calling listValidMoves on opponent's king");
                    List<Location> kingMovesList = this.listValidMoves(opponentKing, opponentKing.getLocation());
                    List<String> dropList = this.getDropMoves(opponentPlayer, opponentKingLoc, threateningPieces);
                    List<String> sacrificeMoves = this.listSacrificeMoves(opponentPlayer, opponentKingLoc, threateningPieces);


                    if (kingMovesList.size() == 0) {
                        System.out.println(currPlayer.getName() + " player has won.");
                        System.exit(0);
                    }
                    System.out.println(opponentPlayer.getName() + " player is in check!");
                    System.out.println("Available moves: ");
                    for (Location loc : kingMovesList) {
                        System.out.println(String.format("move %s %s", opponentKingLoc.toString(), loc.toString()));
                    }
                    for(String dropMove : dropList){
                        System.out.println(dropMove);
                    }
                    for(String sacrifice : sacrificeMoves){
                        System.out.println(sacrifice);
                    }
                }
            } else if (inputSplit[0].equals("drop")) {
                String dropPiece = inputSplit[1];
                String dropPos = inputSplit[2];

                Location dropLoc = new Location(dropPos);

                this.drop(currPlayer, dropPiece, dropLoc);

            } else {
                System.out.println("Illegal move. " + opponentPlayer.toString() + " has won.");
                System.exit(0);
            }

            currPlayer.incrementTurn();
//            Flip turn:
            lowerTurn = !lowerTurn;

            if (lowerTurn) {
                currPlayer = lower;
                opponentPlayer = upper;
            } else {
                currPlayer = upper;
                opponentPlayer = lower;
            }
        }
    }

    //TODO: need to implement drop so that the piece gets correctly pushed back into the onBoard map, and taken out of captured.

    public void drop(Player captor, String pieceName, Location dropLoc) {
        Piece checkPiece = this.getPiece(dropLoc);

        Player opponent = this.getOpponent(captor);

        if (pieceName.equals("p") || pieceName.equals("P")) {
            if (dropLoc.getCol() == captor.getPawn().getLocation().getCol()) {
                System.out.println("Cannot have multiple pawns in same column. Illegal move.");
                System.exit(1);
            }
        }

        if (checkPiece != null) {
            System.out.println("Desired position to drop piece is currently occupied by: " +
                    checkPiece.getName());
            System.out.println("Illegal move. " + opponent.getName() + " wins.");
        }


        Piece dropPiece = captor.getPieceFromCaptured(pieceName);

        if (dropPiece == null)
            throw new IllegalArgumentException("You have not captured this piece.");

        /**
         * TODO: Check for droppability
         */

//        Check for dropping validity here (pawns cannot be in same column)
        if (!dropPiece.canDrop(dropLoc)) {
            System.out.println("Cannot drop piece. Illegal move.");
            System.out.println(opponent.getName() + " wins.");
            System.exit(0);
        }

        this.setPiece(dropLoc, dropPiece);
        dropPiece.setLocation(dropLoc);
        captor.moveToBoardList(dropPiece);
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

    public Piece createPiece(String pieceName, Location loc) {
        if (pieceName.equalsIgnoreCase("p")) {
            Player curr;
            if (pieceName.equals("p"))
                curr = lower;
            else
                curr = upper;
            return new Pawn(curr, loc);
        }

        else if (pieceName.equalsIgnoreCase("b")) {
            Player curr;
            if (pieceName.equals("b"))
                curr = lower;
            else
                curr = upper;
            return new Bishop(curr, loc);
        }
        else if (pieceName.equalsIgnoreCase("r")) {
            Player curr;
            if (pieceName.equals("r"))
                curr = lower;
            else
                curr = upper;
            return new Rook(curr, loc);
        }
        else if (pieceName.equalsIgnoreCase("g")) {
            Player curr;
            if (pieceName.equals("g"))
                curr = lower;
            else
                curr = upper;
            return new GoldGeneral(curr, loc);
        }

        else if (pieceName.equalsIgnoreCase("s")) {
            Player curr;
            if (pieceName.equals("p"))
                curr = lower;
            else
                curr = upper;
            return new SilverGeneral(curr, loc);
        } else {
            Player curr;
            if (pieceName.equals("k"))
                curr = lower;
            else
                curr = upper;
            return new King(curr, loc);
        }
    }
}