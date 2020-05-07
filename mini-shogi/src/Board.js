import React from 'react';
import {Player} from './Player';
import './index.css';
import {array_equals} from './utils';


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
        if (array_equals([rowIdx, colIdx], click1) || array_equals([rowIdx, colIdx], click2))
            selected = true;

        return <Square
            onClick={() => {this.handleClick(rowIdx, colIdx)}}
            i = {rowIdx}
            j = {colIdx}
            selected={selected}
            piece={piece}/>;
    }

    handleFirstClick(i, j){

    }

    handleSecondClick(i, j){
        let click1 = this.state.click1;
        let pieceToMove = this.state.board[click1[0]][click1[1]];
        var newBoard = this.state.board.map(function(arr) {
            return arr.slice();
        });

        newBoard[i][j] = pieceToMove;
        newBoard[click1[0]][click1[1]] = '';
        this.setState({
            board: newBoard,
        })
    }

    handleClick(i, j) {
        // initial click
        if(this.state.click1.length === 0 && this.state.click2.length === 0){
            this.setState({
                click1: [i, j]
            })
        }

        //first click:
        else if (this.state.click2.length !== 0) {
            this.setState({
                click1: [i, j],
                click2: [],
            });
            this.handleFirstClick(i, j);
        }

        //second click
        else{
            console.log('HERE');
            this.setState({
                click2: [i, j]
            });
            this.handleSecondClick(i, j);
        }
    }




    render() {
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