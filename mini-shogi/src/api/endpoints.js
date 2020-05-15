export const base_endpoint = 'http://localhost:8080/';
export const boardState = base_endpoint + 'board';
export const movePiece = base_endpoint + 'move';
export const dropPiece = base_endpoint + 'drop'
export const capturedPieces = base_endpoint + 'getCaptured';

export const resetGame = base_endpoint + 'resetBoard';

export function buildCapturedPiecesLink(player) {
    return capturedPieces + "?player=" +player;
}