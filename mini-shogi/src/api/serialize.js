export let calibrateLocation = (row, col) => {
    row = 4 - row;
    return row.toString() + ',' + col.toString();
};