import React from 'react';
import {Player} from './Player';
import './index.css';
import * as utils from './utils';
import * as Piece from './Piece'
import * as requests from './api/requests'
import * as urls from './api/endpoints'
import * as deserialize from './api/deserialize'
import {initializeBoard} from "./api/deserialize";
import {BOARDSIZE} from "./config";
import {calibrateLocation} from "./api/serialize";
import {handleRequest} from "./api/requests";
import axios from 'axios';

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
        let board = initializeBoard(BOARDSIZE);

        this.state = {
            board,
            click1:[],
            click2:[],
        }
    }

    componentDidMount() {
        this.refreshBoard();
    }

    refreshBoard(){
        axios.get(urls.boardState)
        .then((boardObj) => {
            console.log(boardObj.data);
            const board = deserialize.board_to_array(boardObj.data);
            this.setState( {
                board
            })
        })
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

    movePiece(src, dest, board){
        src = calibrateLocation(src[0], src[1]);
        dest = calibrateLocation(dest[0], dest[1]);
        
        let body = {
            'src': src,
            'dest': dest,
        };

        axios.post(urls.movePiece, body)
            .then((response) => {
                console.log(response.data);
            })
            .then(() => {
                this.refreshBoard();
            });
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
            if (piece_moved) {
                this.setState({
                    click2: [i, j]
                });
            }

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