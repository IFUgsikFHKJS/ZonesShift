package com.example.zonesshift.gamestates;


import com.example.zonesshift.Game;

public abstract class BaseState {

    protected Game game;

    public BaseState(Game game){
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
