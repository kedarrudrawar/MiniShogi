import java.util.List;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args){
//        Initialize board
        Board board = new Board();
        Piece[][] boardArray = board.getBoard();

//        Initialize players (all pieces of one team are initialized to same player)
        Player lower = board.getLower();
        Player upper = board.getUpper();

        List<String[]> moves = new ArrayList<String[]>();

        String[] m1 = {"c5", "d4"};
        moves.add(m1);

        String[] mnew = {"d4", "c3"};
        moves.add(mnew);

        String[] m2 = {"c3", "c4"};
        moves.add(m2);

//        String[] m3 = {"b2", "c1"};
//        moves.add(m3);


//        String[] m3 = {"d1", "e2"};
//        moves.add(m3);
//
//        String[] m4 = {"e1", "e3"};
//        moves.add(m4);


        for(String[] move : moves) {
            try {
                System.out.println("moving from " + move[0] + " to " + move[1]);
                board.move(move[0], move[1]);
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }


            System.out.println(Utils.stringifyBoard(board.getBoard()));


            System.out.print("Lower's captured: ");
            for(String key : lower.getCaptured().keySet()){
                System.out.print(key);
            }
            System.out.println();

            System.out.print("UPPER'S captured: ");
            for(String key : upper.getCaptured().keySet()){
                System.out.print(key);
            }
            System.out.println();
            System.out.println();

        }







    }



}
