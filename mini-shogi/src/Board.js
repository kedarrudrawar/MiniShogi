import React from 'react';
import {Player} from './Player.js';
import './index.css';

function Square(props){
    return (
        <button
            key={[props.i, props.j]}
            className="square"
            // onClick={}
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
                        ['r', 'b', 's', 'g', 'k'],
                    ];
        this.state = {
            board,
            click1:false,
            click2:false,
        }
    }
    renderSquare(rowIdx, colIdx){
        let piece = this.state.board[rowIdx][colIdx];
        return <Square
            onClick={() => {this.handleClick(this.props.i,this.props.j)}}
            i = {rowIdx}
            j = {colIdx}
            piece={piece}/>;
    }

    handleFirstClick(i, j){

    }

    handleSecondClick(i, j){

    }

    handleClick(i, j) {
        let click1 = this.state.click1;

        if (click1) {
            this.setState({
                click1: false,
                click2: true
            });
            this.handleSecondClick(i, j);
        } else {
            this.setState({
                click1: true,
                click2: false,
            });
            this.handleFirstClick(i, j);
        }
    }




    render() {
        return (
            this.state.board.map((row, rowIdx) => {
                return (
                    <div key={rowIdx} className="board-row">
                        {row.map((piece, colIdx) => {
                            return this.renderSquare(piece, rowIdx, colIdx);
                        })}
                    </div>
                )
            })
        );
    }
}
export default Board;