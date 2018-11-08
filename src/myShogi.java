import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class myShogi {
    public static void main(String[] args) {
//        Initialize board
        Board board = new Board();
        Piece[][] boardArray = board.getBoard();


//        Initialize players (all pieces of one team are initialized to same player)
        Player lower = board.getLower();
        Player upper = board.getUpper();

        Scanner sc = new Scanner(System.in);

        boolean lowerTurn = true;
        Player currPlayer = lower;


//        Initial printing

        String boardString = Utils.stringifyBoard(board.getBoard());
        System.out.println(boardString);
        System.out.println();
        System.out.print("Captures UPPER: ");
        for (String name : upper.getCaptured().keySet())
            System.out.print(name + " ");
        System.out.println();
        System.out.print("Captures lower: ");
        for (String name : lower.getCaptured().keySet())
            System.out.print(name + " ");
        System.out.println();
        System.out.print(currPlayer.getName() + ">");


//        Piece lowerKing = board.getPiece(new Location(0, 0));

//        lowerKing.getValidMoves(new Location(2, 2));


        String input = sc.nextLine();

        while(upper.getTurnCount() < 200 || lower.getTurnCount() < 200) {
            String[] inputSplit = input.split(" ");
            String action = inputSplit[0];

            if (action.equals("quit"))
                System.exit(0);


            if (action.equals("move")) {
                String startPos = inputSplit[1];
                String endPos = inputSplit[2];

                boolean promote = false;

                if (inputSplit.length == 4)
                    promote = true;

                if (board.getPiece(new Location(startPos)).getPlayer() != currPlayer)
                    throw new IllegalArgumentException("Illegal move");


                board.move(startPos, endPos);

                if (promote) {
                    System.out.println("about to promote");
                    board.promote(endPos);
                }

            } else if (action.equals("drop")) {
                String dropPiece = inputSplit[1];
                String dropPos = inputSplit[2];
                board.drop(currPlayer, dropPiece, dropPos);

            } else {
                throw new IllegalArgumentException("Illegal input.");
            }


            currPlayer.incrementTurn();
//            Flip turn:
            lowerTurn = !lowerTurn;


            if (lowerTurn)
                currPlayer = lower;
            else
                currPlayer = upper;


//            Print output:
            boardString = Utils.stringifyBoard(board.getBoard());
            System.out.println(boardString);
            System.out.println();
            System.out.print("Captures UPPER: ");
            for (String name : upper.getCaptured().keySet())
                System.out.print(name + " ");
            System.out.println();
            System.out.print("Captures lower: ");
            for (String name : lower.getCaptured().keySet())
                System.out.print(name + " ");
            System.out.println();

            System.out.print(currPlayer.getName() + ">");


            input = sc.nextLine();
        }

    }


}




