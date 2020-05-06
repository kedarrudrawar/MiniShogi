import React from 'react';
import {Player} from './Player.js';
import './index.css';

class Square extends React.Component{
    constructor(props){
        super(props);
    }

    render(){
        return <button className="square">K</button>;
    }
}

class Board extends React.Component{
    constructor(props){
        super(props);
        let board = [
                        ['R', 'B', 'S', 'G', 'K'],
                        ['P','','','',''],
                        ['','','','',''],
                        ['','','','',''],
                        ['p','','','',''],
                        ['r', 'b', 's', 'g', 'k'],
                    ];
        this.state = {
            board: board,

        }
    }


    render() {
        return (
            <div>
                <Square></Square>
            </div>
        );
    }
}
export default Board;