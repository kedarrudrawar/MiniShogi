import javafx.util.*;

import java.util.*;

public abstract class Piece {
    //    Fields
    protected Player player;
    protected String name;
    protected Piece promotionPiece;


    //    Defined methods
    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        if (this.player.getName().equals("UPPER"))
            return this.name;
        else
            return this.name.toLowerCase();

    }

    public Player getPlayer() {
        return this.player;
    }


    public void promote() {
        this.name = "+" + this.name;
    }


    //    Abstract methods
    abstract List<Location> findValidPath(Location startPos, Location endPos);
}

class Bishop extends Piece {
    public Bishop(Player player) {
        this.name = "B";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {

        List<Location> retList = new ArrayList<Location>();

        int colDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if (colDiff != rowDiff) {
            return retList;
        }

        int startCol = Math.min(startPos.getCol(), endPos.getCol());
        int startRow = Math.min(startPos.getRow(), endPos.getRow());

        int endCol = Math.max(startPos.getCol(), endPos.getCol());

        int j = startRow;
        for (int i = startCol; i < endCol; i++) {
            retList.add(new Location(i, j));
            j++;
        }


        return retList;
    }
}


class Rook extends Piece {

    public Rook(Player player) {
        this.name = "R";
        this.player = player;
        this.promotionPiece = new King(player);
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            for (Location l : validPromotedMoves) {
                retList.add(l);
            }
        }

        if (startPos.getCol() != endPos.getCol()) {
            if (startPos.getRow() != endPos.getRow()) {
                return retList;
            }
        }

//        Check if movement is horizontal or vertical:

        boolean horizontal = false;
        if (startPos.getRow() == endPos.getRow()) {
            horizontal = true;
        }


        if (horizontal) {
            int startCol = Math.min(startPos.getCol(), endPos.getCol());
            int endCol = Math.max(startPos.getCol(), endPos.getCol());

            int row = startPos.getRow();

            for (int col = startCol; col < endCol; col++) {
                retList.add(new Location(col, row));
            }
        } else {
            int startRow = Math.min(startPos.getRow(), endPos.getRow());
            int endRow = Math.max(startPos.getRow(), endPos.getRow());

            int col = startPos.getCol();

            for (int row = startRow; row < endRow; row++) {
                retList.add(new Location(col, row));
            }
        }
        return retList;
    }
}


class Pawn extends Piece {
    public Pawn(Player player) {
        this.name = "P";
        this.player = player;
        this.promotionPiece = new GoldGeneral(player);
    }


    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            for (Location l : validPromotedMoves)
                retList.add(l);
        }


        if (endPos.getCol() != startPos.getCol()) {
            return retList;
        }
        if (Math.abs(endPos.getRow() - startPos.getRow()) == 1)
            retList.add(endPos);
        return retList;
    }
}


class King extends Piece {
    public King(Player player) {
        this.name = "K";
        this.player = player;
        this.promotionPiece = null;
    }


    @Override
    public void promote() throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot promote King. Illegal move.");
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<Location>();
        if (Math.abs(startPos.getCol() - endPos.getCol()) <= 1 && Math.abs(startPos.getRow() - endPos.getRow()) <= 1) {
            retList.add(endPos);
        }
        return retList;

    }
}


class SilverGeneral extends Piece {
    public SilverGeneral(Player player) {
        this.name = "S";
        this.player = player;
    }

    public List<Location> findValidPath(Location startPos, Location endPos) {
        List<Location> retList = new ArrayList<>();
//        Check if promoted:
        if (this.name.charAt(0) == '+') {
            List<Location> validPromotedMoves = this.promotionPiece.findValidPath(startPos, endPos);
            return validPromotedMoves;
        }

        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());

//        if movement is more than 1 unit, move is illegal
        if (columnDiff > 1 || rowDiff > 1)
            throw new IllegalArgumentException("Illegal move");

//        this indicates diagonal movement, which is okay in any direction
        if (columnDiff != 0) {
            if (rowDiff != 0) {
                retList.add(endPos);
                return retList;
            }
        }

        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        if (endPos.getRow() > (multiplier * startPos.getRow())) {
            retList.add(endPos);
            return retList;
        } else {
            throw new IllegalArgumentException("Illegal move");
        }
    }
}

class GoldGeneral extends Piece {
    public GoldGeneral(Player player) {
        this.name = "G";
        this.player = player;
    }

    @Override
    public void promote() throws IllegalArgumentException {
        throw new IllegalArgumentException("Gold General cannot be promoted. Illegal Move");
    }

    public List<Location> findValidPath(Location startPos, Location endPos) throws IllegalArgumentException {
//        check if end position is greater than one unit away from start
        int columnDiff = Math.abs(startPos.getCol() - endPos.getCol());
        int rowDiff = Math.abs(startPos.getRow() - endPos.getRow());
        if (columnDiff > 1 || rowDiff > 1)
            throw new IllegalArgumentException("Illegal move");

//        if player is UPPER, their piece's forward movement is vertically negative, so we will use a negative multipler
//          to identify validity of the move
        int multiplier = 1;
        if (this.player.getName().equals("UPPER"))
            multiplier = -1;

        List<Location> retList = new ArrayList<Location>();

        if (columnDiff != 0) {
            if ((multiplier * (endPos.getRow() - startPos.getRow())) < 0) {
                throw new IllegalArgumentException("Illegal move");
            } else {
                retList.add(endPos);
            }
        }
        return retList;
    }
}
