import javax.swing.*;
import java.util.*;
import java.util.stream.*;


/**
 * Class: Board - stores 2D array of Piece objects that represent the miniShogi board.
 *
 * @author Kedar Rudrawar
 */
public class Board {
    // CONSTANTS
    private static final String MOVE = "move";
    private static final String DROP = "drop";
    private static final String LOWER = "lower";
    private static final String UPPER = "UPPER";
    private static final int BOARDSIZE = 5;

    private Piece[][] board;
    private Player upper;
    private Player lower;



    /**
     * Constructor - used in Interactive Mode
     */
    public Board() {
        this.board = this.initializeBoard();
    }

    /**
     * Constructor - used in File Mode
     * @param tc    TestCase object including initialization information
     */
    public Board(Utils.TestCase tc) {
        this.board = this.initializeBoard(tc);
    }


    /* GETTER methods */

    /**
     * This function returns the lower player.
     * @return the lower Player
     */
    public Player getLower() {
        return this.lower;
    }

    /**
     * This function returns the upper player.
     * @return the UPPER Player
     */
    public Player getUpper() {
        return this.upper;
    }

    /**
     * This function returns the opponent of a Player
     * @param player    Player whose opponent will be returned
     * @return a Player - opponent
     */
    private Player getOpponent(Player player) {
        if (player.isUpper())
            return this.getLower();
        else
            return this.getUpper();
    }

    /**
     * This function returns a Piece object stored at a Location on the board
     * @param pos   Location representing desired Piece's position
     * @return a Piece
     */
    private Piece getPiece(Location pos) {
        return this.board[pos.getCol()][pos.getRow()];
    }

    /**
     * This function sets a specific Location on the board to a given Piece
     * @param pos   Location on the board to be set
     * @param piece Piece to be set
     */
    private void setPiece(Location pos, Piece piece) {
        this.board[pos.getCol()][pos.getRow()] = piece;
    }

    /**
     * This function returns the 2D Piece array stored as a field of the class
     * @return a 2D array of Pieces
     */
    public Piece[][] getBoard() {
        return this.board;
    }

    /**
     * This method prints out the current state of the board and captured lists of each player.
     * @return a String
     */
    public String printBoardAndStats() {
        String output = "";
        output += Utils.stringifyBoard(this.getBoard()) + System.getProperty("line.separator");
        output += String.format("Captures %s: ", UPPER);
        for (int i = 0; i < upper.getCaptured().size(); i++) {
            if (i != 0)
                output += " ";
            output += upper.getCaptured().get(i).toString();
        }
        output += System.getProperty("line.separator");
        output += String.format("Captures %s: ", LOWER);
        for (int i = 0; i < lower.getCaptured().size(); i++) {
            if (i != 0)
                output += " ";
            output += lower.getCaptured().get(i).toString();
        }
        output += System.getProperty("line.separator");

        return output;
    }

    /**
     * This method returns a String containing information of a player who is in check and the available moves they can
     * make to get out of check.
     * @param checkedPlayer - Player that is in check
     * @param allMoves - List of String moves to be printed
     * @return a String
     */
    public String printCheckOutput(Player checkedPlayer, List<String> allMoves) {
        String output = "";
        Location opponentKingLoc = checkedPlayer.getPieceFromBoard("k").getLocation();
        output += checkedPlayer.getName() + " player is in check!" + System.getProperty("line.separator");
        output += "Available moves:" + System.getProperty("line.separator");

        for (String sacrifice : allMoves) {
            output += sacrifice + System.getProperty("line.separator");
        }
        return output;
    }

    /**
     * This method is the general method to execute actions/commands including drop and move.
     * @param currPlayer - The Player executing the command
     * @param command - String representing the command ("move a1 a2" or "drop p a2")
     * @return a boolean - true if the command was executed, false if any failure occured
     */
    public boolean executeCommand(Player currPlayer, String command) {
        boolean success = true;

        String[] inputSplit = command.split(" ");
        String action = inputSplit[0];

        if (action.equals(MOVE)) {
            success = executeMove(currPlayer, command);
        } else if (action.equals(DROP)) {
            success = executeDrop(currPlayer, command);
        } else {
            success = false;
        }

        return success;
    }

    /**
     * This method moves a piece by force, without adhering to its moving conventions.
     * This function is used to reverse moves made for testing (see testCheckAfterAction())
     * @param start source Location for move
     * @param end   destination Location for move
     * @return a boolean - to check whether moving the piece puts a piece in check
     */
    public boolean move(Location start, Location end) {
        Piece startPiece = this.getPiece(start);
        Player opponent = this.getOpponent(startPiece.getPlayer());
        if (startPiece == null) {
            return false;
        }

        List<Location> positions = startPiece.findValidPath(start, end);
        if (positions.size() == 0) {
            return false;
        }

        // Iterate through all the positions except the final position (destination) to check if they are all empty.
        for (int i = 0; i < positions.size() - 1; i++) {
            Location currLoc = positions.get(i);
            Piece currPiece = this.getPiece(currLoc);
            if (currPiece != null) {
                return false;
            }
        }

        Piece endPiece = this.getPiece(end);
        //  If destination is empty, move Piece
        if (endPiece == null) {
            this.setPiece(end, startPiece);
            this.setPiece(start, null);
        }
        //  If not empty, capture the occupying piece and demote if necessary
        else {
            if (endPiece.getPlayer() != opponent) {
                return false;
            }
            if (endPiece.isPromoted()) {
                endPiece.demote();
            }
            this.capture(start, end);
        }
        startPiece.setLocation(end);

        return true;
    }

    /**
     * This method reverses the action of dropping a Piece onto the board. This is used to reverse a drop made for testing.
     * @param captor    Player who dropped the Piece onto the board
     * @param dropLoc   Location at which Piece was bored
     * @param capturedListIndex Index in the captured ArrayList to place Piece back into
     * @return void
     */
    public void unDrop(Player captor, Location dropLoc, int capturedListIndex){
        Piece droppedPiece = this.getPiece(dropLoc);
        captor.removeFromBoard(droppedPiece);
        captor.addToCapturedList(capturedListIndex, droppedPiece);
        this.setPiece(dropLoc, null);
        droppedPiece.setLocation(null);
    }

    /**
     * This method checks whether dropping a pawn induces a checkmate (which is illegal). This will be called if a player
     * has attempted to drop a pawn.
     * @param success   boolean representing whether anything has failed yet until this method is called
     * @param checkmate boolean representing whether opponent's king is now in checkmate
     * @param dropPlayer    Player who is attempting to drop the pawn
     * @param dropLoc   Location at which the pawn may be dropped
     * @param index index in the captured array to return the pawn to if it is illegal to drop
     * @return a boolean - true if dropping the pawn is illegal, false otherwise
     */
    public boolean illegalPawnDrop(boolean success, boolean checkmate, Player dropPlayer, Location dropLoc, int index) {
        if (!success) {
            if (checkmate) {
                this.unDrop(dropPlayer, dropLoc, index);
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns whether or not a player's king is in check.
     * @param owner Player whose King this method checks for
     * @param kingLoc   Location of where the king is located
     * @return a boolean - true if king is in check, false otherwise
     */
    public boolean isInCheckBoolean(Player owner, Location kingLoc) {
        List<Piece> threateningPieces = isInCheck(owner, kingLoc);
        if (threateningPieces.size() != 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method accumulates all possible moves a player can make if their king is in check. It sorts and returns a
     * list of strings.
     * @param owner Player whose king is in check
     * @param kingLoc   Location of king in check
     * @return a List of Strings - all possible moves
     */
    public List<String> listAllAvailableMoves(Player owner, Location kingLoc){
        Piece king = this.getPiece(kingLoc);

        List<Piece> threateningPieces = this.isInCheck(owner, kingLoc);
        List<String> kingMovesList = this.listValidMoves(king, kingLoc);
        List<String> dropList = this.listDropMoves(owner, kingLoc, threateningPieces);
        List<String> sacrificeMoves = this.listSacrificeMoves(owner, kingLoc, threateningPieces);
        List<String> allMoves = this.combineAndSortLists(kingMovesList, dropList, sacrificeMoves);

        return allMoves;
    }


    /**
     * This method is used to initialize a board (2D Piece array) given Initialization Information.
     * Used in File Mode Constructor
     * @param tc
     * @return a 2D Piece Array
     */
    private Piece[][] initializeBoard(Utils.TestCase tc) {
        List<Utils.InitialPosition> positions = tc.initialPieces;
        Piece[][] board = new Piece[BOARDSIZE][BOARDSIZE];
        Player lower = new Player(LOWER);
        Player upper = new Player(UPPER);
        this.lower = lower;
        this.upper = upper;

        this.initalizePlayerLists(tc.upperCaptures, tc.lowerCaptures);

        for (Utils.InitialPosition position : positions) {
            String pieceName = position.piece;
            Location loc = new Location(position.position);
            board[loc.getCol()][loc.getRow()] = this.createPiece(pieceName, loc);
            if (Character.isUpperCase(pieceName.charAt(pieceName.length() - 1))) {
                this.upper.addToBoardList(board[loc.getCol()][loc.getRow()]);
            } else {
                this.lower.addToBoardList(board[loc.getCol()][loc.getRow()]);
            }

        }

        return board;
    }

    /**
     * This method is used to initialize a board (2D Piece array) from scratch.
     * @return a 2D Piece Array
     */
    private Piece[][] initializeBoard() {
        Piece[][] board = new Piece[BOARDSIZE][BOARDSIZE];

        Player lower = new Player(LOWER);
        Player upper = new Player(UPPER);
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

        board[0][0] = new King(lower, new Location(0, 0));
        lower.addToBoardList(board[0][0]);

        board[4][3] = new Pawn(upper, new Location(4, 3));
        upper.addToBoardList(board[4][3]);
        board[0][1] = new Pawn(lower, new Location(0, 1));
        lower.addToBoardList(board[0][1]);

        return board;
    }

    /**
     * This method initializes the captured Lists of each of the Players
     * Used in File Mode
     * @param upperCaptures List of Strings representing captured Pieces for Upper player
     * @param lowerCaptures List of Strings representing captured Pieces for Lower player
     * @return void
     */
    private void initalizePlayerLists(List<String> upperCaptures, List<String> lowerCaptures) {
        for (String piece : upperCaptures) {
            if (piece.length() == 0) {
                continue;
            }
            this.upper.addToCapturedList(this.createPiece(piece, null));
        }
        for (String piece : lowerCaptures) {
            if (piece.length() == 0) {
                continue;
            }
            this.lower.addToCapturedList(this.createPiece(piece, null));
        }
    }

    /**
     * Concatenate three lists together into one and sort the final list.
     * @param list1 List 1 to be concatenated
     * @param list2 List 2 to be concatenated
     * @param list3 List 3 to be concatenated
     * @return a combined List
     */
    private List<String> combineAndSortLists(List<String> list1, List<String> list2, List<String> list3) {
        List<String> allMoves = Stream.concat(list1.stream(), Stream.concat(list2.stream(),
                list3.stream())).collect(Collectors.toList());
        Collections.sort(allMoves);

        return allMoves;
    }

    /**
     * This method checks whether a player owns the Piece they are moving.
     * @param player    Player to check
     * @param startLoc  Location of the Piece to check
     * @return a boolean - true if their Piece, false otherwise
     */
    private boolean isValidPlayer(Player player, Location startLoc) {
        if (! player.equals(this.getPiece(startLoc).getPlayer())) {
            return false;
        }

        return true;
    }

    // Execution methods

    /**
     * This method calls the move method in the this class, returns whether the method was called successfully
     * @param currPlayer    Player performing the action
     * @param input String representing the move action ("move a1 a2")
     * @return a boolean - true if move was successfully made, false otherwise
     */
    private boolean executeMove(Player currPlayer, String input) {
        boolean success;
        String[] inputSplit = input.split(" ");
        Location startLoc = new Location(inputSplit[1]);
        Location endLoc = new Location(inputSplit[2]);

        Piece capturedPiece = this.getPiece(endLoc);
        boolean wasPromoted = false;

        if(capturedPiece != null){
            if(capturedPiece.isPromoted())
                wasPromoted = true;
        }

        // Check whether input requests promotion
        boolean promoteCurrPiece = false;
        if (inputSplit.length == 4){
            if (inputSplit[3].equals("promote"))
                promoteCurrPiece = true;
            else {
                return false;
            }
        }

        // Check if moving piece belongs to currPlayer
        if (!isValidPlayer(currPlayer, startLoc)) {
            return false;
        }

        Piece currKing = currPlayer.getPieceFromBoard("k");

        // Check if moving piece is the King
        if (startLoc.equals(currKing.getLocation())) {
            boolean moveKingIntoCheck = this.isInCheckBoolean(currPlayer, endLoc);
            // Cannot move king into check, return false
            if (moveKingIntoCheck) {
                return false;
            } else {
                success = this.move(startLoc, endLoc);
            }
        } else {
            // Cannot move another piece that would put king into check, return false
            success = this.move(startLoc, endLoc);
            boolean inducedCheck = this.isInCheckBoolean(currPlayer, currKing.getLocation());
            if (inducedCheck) {
                // Restore initial position
                this.forceMove(endLoc, startLoc);
                return false;
            }
        }

        if (!success)
            return false;

        if (promoteCurrPiece) {
            success = this.promote(startLoc, endLoc);
            if (!success) {
                //  If promotion fails, any prior movement should be reversed as well
                this.forceMove(endLoc, startLoc);
                if(capturedPiece != null){
                    Player originalOwner = this.getOpponent(currPlayer);

                    // Restore captured piece to original position
                    currPlayer.removeFromCaptured(capturedPiece);
                    capturedPiece.setPlayer(originalOwner);
                    this.setPiece(endLoc, capturedPiece);
                    capturedPiece.setLocation(endLoc);
                    originalOwner.addToBoardList(capturedPiece);
                    if(wasPromoted)
                        capturedPiece.promote();

                }
            }
        }
        return success;
    }

    /**
     * This method calls the drop method in the this class, returns whether the method was called successfully
     * @param currPlayer    Player performing the action
     * @param input String representing the move action ("move a1 a2")
     * @return a boolean - true if drop was successfully made, false otherwise
     */
    private boolean executeDrop(Player currPlayer, String input) {
        boolean success = false;

        String[] inputSplit = input.split(" ");
        String dropPiece = inputSplit[1];
        Location dropLoc = new Location(inputSplit[2]);

        success = this.drop(currPlayer, dropPiece, dropLoc);

        return success;
    }


    /**
     * This method implements capturing a piece for a player and the board. It moves the Piece from the board to the
     * captor's captured list, and reassigns the respective fields of the Piece.
     * @param startLoc  source Location of the capturing piece.
     * @param endLoc    destination Location of capturing piece (original Location of captured piece)
     * @return void
     */
    private void capture(Location startLoc, Location endLoc) {
//        Initialize variables
        Piece captorPiece = this.getPiece(startLoc);
        Piece capturedPiece = this.getPiece(endLoc);
        Player captorPlayer = captorPiece.getPlayer();
        Player opponentPlayer = this.getOpponent(captorPlayer);

        capturedPiece.setPlayer(captorPlayer);
        capturedPiece.setLocation(null);
        captorPlayer.addToCapturedList(capturedPiece);
        opponentPlayer.removeFromBoard(capturedPiece);

        this.setPiece(endLoc, captorPiece);
        this.setPiece(startLoc, null);
    }

    /**
     * This method is only to be used for moving pieces back to original positions. They would have been moved for
     * testing (in method testCheckAfterAction()). This method ignores regular conventions of moving pieces.
     * @param startLoc  source Location for move
     * @param endLoc    destination Location for move
     * @return void
     */
    private void forceMove(Location startLoc, Location endLoc) {
        Piece startPiece = this.getPiece(startLoc);
        this.setPiece(endLoc, startPiece);
        this.setPiece(startLoc, null);
        startPiece.setLocation(endLoc);
    }

    /**
     * This method drops a Piece onto the board.
     * @param captor    Player who has captured the Piece.
     * @param pieceName String representing name of the Piece to be dropped
     * @param dropLoc   Location representing position to drop Piece on
     * @return a boolean - true if the drop was executed, false if any failure occured
     */
    private boolean drop(Player captor, String pieceName, Location dropLoc) {
        Piece checkPresencePiece = this.getPiece(dropLoc);
        if (pieceName.equals("p") || pieceName.equals("P")) {
            Piece pawn = captor.getPieceFromBoard("p");
            if(pawn != null) {
                if (dropLoc.getCol() == pawn.getLocation().getCol()) {
                    return false;
                }
            }
        }
        if (checkPresencePiece != null) {
            return false;
        }

        Piece dropPiece = captor.getPieceFromCaptured(pieceName);

        if (dropPiece == null)
            return false;


//        Check for dropping validity here (pawns cannot be in same column)
        if (!dropPiece.canDrop(dropLoc))
            return false;

        this.setPiece(dropLoc, dropPiece);
        dropPiece.setLocation(dropLoc);
        captor.moveToBoardList(dropPiece);

        return true;
    }

    /**
     * This method is used to promote a Piece. It calls the promote method within the Piece class if the Piece
     *  was in the right position to be promoted.
     * @param startLoc
     * @param endLoc
     * @return a boolean - true if promotion was executed, false if any failure occured
     */
    private boolean promote(Location startLoc, Location endLoc) {
        Piece piece = this.getPiece(endLoc);
        if (piece.getPlayer().isUpper()) {
            if (startLoc.getRow() != 0 && endLoc.getRow() != 0) {
                return false;
            }
        } else {
            if (startLoc.getRow() != 4 && endLoc.getRow() != 4) {
                return false;
            }
        }

        return piece.promote();
    }

    /**
     * This function creates a new Piece with a specific Location field. This is used in file mode to initialize pieces.
     * @param piece String indicating type of piece to be made
     * @param loc   Location representing position the Piece should be placed
     * @return a Piece object
     */
    private Piece createPiece(String piece, Location loc){
        String pieceName = piece.substring(piece.length() - 1);
        boolean promoted = false;
        if (piece.charAt(0) == '+') {
            promoted = true;
        }
        if (pieceName.equalsIgnoreCase("p")) {
            Player curr;
            if (pieceName.equals("p"))
                curr = lower;
            else
                curr = upper;
            Piece p = new Pawn(curr, loc);
            if (promoted) {
                p.forcePromote();
            }
            return p;
        } else if (pieceName.equalsIgnoreCase("b")) {
            Player curr;
            if (pieceName.equals("b"))
                curr = lower;
            else
                curr = upper;
            Piece b = new Bishop(curr, loc);
            if (promoted)
                b.forcePromote();
            return b;
        } else if (pieceName.equalsIgnoreCase("r")) {
            Player curr;
            if (pieceName.equals("r"))
                curr = lower;
            else
                curr = upper;
            Piece r = new Rook(curr, loc);
            if (promoted)
                r.forcePromote();
            return r;
        } else if (pieceName.equalsIgnoreCase("g")) {
            Player curr;
            if (pieceName.equals("g"))
                curr = lower;
            else
                curr = upper;
            Piece g = new GoldGeneral(curr, loc);
            if (promoted)
                g.forcePromote();
            return g;
        } else if (pieceName.equalsIgnoreCase("s")) {
            Player curr;
            if (pieceName.equals("s"))
                curr = lower;
            else
                curr = upper;
            Piece s = new SilverGeneral(curr, loc);
            if(promoted)
                s.forcePromote();
            return s;

        } else {
            Player curr;
            if (pieceName.equals("k"))
                curr = lower;
            else
                curr = upper;
            return new King(curr, loc);
        }
    }

    /**
     * This method returns a List of Pieces that are currently putting the inputted Player's king in check
     * @param owner Player whose King this method checks for
     * @param kingLoc   Location of where the king is located
     * @return a List - List of Pieces currently ready to capture the King
     */
    private List<Piece> isInCheck(Player owner, Location kingLoc) {
        List<Piece> threateningPieces = new ArrayList<>();
        Player opponent;
        if (owner.getName().equals("UPPER"))
            opponent = this.getLower();
        else
            opponent = this.getUpper();

        for (Piece p : opponent.getOnBoard()) {
            List<Location> moves = p.findValidPath(p.getLocation(), kingLoc);
            if (moves.size() == 0)
                continue;
            //  Check whether there is a piece in the way of the king and the attacking piece
            //  If there is, the king will not be in check from this piece, regardless of the type of piece blocking it.
            boolean blocked = false;
            for (Location loc : moves) {
                if (this.getPiece(loc) != null) {
                    if (this.getPiece(loc).getName().equalsIgnoreCase("k"))
                        continue; //continue, because we already know the king's location can be accessed.
                    else if (loc.equals(kingLoc))
                        continue;   // This edge case means that there was a piece there, but the king is considering capturing it.
                    // If the position of the move and kingLoc are the same, and there is already a piece there on the board
                    // We can ignore it, because the king will have captured it.
                    // See checkmate.out tester for example.
                    blocked = true;
                    break;
                }
            }
            if (!blocked) {
                threateningPieces.add(p);
            }
        }

        //  If size of threateningPieces = 0, that means the king is not in check.
        return threateningPieces;
    }

    /**
     * This method will return all valid moves the king can make to remove himself from check.
     * @param king - Piece object for the King
     * @param kingLoc - Location representing position of the King
     * @return a List - all locations the king can move to
     */
    private List<String> listValidMoves(Piece king, Location kingLoc) {
        Player owner = king.getPlayer();
        List<Location> moves = king.getValidMoves(kingLoc);
        List<String> retList = new ArrayList<>();

        for (Location l : moves) {
            if (this.getPiece(l) != null) {
                if (this.getPiece(l).getPlayer() == owner) {
                    continue;
                }
            }
            if (this.isInCheckBoolean(owner, l)) {
                continue;
            }
            retList.add(MOVE + " " + kingLoc.toString() + " " + l.toString());
        }

        return retList;
    }

    /**
     * This method will check whether the king will be in check after a specific move has been made.
     * This is to check for double/triple/... check.
     * @param action    String representing action after which we need to check if the King will be in check
     * @return a boolean - true if King in check after action
     */
    private boolean testCheckAfterAction(Player player, String action) {
        String[] actionSplit = action.split(" ");
        Location endLoc = new Location(actionSplit[2]);

        if (actionSplit[0].equals(MOVE)) {
            Location startLoc = new Location(actionSplit[1]);
            Piece target = this.getPiece(endLoc);
            boolean empty = true;
            boolean promoted = false;
            if (target != null) {
                empty = false;
                if(target.isPromoted()){
                    promoted = true;
                }
            }

            this.move(startLoc, endLoc);
            Piece king = player.getPieceFromBoard("k");
            boolean currKingCheck = this.isInCheckBoolean(player, king.getLocation());

//          Reverse move after testing
            this.forceMove(endLoc, startLoc);

            if (!empty) {
                Player originalOwner = this.getOpponent(player);

                player.removeFromCaptured(target);
                target.setPlayer(originalOwner);
                this.setPiece(endLoc, target);
                target.setLocation(endLoc);
                originalOwner.addToBoardList(target);
                if(promoted)
                    target.promote();
            }

            if (currKingCheck)
                return true;
            else
                return false;
        } else if (actionSplit[0].equals(DROP)) {
            String pieceName = actionSplit[1];
            int capturedIndex = player.getIndexFromCaptured(pieceName);
            this.drop(player, pieceName, endLoc);
            boolean currKingCheck = this.isInCheckBoolean(player, player.getKing().getLocation());
            this.unDrop(player, endLoc, capturedIndex);

            if (currKingCheck)
                return true;
        }

        return false;
    }

    /**
     * This function lists all possible drops that would remove a king from check.
     * @param owner Player whose king is in check
     * @param kingLoc   Location of the king in check
     * @param threateningPieces List of Pieces that are currently causing the king to be in check
     * @return a List of Strings - list of all drops
     */
    private List<String> listDropMoves(Player owner, Location kingLoc, List<Piece> threateningPieces) {
        List<String> dropStrings = new ArrayList<>();
        for (Piece piece : threateningPieces) {
            Location start = piece.getLocation();
            List<Location> positions = piece.findValidPath(start, kingLoc);
            for (Location position : positions) {
                if (position.equals(kingLoc))
                    continue;
                List<Piece> capturedPieces = new ArrayList<>(owner.getCaptured());
                for (Piece dropPiece : capturedPieces) {
                    String action = String.format("%s %s %s", DROP, dropPiece.getName().toLowerCase(), position.toString());
                    if (!testCheckAfterAction(owner, action)) {
                        dropStrings.add(action);
                    }
                }
            }
        }

        return dropStrings;
    }

    /**
     * This method returns a list of moves that a player can make to remove their king from check.
     * These moves are for pieces that are not a King
     * @param owner
     * @param kingLoc
     * @param threateningPieces
     * @return a List of moves a player can make to protect King from check, with pieces other than the King
     */
    private List<String> listSacrificeMoves(Player owner, Location kingLoc, List<Piece> threateningPieces) {
        List<String> moves = new ArrayList<>();
        for (Piece threateningPiece : threateningPieces) {
            Location start = threateningPiece.getLocation();
            List<Location> positions = threateningPiece.findValidPath(start, kingLoc);

            // Add start location of threatening piece; this location should be accessible to owner's pieces
            //   (if path exists)
            positions.add(start);

            // Create copy to avoid concurrent modification
            List<Piece> onBoardPieces = new ArrayList<>(owner.getOnBoard());
            for (Piece ownerPiece : onBoardPieces) {
                if (ownerPiece.getName().equalsIgnoreCase("k"))
                    continue;

                Location ownerPieceLoc = ownerPiece.getLocation();
                for (Location position : positions) {
                    // Check whether owner's piece can capture the threatening piece, or block its path
                    List<Location> path = ownerPiece.findValidPath(ownerPieceLoc, position);
                    // If valid path exists from owner's piece, check if path is obstructed
                    if (path.size() != 0) {
                        boolean obstructed = false;
                        for (Location l : path) {
                            // If the path is obstructed, ownerPiece cannot move there
                            // If the obstructing piece is the threatening piece, owner's piece can capture it
                            if (this.getPiece(l) != null && ! this.getPiece(l).equals(threateningPiece)) {
                                obstructed = true;
                                break;
                            }
                        }

                        if (!obstructed) {
                            String action = String.format("%s %s %s", MOVE, ownerPieceLoc, position.toString());
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
}
