import java.util.*;


public class Board {
    private Piece[][] board;
    private Location upperKingPos;
    private Location lowerKingPos;


    public Board() {
        this.board = this.initializeBoard();
    }


    private Piece[][] initializeBoard() {
        Piece[][] board = new Piece[5][5];

        Player lower = new Player("lower");
        Player upper = new Player("UPPER");

        board[0][4] = new Rook(upper, new Location(0,4));
        board[4][0] = new Rook(lower, new Location(4,0));


        board[1][4] = new Bishop(upper);
        board[3][0] = new Bishop(lower);

        board[2][4] = new SilverGeneral(upper);
        board[2][0] = new SilverGeneral(lower);

        board[3][4] = new GoldGeneral(upper);
        board[1][0] = new GoldGeneral(lower);

        board[4][4] = new King(upper);
        board[0][0] = new King(lower);

        board[4][3] = new Pawn(upper);
        board[0][1] = new Pawn(lower);

        return board;
    }


// GETTER methods

    public Player getLower(){
        return this.board[4][0].getPlayer();
    }

    public Player getUpper(){
        return this.board[0][4].getPlayer();
    }


    public Piece[][] getBoard() {
        return this.board;
    }

    public Piece getPiece(Location pos) {
        return this.board[pos.getCol()][pos.getRow()];
    }


// Setter method
    public void setPiece(Location pos, Piece piece){
        this.board[pos.getCol()][pos.getRow()] = piece;
    }


//  HELPER methods
    public void canPromote(Location endPos) throws IllegalArgumentException{
        Piece piece = this.getPiece(endPos);
        if(piece.getPlayer().getName() == "UPPER"){
            if(endPos.getRow() != 0)
                throw new IllegalArgumentException("Cannot promote.");
        }
        else{
            if(endPos.getRow() != 1)
                throw new IllegalArgumentException("Cannot promote.");
        }

        piece.promote();
    }




// ACTION methods

    public void capture(Location startPos, Location endPos) {
        Piece captorPiece = this.getPiece(startPos);
        Piece capturedPiece = this.getPiece(endPos);
        Player captorPlayer = captorPiece.getPlayer();
        capturedPiece.setPlayer(captorPlayer);
        captorPlayer.capture(capturedPiece);


        this.setPiece(endPos, captorPiece);
        this.setPiece(startPos, null);
    }


    public void move(String startPos, String endPos) throws IllegalArgumentException {

        Location start = new Location(startPos);
        Location end = new Location(endPos);

        Piece startPiece = this.getPiece(start);

        if(startPiece == null)
            throw new IllegalArgumentException("Illegal move.");

        List<Location> positions = startPiece.findValidPath(start, end);


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
            if(endPiece.getPlayer() == startPiece.getPlayer()) {
                if(endPiece.getPlayer().getName().equals("UPPER"))
                    System.out.println("lower player wins. Illegal move.");
                else
                    System.out.println("UPPER player wins. Illegal move.");
                System.exit(0);
            }
            if(endPiece.isPromoted())
                endPiece.demote();
            this.capture(start, end);
        }

//        Check whether own player is in check



//        Check whether opponent player is in check




        startPiece.setPosition(end);
    }

    public void drop(Player captor, String pieceName, String position) throws IllegalArgumentException {
        Location dropPos = new Location(position);
        Piece checkPiece = this.getPiece(dropPos);

        if (checkPiece != null) {
            throw new IllegalArgumentException("Desired position to drop piece is currently occupied by: " +
                    checkPiece.getName());
        }

        if(captor.getName().equals("UPPER")){
            if(dropPos.getRow() == 0)
                throw new IllegalArgumentException("Cannot drop into promotion zone.");
        }
        else{
            if(dropPos.getRow() == 4)
                throw new IllegalArgumentException("Cannot drop into promotion zone");
        }



        Map<String, Piece> capturedMap = captor.getCaptured();

        Piece dropPiece = capturedMap.get(pieceName);

        if(dropPiece == null)
            throw new IllegalArgumentException("You have not captured this piece.");

//        Check for dropping validity here (pawns cannot be in same column)
        if(! dropPiece.canDrop(dropPos)){

        }



        this.setPiece(dropPos, dropPiece);

        if (capturedMap.containsKey(pieceName))
            captor.getCaptured().remove(pieceName);
    }

    public void promote(String position){
        Location promPos = new Location(position);
        Piece piece = this.getPiece(promPos);
        if(piece.getPlayer().getName() == "UPPER"){
            if(promPos.getRow() != 0)
                throw new IllegalArgumentException("Cannot promote.");
        }
        else{
            if(promPos.getRow() != 4)
                throw new IllegalArgumentException("Cannot promote.");
        }

        piece.promote();
    }


    public void inCheck(){

    }







}