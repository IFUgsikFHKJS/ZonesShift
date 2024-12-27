package com.example.zonesshift.entities;

import com.example.zonesshift.helpers.GameConstants;
import android.graphics.PointF;

public class Player extends Character{
    public Player() {
        super(new PointF((float) GameConstants.GameSize.GAME_WIDTH / 2, (float) GameConstants.GameSize.GAME_HEIGHT / 2 - 150), GameCharacters.PLAYER);
    }

    public void update(double delta, boolean movePlayer){
        if(movePlayer)
            updateAnimation();
    }

}
