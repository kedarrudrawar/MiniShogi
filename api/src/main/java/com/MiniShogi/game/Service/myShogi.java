package com.MiniShogi.game.Service;

import java.util.List;
import java.util.Scanner;

/**
 * The class myShogi to run the game miniShogi, in interactive or file mode.
 *
 * @author Kedar Rudrawar
 */
public class myShogi {
    private static final int TURNLIMIT = 200;
    private static final String ILLEGALSTRING = " player wins.  Illegal move.";
    private static final String CHECKMATESTRING = " player wins.  Checkmate.";
    private static final String TIESTRING = " Tie game.  Too many moves.";

    public Board board;

    private Player currentPlayer;
    private Player opponentPlayer;

    private boolean lowerTurn;
    private String availableMoves;


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }


    /**
     * No - arg constructor for myShogi.
     * Instantiates new Board object.
     */
    public myShogi() {
        this.board = new Board();
        this.currentPlayer = this.board.getLower();
        this.opponentPlayer = this.board.getUpper();
        this.availableMoves = "";
        this.lowerTurn = true;
    }

    /**
     * Constructor for myShogi - used in File mode.
     * @param tc    TestCase object containing initialization data
     */
    public myShogi(Utils.TestCase tc) {
        this.board = new Board(tc);
    }

    /**
     * This method prints out the current state of the game.
     * @return void
     */
    private void printBoardAndStats() {
        Utils.stringifyBoard(this.board.getBoardArray());
        System.out.println(this.board.printBoardAndStats());
    }

    /**
     * This method prints current game state and a string indicating opponent win due to an illegal move.
     * @param winnerPlayer  Player that won
     * @return void
     */
    private void printIllegalMoveOutput(Player winnerPlayer) {
        System.out.println(this.board.printBoardAndStats());
        System.out.println(winnerPlayer.toString() + ILLEGALSTRING);
    }

    /**
     * This method prints current game state and a string indicating opponent win due to checkmate.
     * @param winnerPlayer  Player that won
     * @return void
     */
    private void printCheckmateOutput(Player winnerPlayer) {
        System.out.println(this.board.printBoardAndStats());
        System.out.println(winnerPlayer.toString() + CHECKMATESTRING);
    }

    /**
     * This method checks whether the turn count limit has been reached.
     * If it has, it will output a String indicating a tie.
     * @return a boolean - true if reached limit, false otherwise
     */
    private boolean moveCountLimit() {
        if (board.getUpper().getTurnCount() == TURNLIMIT && board.getLower().getTurnCount() == TURNLIMIT) {
            System.out.println(TIESTRING);
            return true;
        }
        return false;
    }


    public boolean performTurn(String input){
        this.availableMoves = "";
        boolean success = this.board.executeCommand(this.currentPlayer, input);
        if (!success) {
            this.printIllegalMoveOutput(this.opponentPlayer);
            return false;
        }

        Piece opponentKing = this.opponentPlayer.getKing();
        Location opponentKingLoc = opponentKing.getLocation();
        boolean opponentKingInCheck = this.board.isInCheckBoolean(this.opponentPlayer, opponentKingLoc);

        //Check if move placed opponent in check
        if (opponentKingInCheck) {
            List<String> allMoves = this.board.listAllAvailableMoves(this.opponentPlayer, opponentKingLoc);

            if (allMoves.size() == 0) {
                this.printCheckmateOutput(this.currentPlayer);
                System.exit(0);
            } else {
                this.availableMoves = this.board.printCheckOutput(this.opponentPlayer, allMoves);
            }
        }

        this.currentPlayer.incrementTurn();

        // Flip turn
        this.lowerTurn = !this.lowerTurn;
        Player temp = this.currentPlayer;
        this.currentPlayer = this.opponentPlayer;
        this.opponentPlayer = temp;

        this.printBoardAndStats();
        return success;
    }

    /**
     * This method runs the interactive mode of the game. Calls for user input at each turn, while constantly writing
     * to output.
     * @return void
     */
    public void runInteractiveMode() {
        this.board = new Board();

        // Initialize players (all pieces of one team are initialized to same player)
        Player lower = this.board.getLower();
        Player upper = this.board.getUpper();

        boolean success;

        Scanner sc = new Scanner(System.in);

        // This string will store any available moves if a king is in check. Reset to "" every move.
        String availableMoves = "";

        // Initial print
        this.printBoardAndStats();
        System.out.print(this.currentPlayer.getName() + "> ");

        String input = sc.nextLine();

        while (upper.getTurnCount() < TURNLIMIT || lower.getTurnCount() < TURNLIMIT) {
            if (input.equals("quit"))
                return;

            System.out.println(this.currentPlayer.getName() + " player action: " + input);

            this.performTurn(input);

            this.printBoardAndStats();
            System.out.print(availableMoves);
            System.out.print(this.currentPlayer.getName() + "> ");

            input = sc.nextLine();
        }
    }

    /**
     * This method runs the file mode of the game. Takes an input TestCase file to initialize board, runs a series of
     * inputted commands, and outputs the game state at the end.
     * @param tc    TestCase Object containing initialization info
     * @return void
     */
    private void runFileMode(Utils.TestCase tc) {
        this.board = new Board(tc);

        Player currPlayer = this.board.getLower();
        Player opponentPlayer = this.board.getUpper();
        boolean lowerTurn = true;
        boolean checkmate = false;
        boolean lastMove = false;
        boolean success;

        // This string will store any available moves if a king is in check. Reset to "" every move.
        String availableMoves;

        for (int i = 0; i < tc.moves.size(); i++) {
            String command = tc.moves.get(i);
            String[] commandSplit = command.split(" ");
            String action = commandSplit[0];
            int capturedIndex = 0;
            availableMoves = "";

            if (i == tc.moves.size() - 1) {
                lastMove = true;
            }

            success = this.board.executeCommand(currPlayer, command);
            if (!success) {
                System.out.println(currPlayer.getName() + " player action: " + command);
                this.printIllegalMoveOutput(opponentPlayer);
                return;
            }

            Piece opponentKing = opponentPlayer.getKing();
            Location opponentKingLoc = opponentKing.getLocation();
            boolean opponentKingInCheck = this.board.isInCheckBoolean(opponentPlayer, opponentKingLoc);

            if (opponentKingInCheck) {
                List<String> allMoves = this.board.listAllAvailableMoves(opponentPlayer, opponentKingLoc);
                if (allMoves.size() == 0) {
                    success = false;
                    checkmate = true;
                } else {
                    availableMoves = this.board.printCheckOutput(opponentPlayer, allMoves);
                }
            }

            if (!success || lastMove) {
                System.out.println(currPlayer.getName() + " player action: " + command);
            }

            // Flip turn
            lowerTurn = !lowerTurn;

            if (lowerTurn) {
                currPlayer = this.board.getLower();
                opponentPlayer = this.board.getUpper();
            } else {
                currPlayer = this.board.getUpper();
                opponentPlayer = this.board.getLower();
            }

            // check legality of pawn drop
            if (action.equals("drop")) {
                String pieceName = commandSplit[1];
                Location dropLoc = new Location(commandSplit[2]);
                if (pieceName.equalsIgnoreCase("p")) {
                    if (this.board.illegalPawnDrop(success, checkmate, opponentPlayer, dropLoc, capturedIndex)) {
                        this.printIllegalMoveOutput(currPlayer);
                        return;
                    }
                }
            }

            // Output only on last move
            if (lastMove) {
                if (!success) {
                    if (checkmate) {
                        this.printCheckmateOutput(opponentPlayer);
                    } else {
                        this.printIllegalMoveOutput(currPlayer);
                    }
                    return;
                }

                System.out.println(this.board.printBoardAndStats());
                System.out.print(availableMoves);

                currPlayer.incrementTurn();
                if (moveCountLimit()) {
                    System.exit(0);
                }
                System.out.println(currPlayer.getName() + "> ");

            } else {
                currPlayer.incrementTurn();
                if (moveCountLimit()) {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * This is the Main method. Run miniShogi here.
     * @param args  '-i' for interactive mode, '-f [path]' for file mode
     * @return void
     */
    public static void main(String[] args) {
        if (args[0].equals("-f")) {
            Utils.TestCase tc = null;
            try {
                tc = Utils.parseTestCase(args[1]);
            } catch (Exception e) {
                System.out.println("caught exception");
            }

            myShogi game = new myShogi(tc);
            game.runFileMode(tc);

        } else {
            myShogi game = new myShogi();
            game.runInteractiveMode();
        }

    }
}




