package com.example.zonesshift.entities;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT_RES;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH_RES;

import com.example.zonesshift.helpers.GameConstants;
import android.graphics.PointF;

public class Player extends Character{
    static float posX = (float) GAME_WIDTH / 2;
    static float posY = (float) GameConstants.GameSize.GAME_HEIGHT / 2 - 150;

    public Player() {
        super(new PointF(posX, posY), (float) (posX + GAME_WIDTH*0.7 / GAME_WIDTH_RES), (float) (posY + GAME_HEIGHT * 1.6 / GAME_HEIGHT_RES), GameCharacters.PLAYER);

    }

    public void update(double delta, boolean movePlayer){
        if(movePlayer)
            updateAnimation();
    }

}
