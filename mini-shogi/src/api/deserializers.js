let getLocation = (piece) => {
    let row = 4 - piece['location']['row'];
    let col = piece['location']['col'];
    return [row, col];
};

export function board_to_array(boardObj){
    let boardToReturn = [];
    for(let i = 0; i < 5; i++){
        boardToReturn.push(['','','','','']);
    }
    let boardArray = boardObj['boardArray'];
    for(let i = 0; i < boardArray.length; i++) {
        for (let j = 0; j < boardArray[i].length; j++) {
            let piece = boardArray[i][j];
            if (piece == null) {
                continue;
            }
            let row, col;
            [row, col] = getLocation(piece);
            boardToReturn[row][col] = piece['name'];
        }
    }
    return boardToReturn;

}