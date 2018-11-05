import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class myShogi{
    public static void main(String[] args){
//        Initialize board
        Board board = new Board();
        Piece[][] boardArray = board.getBoard();

//        Initialize players (all pieces of one team are initialized to same player)
        Player lower = board.getLower();
        Player upper = board.getUpper();

        List<String[]> moves = new ArrayList<String[]>();

        String[] m1 = {"c1", "c2"};
        moves.add(m1);

        String[] mnew = {"c2", "c1"};
        moves.add(mnew);

        Scanner sc = new Scanner(System.in);
        System.out.println(Utils.stringifyBoard(boardArray));

        String input = sc.nextLine();


        while(! input.equals("quit")){
//            System.out.println(input);
            String[] positions = input.split(" ");
            System.out.println(positions[0] + " , " + positions[1]);

            board.move(positions[0], positions[1]);
            System.out.println(Utils.stringifyBoard(boardArray));

            System.out.print("UPPER's captured: ");
            for(String name : upper.getCaptured().keySet()){
                System.out.print(name);
            }

            System.out.println();
            System.out.print("lower's captured: ");
            for(String name : lower.getCaptured().keySet()){
                System.out.print(name);
            }

            System.out.println();

            input = sc.nextLine();

        }



    }



}
