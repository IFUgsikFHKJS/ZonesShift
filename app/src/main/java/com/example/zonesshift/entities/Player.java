package com.example.zonesshift.entities;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.GameConstants;
import android.graphics.PointF;

public class Player extends Character{


    public Player(PointF p) {
        super(new PointF(p.x, p.y), (float) (p.x + Playing.getTileSize() * 0.7), (float) (p.y + Playing.getTileSize() * 1.6), GameCharacters.PLAYER);

    }

    public void update(double delta, boolean movePlayer){
        if(movePlayer)
            updateAnimation();
    }

}
