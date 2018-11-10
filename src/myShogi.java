import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class myShogi {
    public static void main(String[] args) {
//        INITIALIZE GAME MODE.

//        File mode:
        if (args[0].equals("-f")) {
            Utils.TestCase tc = null;
            System.out.println("path : " + args[1]);
            try {
                tc = Utils.parseTestCase(args[1]);
            } catch (Exception e) {
                System.out.println("caught exception");
            }
            Board board = new Board(tc.initialPieces);

            board.movePieces(tc.moves);


            System.exit(0);


//            Interactive mode

        } else {


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
            for (String name : upper.getCaptured().keySet())
                System.out.print(name + " ");
            System.out.println();
            System.out.print("Captures lower: ");
            for (String name : lower.getCaptured().keySet())
                System.out.print(name + " ");
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


//                TODO: utilize the check method in here rather than in board.java: move()

                    /**

                     //        Check whether lower player is in check




                     if (isInCheck(lower, lowerKingPos)) {
                     //            System.out.println("calling listValidMoves on lowerKingPos");
                     List<Location> movesList = listValidMoves(lowerKing, lowerKingPos);
                     if (movesList.size() == 0) {
                     System.out.println("UPPER player has won.");
                     System.exit(0);
                     }

                     System.out.println("lower player is in check!");
                     System.out.print("Available moves: ");
                     for (Location move : movesList) {
                     System.out.println(String.format("move %s %s", lowerKingPos.toString(), move.toString()));
                     }
                     //            System.out.println(movesString);
                     }

                     //        Check whether upper player is in check
                     if (board.isInCheck(upper, upperKingPos)) {
                     //            System.out.println("calling listValidMoves on upperKingPos");
                     List<Location> movesList = listValidMoves(upperKing, upperKingPos);
                     if (movesList.size() == 0) {
                     System.out.println("lower player has won.");
                     System.exit(0);
                     }

                     System.out.println("UPPER player is in check!\n");
                     System.out.println("Available moves: ");
                     for (Location move : movesList) {
                     System.out.println(String.format("move %s %s", upperKingPos.toString(), move.toString()));
                     }
                     System.out.println();
                     }
                     return false;

                     */


                    Piece currKing = currPlayer.getKing();

//                This is to check whether the piece that's moving is the king. If it is, we need to check whether the destination
//                of the king will put it in check. If it is not, then we just need to check the original position of the king.
                    if (startLoc.equals(currKing.getLocation())) {
                        boolean currKingCheck = (board.isInCheck(currPlayer, endLoc).size() != 0);

//                    If moving the king to this location puts king in check, this is illegal.
                        if (currKingCheck) {
                            System.out.println("Moving your piece into check. Illegal move.");
                            System.exit(0);
                        }
                        board.move(startLoc, endLoc);
                    } else {
                        board.move(startLoc, endLoc);
                        boolean currKingCheck = (board.isInCheck(currPlayer, currKing.getLocation()).size() != 0);
                        if (currKingCheck) {
                            System.out.println("Moving your piece into check. Illegal move.");
                            System.exit(0);
                        }
                    }

                    if (promote) {
                        System.out.println("about to promote");
                        board.promote(endPos);
                    }

                    Piece opponentKing = opponentPlayer.getKing();
                    Location opponentKingLoc = opponentKing.getLocation();
                    List<Piece> threateningPieces = board.isInCheck(opponentPlayer, opponentKingLoc);

                    boolean opponentKingCheck = (threateningPieces.size() != 0);
                    if (opponentKingCheck) {
//                    System.out.println("calling listValidMoves on opponent's king");
                        List<Location> movesList = board.listValidMoves(opponentKing, opponentKingLoc);
                        List<String> dropList = board.getDropMoves(opponentPlayer, opponentKingLoc, threateningPieces);


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
                for (String name : upper.getCaptured().keySet())
                    System.out.print(name + " ");
                System.out.println();
                System.out.print("Captures lower: ");
                for (String name : lower.getCaptured().keySet())
                    System.out.print(name + " ");
                System.out.println("\n");

                System.out.print(currPlayer.getName() + "> ");

                input = sc.nextLine();
            }

        }


    }
}




