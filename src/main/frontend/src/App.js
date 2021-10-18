import logo from './logo.svg';
import './App.css';
import React, {useState, useEffect} from "react";
import styled from "styled-components";
import axios from "axios";
import {render} from "@testing-library/react";

const theme = {
    blue: {
        default: "#3f51b5",
        hover: "#283593"
    },
    pink: {
        default: "#e91e63",
        hover: "#ad1457"
    }
};

const Button = styled.button`
  background-color: ${(props) => theme[props.theme].default};
  color: white;
  padding: 5px 15px;
  border-radius: 5px;
  outline: 0;
  text-transform: uppercase;
  margin: 10px 0px;
  cursor: pointer;
  box-shadow: 0px 2px 2px lightgray;
  transition: ease background-color 250ms;
  &:hover {
    background-color: ${(props) => theme[props.theme].hover};
  }
  &:disabled {
    cursor: default;
    opacity: 0.7;
  }
`;

Button.defaultProps = {
    theme: "blue"
};

const ButtonToggle = styled(Button)`
  opacity: 0.7;
  ${({ active }) =>
    active &&
    `
    opacity: 1; 
  `}
`;

class UserInterface extends React.Component{
    constructor(props) {
        super(props);
        this.state = {songs: [], access_token: localStorage.getItem('access_token'), username: null, password: null};
        this.setState({access_token: localStorage.getItem('access_token')});
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleUsernameSubmit = this.handleUsernameSubmit.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handlePasswordSubmit = this.handlePasswordSubmit.bind(this);
    }

    handleUsernameChange(event){
        this.setState({username: event.target.value});
    }

    handleUsernameSubmit(event) {
        event.preventDefault();
    }

    handlePasswordChange(event){
        this.setState({password: event.target.value});
    }

    handlePasswordSubmit(event) {
        event.preventDefault();
    }

    getSongsList = () => {
        axios.get("http://localhost:8080/showAllSongs", {headers: {"Authorization" : this.state.access_token}}).then(res => {
            console.log(this.state.access_token);
            console .log(res.data);
            console.log(this.state.username);
            console.log(this.state.password);
            this.setState({songs: res.data});
        });
    }

    login = () => {
        axios.get("http://localhost:8080/api/myLogin?username=Dude&password=1111").then(res => {
            console .log(res.data.access_token);
            this.setState({access_token: "Bearer " + res.data.access_token})
            localStorage.setItem('access_token', this.state.access_token);
        });
    }
    render() {
        return (
            <div>
                <form onSubmit={this.handleUsernameSubmit}>
                    <label>
                        Username:
                            <input type="text" value={this.state.value} onChange={this.handleUsernameChange} />
                        Password:
                        <input type="text" value={this.state.value} onChange={this.handlePasswordChange} />
                    </label>
                </form>

                <Button type = "button" onClick={() => {this.getSongsList()}}>GetSongsList</Button>
                <Button type = "button" onClick={() => {this.login()}}>Login</Button>
                <p>{this.state.songs.map((song, index) =>
                        <>
                            <div key = {index}>
                                <h1>{song.song}</h1>
                                <p>{song.artist}</p>
                                <p>{song.genre}</p>
                            </div>
                        </>
                )}</p>
                <p>{this.state.access_token}</p>
            </div>
        );
    }
}


function App() {
  return (
    <div className="App">
    <UserInterface/>
    </div>
  );
}

export default App;
