import React from 'react';
import {Player} from './Player';
import './index.css';
import * as utils from './utils';
import * as requests from './api/requests'
import * as urls from './api/endpoints'
import {buildCapturedPiecesLink} from './api/endpoints';

import * as deserialize from './api/deserialize'
import {initializeBoard} from "./api/deserialize";
import {BOARDSIZE} from "./config";
import {calibrateLocation} from "./api/serialize";
import {handleRequest} from "./api/requests";
import axios from 'axios';


const LOWER_COLUMN_IDX = BOARDSIZE;
const UPPER_COLUMN_IDX = BOARDSIZE + 1;


function Piece(props){
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
            move : {
                click1: [],
                click2: [],
            },
            drop : {
                piece: [],
                destination: []
            },
            captured_lower: [],
            captured_upper: [],
        }
    }

    isMove(){
        return this.state.move.click1.length !== 0;
    }

    isDrop(){
        return this.state.drop.piece !== '';
    }

    componentDidMount() {
        this.refresh();
    }

    refresh(){
        this.refreshBoard();
        this.refreshGameInfo();
    }

    refreshBoard(){
        axios.get(urls.boardState)
        .then((boardObj) => {
            const board = deserialize.board_to_array(boardObj.data);
            this.setState( {
                board
            })
        })
    }

    refreshGameInfo(){
        axios.get(urls.buildCapturedPiecesLink('lower'))
        .then(response => {
            let data = response.data;
            this.setState({
                captured_lower: deserialize.capturedToPieces(data),
            })
        });

        axios.get(urls.buildCapturedPiecesLink('upper'))
        .then(response => {
            let data = response.data;
            this.setState({
                captured_upper: deserialize.capturedToPieces(data),
            })
        });
    }

    dropPiece(src, dest){
        dest = calibrateLocation(dest[0], dest[1]);
        let capturedList = src[0] === LOWER_COLUMN_IDX ? this.state.captured_lower : UPPER_COLUMN_IDX;
        let body = {
            'piece': capturedList[src[1]],
            'dest': dest,
        }; 
        let success;
        axios.post(urls.dropPiece, body)
            .then((response) => {
                success = response.data;
            })
            .then(() => {
                this.refresh();
            });
        return success;
    }



    movePiece(src, dest){
        src = calibrateLocation(src[0], src[1]);
        dest = calibrateLocation(dest[0], dest[1]);
        
        let body = {
            'src': src,
            'dest': dest,
        };
        
        let success;
        axios.post(urls.movePiece, body)
            .then((response) => {
                success = response.data;
            })
            .then(() => {
                this.refresh();
            });
        return success;
    }

    handleMove(i, j){
        return this.movePiece(this.state.move.click1, [i,j]);
    }

    handleDrop(i, j){
        return this.dropPiece(this.state.drop.piece, [i,j]);
    }


    handleClick(i, j) {
        // handle first click for dropping a piece
        let original_state;
        if (i >= BOARDSIZE){ // checks if click was on captured piece, or board piece
            original_state = JSON.parse(JSON.stringify(this.state.drop));
            original_state.piece = [i,j]
            this.setState({
                drop: original_state 
            });
        }





        // check if click is for dropping a piece or initializing a move of a piece
        else{
            if(this.isDrop()){
                original_state = JSON.parse(JSON.stringify(this.state.drop));
                original_state.destination = [i,j]
                let piece_dropped = this.handleDrop(i, j);
                this.setState({
                    drop: original_state
                });
            }




            original_state = JSON.parse(JSON.stringify(this.state.move));
            // initial click
            if(this.state.move.click1.length === 0 && this.state.move.click2.length === 0) {
                if (this.state.board[i][j] !== ''){  // check if square is empty
                    original_state.click1 = [i,j];
                }
            }

            //second click
            else{
                let piece_moved = this.handleMove(i, j);

                if (piece_moved) {
                    original_state.click2 = [i,j];
                }
                else{
                    original_state.click1 = [];
                    original_state.click2 = [];
                }

            }
            this.setState({
                move: original_state
            });
        }
    }



    render() {
        return (
            <div className="wrapper">
                <div className="board">{this.renderBoard()} </div>
                <div className="game-info">
                    <div className='captured-column'>
                        <div className="column-title">Upper Captures</div>
                        {this.renderCapturedColumn('upper')}
                    </div>
                    <div className='captured-column'>
                        <div className="column-title">Lower Captures</div>
                        {this.renderCapturedColumn('lower')}
                    </div>
                </div>
            </div>
        );
    }



// RENDER FUNCTIONS


renderCapturedColumn(player) {
    let captured = player === 'lower' ? this.state.captured_lower : this.state.captured_upper;
    let column_idx = player === 'lower' ? LOWER_COLUMN_IDX : UPPER_COLUMN_IDX;
    
    captured.map((piece, idx) => {
        return (
        <Piece
            i = {column_idx}
            j = {idx}
            piece={piece}
            selected={utils.array_equals([column_idx, idx], this.state.drop.piece)}
            onClick={() => {this.handleClick(LOWER_COLUMN_IDX, idx)}}
        />);
    })
   
}



renderSquare(rowIdx, colIdx){
    let click1 = this.state.move.click1;
    let click2 = this.state.move.click2;
    let piece = this.state.board[rowIdx][colIdx];
    let selected = false;
    if (utils.array_equals([rowIdx, colIdx], click1) || utils.array_equals([rowIdx, colIdx], click2))
        selected = true;

    return <Piece
        onClick={() => {this.handleClick(rowIdx, colIdx)}}
        i = {rowIdx}
        j = {colIdx}
        selected={selected}
        piece={piece}/>;
}

renderBoard(){
    return ( 
        <div className="board">
            {this.state.board.map((row, rowIdx) => {
                return (
                    <div key={rowIdx} className="board-row">
                            {row.map((_piece, colIdx) => {
                                return this.renderSquare(rowIdx, colIdx);
                            })}
                    </div>
                );
            })}
         </div>
    );
}




}
export default Board;