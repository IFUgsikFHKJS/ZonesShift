package com.zsgame.zonesshift.gamestates;


import com.zsgame.zonesshift.Game;

public abstract class BaseState {

    protected Game game;

    public BaseState(Game game){
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
