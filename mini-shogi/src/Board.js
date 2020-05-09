import React from 'react';
import {Player} from './Player';
import './index.css';
import * as utils from './utils';
import * as Piece from './Piece'


function Square(props){
    return (
        <button
            key={[props.i, props.j]}
            className={`square ${props.selected ? "square_selected" : ""}`}
            onClick={props.onClick}
        >{props.piece}
        </button>
    );
}

class Board extends React.Component{
    constructor(props){
        super(props);
        let board = [
                        ['R', 'B', 'S', 'G', 'K'],
                        ['','','','','P'],
                        ['','','','',''],
                        ['p','','','',''],
                        ['k' ,'g' ,'s' ,'b' ,'r'],
                    ];
        this.state = {
            board,
            click1:[],
            click2:[],
        }
    }
    renderSquare(rowIdx, colIdx){
        let click1 = this.state.click1;
        let click2 = this.state.click2;
        let piece = this.state.board[rowIdx][colIdx];
        let selected = false;
        if (utils.array_equals([rowIdx, colIdx], click1) || utils.array_equals([rowIdx, colIdx], click2))
            selected = true;

        return <Square
            onClick={() => {this.handleClick(rowIdx, colIdx)}}
            i = {rowIdx}
            j = {colIdx}
            selected={selected}
            piece={piece}/>;
    }

    validateMovement(piece, src, dest) {
        if(utils.array_equals(src, dest))
            return false;


        return ((Piece.isBishop(piece) && Piece.getBishopPath(src, dest).length !== 0) ||
            (Piece.isKing(piece) && Piece.getKingPath(src, dest).length !== 0) ||
            (Piece.isGoldGeneral(piece) && Piece.getGoldGeneralPath(src, dest).length !== 0) ||
            (Piece.isSilverGeneral(piece) && Piece.getSilverGeneralPath(src, dest).length !== 0) ||
            (Piece.isRook(piece) && Piece.getRookPath(src, dest).length !== 0) ||
            (Piece.isPawn(piece) && Piece.getPawnPath(src, dest).length !== 0)
        );
    }

    movePiece(src, dest, board){
        console.log('trying to move from: ' + src + ' to ' + dest);
        let piece = board[src[0]][src[1]];
        if(this.validateMovement(piece, src, dest)) {
            console.log('valid');
            board[dest[0]][dest[1]] = board[src[0]][src[1]];
            board[src[0]][src[1]] = '';
            this.setState({
                board: board,
            });
        }
    }

    handleSecondClick(i, j){
        // TODO: Fix bug where I can't move after hitting another piece
        if(this.state.board[i][j] !== ''){
            alert('PIECE ALREADY THERE >:(');
            return false;
        }
        let newBoard = this.state.board.map(function(arr) {
            return arr.slice();
        });
        this.movePiece(this.state.click1, [i,j], newBoard);

        return true;
    }

    handleClick(i, j) {
        // initial click
        if(this.state.click1.length === 0 && this.state.click2.length === 0) {
            if (this.state.board[i][j] !== ''){
                this.setState({click1: [i, j]});
            }
        }

        //first click:
        else if (this.state.click2.length !== 0) {
            if (this.state.board[i][j] === '')
                return;
            this.setState({
                click1: [i, j],
                click2: [],
            });
        }

        //second click
        else{
            let piece_moved = this.handleSecondClick(i, j);
            console.log('HERE');
            if (piece_moved) {
                this.setState({
                    click2: [i, j]
                });
            }

        }
    }




    render() {
        fetch('http://localhost:8080/currentPlayer')
            .then((response) => response.text())
            .then(text => {
                try {
                    console.log(text);
                    const data = JSON.parse(text);
                    console.log(data);
                } catch (err){
                    console.log("its actually text");
                }
            })
            .catch(() => {
                console.log("Caught exception.");
            });

        return (
            this.state.board.map((row, rowIdx) => {
                return (
                    <div key={rowIdx} className="board-row">
                        {row.map((piece, colIdx) => {
                            return this.renderSquare(rowIdx, colIdx);
                        })}
                    </div>
                )
            })
        );
    }
}
export default Board;