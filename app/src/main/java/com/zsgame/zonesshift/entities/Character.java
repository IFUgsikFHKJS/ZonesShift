package com.zsgame.zonesshift.entities;

import android.graphics.PointF;

public abstract class Character extends Entity{

    protected int aniTick, aniIndex;
    protected int faceDir = 1;
    protected final GameCharacters gameCharType;

    public Character(PointF pos, float width, float height, GameCharacters gameCharType) {
        super(pos, width, height);
        this.gameCharType = gameCharType;
    }

    protected void updateAnimation(){
        aniTick++;
        if(aniTick >= 5){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= 2)
                aniIndex = 0;
        }
    }

    public void resetAnimation() {
        aniTick = 0;
        aniIndex = 0;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }

}
