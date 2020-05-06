import React from "react";
import Board from './Board.js';

class MyShogi extends React.Component{
    constructor(props){
        super(props);
    }

    render() {
        return (
            <Board />
        );
    }
}

export default MyShogi;