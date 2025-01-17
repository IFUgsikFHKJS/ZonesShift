package com.example.zonesshift.entities;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.GameConstants;
import android.graphics.PointF;

public class Player extends Character{
    static float posX = (float) GAME_WIDTH / 2;
    static float posY = (float) GameConstants.GameSize.GAME_HEIGHT / 2 - 150;

    public Player() {
        super(new PointF(posX, posY), (float) (posX + Playing.getTileSize() * 0.7), (float) (posY + Playing.getTileSize() * 1.6), GameCharacters.PLAYER);

    }

    public void update(double delta, boolean movePlayer){
        if(movePlayer)
            updateAnimation();
    }

}
