import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class myShogi {
    public static void main(String[] args) {
//        INITIALIZE GAME MODE.

//        File mode:
        if (args[0].equals("-f")) {
            Utils.TestCase tc = null;
//            System.out.println("path : " + args[1]);
            try {
                tc = Utils.parseTestCase(args[1]);
            } catch (Exception e) {
                System.out.println("caught exception");
            }
            Board board = new Board(tc);

            board.runFileMode(tc.moves);

            System.exit(0);

        }


//            Interactive mode

        else {
//        Initialize board
            Board board = new Board();
            Piece[][] boardArray = board.getBoard();


//        Initialize players (all pieces of one team are initialized to same player)
            Player lower = board.getLower();
            Player upper = board.getUpper();

            Scanner sc = new Scanner(System.in);

            boolean lowerTurn = true;
            Player currPlayer = lower;
            Player opponentPlayer = upper;

//        Initial printing
            String boardString = Utils.stringifyBoard(board.getBoard());
            System.out.println(boardString);
            System.out.print("Captures UPPER: ");
            for (Piece piece : upper.getCaptured())
                System.out.print(piece.toString() + " ");
            System.out.println();
            System.out.print("Captures lower: ");
            for (Piece piece : lower.getCaptured())
                System.out.print(piece.toString() + " ");
            System.out.println("\n");
            System.out.print(currPlayer.getName() + "> ");

            String input = sc.nextLine();

//        main loop

            while (upper.getTurnCount() < 200 || lower.getTurnCount() < 200) {
                String[] inputSplit = input.split(" ");
                String action = inputSplit[0];

                if (action.equals("quit"))
                    System.exit(0);

                System.out.println(currPlayer.getName() + " player action: " + input);

                if (action.equals("move")) {
                    String startPos = inputSplit[1];
                    String endPos = inputSplit[2];

                    Location startLoc = new Location(startPos);
                    Location endLoc = new Location(endPos);

                    boolean promote = false;

                    if (inputSplit.length == 4) {
                        if (inputSplit[3].equals("promote"))
                            promote = true;
                    }

                    if (board.getPiece(startLoc).getPlayer() != currPlayer) {
                        System.out.println("Moving piece that is not yours. Illegal move.");
                        System.exit(0);
                    }

                    Piece currKing = currPlayer.getKing();

//                This is to check whether the piece that's moving is the king. If it is, we need to check whether the destination
//                of the king will put it in check. If it is not, then we just need to check the original position of the king.
                    if (startLoc.equals(currKing.getLocation())) {
                        boolean currKingCheck = board.isInCheckBoolean(currPlayer, endLoc);

//                    If moving the king to this location puts king in check, this is illegal.
                        if (currKingCheck) {
                            System.out.println("Moving your piece into check. Illegal move.");
                            System.exit(0);
                        }
                        board.move(startLoc, endLoc);
                    } else {
                        board.move(startLoc, endLoc);
                        boolean currKingCheck = board.isInCheckBoolean(currPlayer, currKing.getLocation());
                        if (currKingCheck) {
                            System.out.println("Moving your piece into check. Illegal move.");
                            System.exit(0);
                        }
                    }

                    if (promote) {
                        System.out.println("about to promote");
                        board.promote(startLoc, endLoc);
                    }

                    Piece opponentKing = opponentPlayer.getKing();
                    Location opponentKingLoc = opponentKing.getLocation();
                    List<Piece> threateningPieces = board.isInCheck(opponentPlayer, opponentKingLoc);

                    boolean opponentKingCheck = (threateningPieces.size() != 0);
                    if (opponentKingCheck) {
//                    System.out.println("calling listValidMoves on opponent's king");
                        List<Location> movesList = board.listValidMoves(opponentKing, opponentKingLoc);
                        List<String> dropList = board.listDropMoves(opponentPlayer, opponentKingLoc, threateningPieces);
                        List<String> sacrificeMoves = board.listSacrificeMoves(opponentPlayer, opponentKingLoc, threateningPieces);

                        if (movesList.size() == 0) {
                            System.out.println(currPlayer.getName() + " player has won.");
                            System.exit(0);
                        }
                        System.out.println(opponentPlayer.getName() + " player is in check!");
                        System.out.println("Available moves: ");
                        for (Location loc : movesList) {
                            System.out.println(String.format("move %s %s", opponentKingLoc.toString(), loc.toString()));
                        }
                        for(String s : dropList){
                            System.out.println(s);
                        }
                        for(String sacrifice : sacrificeMoves){
                            System.out.println(sacrifice);
                        }
                    }

                } else if (action.equals("drop")) {
                    String dropPiece = inputSplit[1];
                    String dropPos = inputSplit[2];

                    Location dropLoc = new Location(dropPos);

                    board.drop(currPlayer, dropPiece, dropLoc);

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

                System.out.println(Utils.stringifyBoard(board.getBoard()));
                System.out.print("Captures UPPER: ");
                for (Piece piece: upper.getCaptured())
                    System.out.print(piece.toString() + " ");
                System.out.println();
                System.out.print("Captures lower: ");
                for (Piece piece: lower.getCaptured())
                    System.out.print(piece.toString() + " ");
                System.out.println("\n");

                System.out.print(currPlayer.getName() + "> ");

                input = sc.nextLine();
            }

        }


    }
}




