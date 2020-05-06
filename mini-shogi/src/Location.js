/**
 * Class Location
 * This class is used to create an object storing the specific position of a Piece on the board at all times
 * Contains Row and Col values between 0 - 4
 */

    // /**
    //  * Constructor - initializes row and column in the board given integer inputs
    //  * Col and Row: between 0 - 4 [inclusive]
    //  * @param col   Column in the board (indexed by 0)
    //  * @param row   Row in the board (indexed by 0)
    //  */
    // public Location(int col, int row){
    // if(col >= 0 && col <= 4)
    // this.col = col;
    // if(row >= 0 && row <= 4)
    // this.row = row;

const Location = (row, col) => {
        return ({
            col: 0,
            row: 0,
        });
    };
export default Location;

/**
 * Constructor - initializes row and column in the board given an input String
 * Col: a - e [inclusive]
 * Row: 0 - 4 [inclusive]
 * @param pos   String representation of the position, i.e. "e3".
 */

function posToLoc(pos) {
    let location = {
        col: 0,
        row: 0,
    };

    let col = pos.charAt(0) - 97;
    if (col >= 0 && col <= 4)
        location.col = col;

    let row = parseInt(pos.charAt(1)) - 1;
    if(row >= 0 && row <= 4)
        location.row = row;
    return location;
}
// public Location(String pos){
//     int col = pos.charAt(0) - 97;
//     if(col >= 0 && col <= 4)
//         this.col = col;
//     int row = Character.getNumericValue(pos.charAt(1)) - 1;
//     if(row >= 0 && row <= 4)
//         this.row = row;
// }

/**
 * Return column of a Location object
 * @return an int - column
 */
function getCol(loc) {
    return loc.col;
}

/**
 * Return row of a Location object
 * @return an int - row
 */
function getRow(loc) {
    return loc.row;
}

/**
 * This method returns a Location object as a String. It outputs as the conventional string representation of
 * Location objects (i.e. "a2").
 * @return string String
 */
function toString(loc){
    let locStr = "";
    locStr += String(getCol(loc) + 97);
    locStr += String(getRow(loc) + 1);

    return locStr;
}

/**
 * This method checks equality of two Location objects (whether the row and the column are the same)
 * @param loc1 - Location object to compare
 * @param loc2 - Location object to compare
 * @return  a boolean - true if equal, false otherwise
 */

function equals(loc1, loc2){
    if (getRow(loc1) === getRow(loc2)) {
        if (getCol(loc1) === getCol(loc2))
            return true;
    }
    return false;
}


