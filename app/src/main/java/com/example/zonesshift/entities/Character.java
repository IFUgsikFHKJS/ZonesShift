package com.example.zonesshift.entities;

import android.graphics.PointF;

public abstract class Character extends Entity{

    protected int aniTick, aniIndex;
    protected final GameCharacters gameCharType;

    public Character(PointF pos, GameCharacters gameCharType) {
        super(pos, 1, 1);
        this.gameCharType = gameCharType;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }

}
