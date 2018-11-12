/**
 * Class Location
 * This class is used to create an object storing the specific position of a Piece on the board at all times
 * Contains Row and Col values between 0 - 4
 */

public class Location {
    private int row;
    private int col;

    /**
     * Constructor - initializes row and column in the board given integer inputs
     * Col and Row: between 0 - 4 [inclusive]
     * @param col   Column in the board (indexed by 0)
     * @param row   Row in the board (indexed by 0)
     */
    public Location(int col, int row){
        if(col >= 0 && col <= 4)
            this.col = col;
        if(row >= 0 && row <= 4)
            this.row = row;
    }

    /**
     * Constructor - initializes row and column in the board given an input String
     * Col: a - e [inclusive]
     * Row: 0 - 4 [inclusive]
     * @param pos   String representation of the position, i.e. "e3".
     */
    public Location(String pos){
        int col = pos.charAt(0) - 97;
        if(col >= 0 && col <= 4)
            this.col = col;
        int row = Character.getNumericValue(pos.charAt(1)) - 1;
        if(row >= 0 && row <= 4)
            this.row = row;
    }

    /**
     * Return column of a Location object
     * @return an int - column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Return row of a Location object
     * @return an int - row
     */
    public int getRow(){
        return this.row;
    }

    /**
     * This method returns a Location object as a String. It outputs as the conventional string representation of
     * Location objects (i.e. "a2").
     * @return a String
     */
    @Override
    public String toString(){
        String loc = "";
        loc += (char)(this.getCol() + 97);
        loc += Integer.toString(this.getRow() + 1);

        return loc;
    }

    /**
     * This method checks equality of two Location objects (whether the row and the column are the same)
     * @param loc   Location object to compare the calling object to
     * @return  a boolean - true if equal, false otherwise
     */
    @Override
    public boolean equals(Object loc){
        Location l = (Location) loc;
        if(this.getRow() == l.getRow()){
            if(this.getCol() == l.getCol())
                return true;
        }
        return false;
    }

}

