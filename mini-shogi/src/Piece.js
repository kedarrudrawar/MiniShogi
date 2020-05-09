import * as utils from './utils';

/**
 * Checks if movement from src --> dest is diagonal
 * @param src
 * @param dest
 * @returns {boolean}
 */
function isDiagonal(src, dest){
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;
    return Math.abs(dest_row - src_row) === Math.abs(dest_col - src_col);
}

/**
 * Checks if movement from src --> dest is vertical
 * @param src
 * @param dest
 * @returns {boolean}
 */
function isVertical(src, dest){
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;
    return src_row === dest_row && src_col !== dest_col;
}

/**
 * Checks if movement from src --> dest is horizontal
 * @param src
 * @param dest
 * @returns {boolean}
 */
function isHorizontal(src, dest){
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;
    return src_row !== dest_row && src_col === dest_col;
}

/**
 * Checks if movement from src --> dest is one unit distance
 * @param src
 * @param dest
 * @returns {boolean}
 */
function isUnitMove(src, dest){
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;
    let rowDiff = Math.abs(dest_row - src_row);
    let colDiff = Math.abs(dest_col - src_col);

    // can only move one space
    return rowDiff <= 1 && colDiff <= 1;
}

export function getPawnPath(src, dest) {
    console.log("pawn paths")
    let paths = [];
    let player = 'upper';
    let adder = player === 'upper' ? 1 : -1;

    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;

    if (src_row + adder === dest_row && src_col === dest_col){
        paths.push(dest);
    }
    else{
        alert("Illegal move.");
    }

    return paths;
}

export function getRookPath(src, dest) {
    let player = 'upper';
    let paths = [];

    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;

    if(isVertical(src, dest)){
        let col_adder = src_col < dest_col ? 1 : -1;
        src_col += col_adder;
        while (src_col !== dest_col){
            paths.push([src_row, src_col]);
            src_col += col_adder;
        }
        paths.push([dest_row, dest_col])
    }
    else if(isHorizontal(src, dest)){
        let row_adder = src_row < dest_row ? 1 : -1;
        src_row += row_adder;
        while (src_row !== dest_row){
            paths.push([src_row, src_col]);
            src_row += row_adder;
        }
        paths.push([dest_row, dest_col])
    }
    else{
        alert("Illegal move.");
    }
    return paths;
}

export function getSilverGeneralPath(src, dest) {
    console.log("sg paths");
    let player = 'upper';
    let adder = player === 'upper' ? 1 : -1;

    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;

    if(! isUnitMove(src, dest))
        return [];

    // can move behind or diagonally (in any direction)
    if ((src_row + adder === dest_row && src_col === dest_col) || isDiagonal(src, dest))
        return [dest];

    return [];
}

export function getGoldGeneralPath(src, dest) {
    console.log("gg paths")
    let player = 'upper';
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;
    return false;
}

export function getKingPath(src, dest) {
    let player = 'upper';
    // let [src_row, src_col] = src;
    // let [dest_row, dest_col] = dest;

    if(isUnitMove(src, dest)){
        return [dest];
    }
    alert("Illegal move.");
    return [];
}

export function getBishopPath(src, dest) {
    let paths = [];
    let player = 'upper';
    let [src_row, src_col] = src;
    let [dest_row, dest_col] = dest;

    if (! isDiagonal(src, dest)){
        alert("Illegal move.");
        return paths;
    }

    let row_adder = src_row < dest_row ? 1 : -1;
    let col_adder = src_col < dest_col ? 1 : -1;

    let curr = src.slice();
    while(! utils.array_equals(curr, dest)){
        paths.push(curr);
        curr[0] += row_adder;
        curr[1] += col_adder;
    }
    paths.push(curr);
    return paths;
}

export function isPawn(piece){
    return piece ==='P' || piece === 'p';
}
export function isBishop(piece){
    return piece === 'B' || piece === 'b';
}
export function isKing(piece){
    return piece === 'K' || piece === 'k';
}
export function isRook(piece){
    return piece === 'R' || piece === 'r';
}
export function isGoldGeneral(piece){
    return piece === 'G' || piece === 'g';
}
export function isSilverGeneral(piece){
    return piece === 'S' || piece === 's';
}
