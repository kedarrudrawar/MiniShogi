import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class myShogi to run the game miniShogi, in interactive or file mode.
 * @author Kedar Rudrawar
 */

public class myShogi {
    private Board board;


    public myShogi(){
        this.board = new Board();
    }

    public myShogi(Utils.TestCase tc){
        this.board = new Board(tc);
    }


    public void printInitialOutput(Player currPlayer){
        String boardString = Utils.stringifyBoard(board.getBoard());
        System.out.println(board.printBoardAndStats());
        System.out.print(currPlayer.getName() + "> ");
    }

    public void runInteractiveMode(){
//        Initialize board
        Board board = new Board();

//        Initialize players (all pieces of one team are initialized to same player)
        Player lower = board.getLower();
        Player upper = board.getUpper();

        Scanner sc = new Scanner(System.in);

        boolean lowerTurn = true;
        Player currPlayer = lower;
        Player opponentPlayer = upper;

//        Initial printing
        this.printInitialOutput(currPlayer);

        String input = sc.nextLine();

//        main loop

        while (upper.getTurnCount() < 200 || lower.getTurnCount() < 200) {
            String[] inputSplit = input.split(" ");
            String action = inputSplit[0];

            if (action.equals("quit"))
                System.exit(0);

            System.out.println(currPlayer.getName() + " player action: " + input);

            if (action.equals("move")) {
                board.executeMove(currPlayer, input);




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
//                        board.promote(startLoc, endLoc);
                }

                Piece opponentKing = opponentPlayer.getKing();
                Location opponentKingLoc = opponentKing.getLocation();
                List<Piece> threateningPieces = board.isInCheck(opponentPlayer, opponentKingLoc);

                boolean opponentKingCheck = (threateningPieces.size() != 0);
                if (opponentKingCheck) {
//                    System.out.println("calling listValidMoves on opponent's king");
                    List<String> movesList = board.listValidMoves(opponentKing, opponentKingLoc);
                    List<String> dropList = board.listDropMoves(opponentPlayer, opponentKingLoc, threateningPieces);
                    List<String> sacrificeMoves = board.listSacrificeMoves(opponentPlayer, opponentKingLoc, threateningPieces);


                    if (movesList.size() == 0) {
                        System.out.println(currPlayer.getName() + " player has won.");
                        System.exit(0);
                    }
                    System.out.println(opponentPlayer.getName() + " player is in check!");
                    System.out.println("Available moves: ");
                    for (String kingMove: movesList) {
                        System.out.println(String.format("move %s %s", opponentKingLoc.toString(), kingMove));
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

//                    board.executeDrop(currPlayer, dropPiece, dropLoc);

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

    public void runFileMode(Utils.TestCase tc){
        Board board = new Board(tc);
        boolean lowerTurn = true;
        Player currPlayer = board.getLower();
        Player opponentPlayer = board.getUpper();
        boolean success = true;
        boolean checkmate = false;
        boolean lastMove = false;

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

            success = board.executeCommand(currPlayer, command);
            if (!success) {
                System.out.println(currPlayer.getName() + " player action: " + command);
                System.out.println(board.printBoardAndStats());
                System.out.println(opponentPlayer.toString() + " player wins.  Illegal move.");
                System.exit(0);
            }


            Piece opponentKing = opponentPlayer.getKing();
            Location opponentKingLoc = opponentKing.getLocation();
            boolean opponentKingInCheck = board.isInCheckBoolean(opponentPlayer, opponentKingLoc);

            if (opponentKingInCheck) {

                List<Piece> threateningPieces = board.isInCheck(opponentPlayer, opponentKingLoc);

                List<String> kingMovesList = board.listValidMoves(opponentKing, opponentKing.getLocation());
                List<String> dropList = board.listDropMoves(opponentPlayer, opponentKingLoc, threateningPieces);
                List<String> sacrificeMoves = board.listSacrificeMoves(opponentPlayer, opponentKingLoc, threateningPieces);

                List<String> allMoves = this.combineAndSortLists(kingMovesList, dropList, sacrificeMoves);


                if (allMoves.size() == 0) {
                    success = false;
                    checkmate = true;
                } else {
                    availableMoves = board.printCheckOutput(opponentPlayer, allMoves);
                }
            }

            if(!success || lastMove){
                System.out.println(currPlayer.getName() + " player action: " + command);
            }


            /*
            Flip turns here:
             */

            lowerTurn = !lowerTurn;

            if (lowerTurn) {
                currPlayer = board.getLower();
                opponentPlayer = board.getUpper();
            } else {
                currPlayer = board.getUpper();
                opponentPlayer = board.getLower();
            }

            if(lastMove) {
//                Check for illegal pawn drop here (checkmate)
                if(action.equals("drop")) {
                    if (commandSplit[1].equalsIgnoreCase("p")) {
                        if(board.illegalPawnDrop(success, checkmate, opponentPlayer, new Location(commandSplit[2]), capturedIndex)){
                            System.out.println(board.printBoardAndStats());
                            System.out.print(availableMoves);
                            System.out.println(currPlayer.toString() + " player wins.  Illegal move.");
                            System.exit(0);
                        }
                    }
                }


                System.out.println(board.printBoardAndStats());
                System.out.print(availableMoves);
                if (!success) {
                    if(checkmate){
                        System.out.println(opponentPlayer.toString() + " player wins.  Checkmate.");
                        System.exit(0);
                    }
                    else {
                        System.out.println(currPlayer.toString() + " player wins.  Illegal move.");
                        System.exit(0);
                    }
                }
                System.out.println(currPlayer.getName() + "> ");
            }
            currPlayer.incrementTurn();

        }

    }

    public List<String> combineAndSortLists(List<String> list1, List<String> list2, List<String> list3){
        List<String> allMoves = Stream.concat(list1.stream(), Stream.concat(list2.stream(),
                list3.stream())).collect(Collectors.toList());
        Collections.sort(allMoves);
        return allMoves;
    }


    public static void main(String[] args) {
        if (args[0].equals("-f")) {
            Utils.TestCase tc = null;
//            System.out.println("path : " + args[1]);
            try {
                tc = Utils.parseTestCase(args[1]);
            } catch (Exception e) {
                System.out.println("caught exception");
            }

            myShogi game = new myShogi(tc);
            game.runFileMode(tc);
        }


        else {
            myShogi game = new myShogi();
            game.runInteractiveMode();
        }

    }
}




