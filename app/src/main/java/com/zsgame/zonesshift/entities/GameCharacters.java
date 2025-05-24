package com.zsgame.zonesshift.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.gamestates.Playing;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.main.MainActivity;

public enum GameCharacters implements BitmapMethods {

    PLAYER(R.drawable.ciz25_sheet);

    private Bitmap spriteSheet;
    private Bitmap[][] sprites = new Bitmap[5][2];

//    GameCharacters(int resId) {
//        options.inScaled = false;
//        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
//
//        sprites[0][0] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, 0, 0, 64, 64));
//    }

    GameCharacters(int resID) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for(int j = 0; j < sprites.length; j++){
            for(int i = 0; i < sprites[j].length; i++){
                sprites[j][i] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, 64 * i ,64 * j, 64, 64), Playing.getTileSize());
            }
        }
    }

    public void setPlayerBitmap(int tileSize){
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.ciz25_sheet, options);
        for(int j = 0; j < sprites.length; j++){
            for(int i = 0; i < sprites[j].length; i++){
                sprites[j][i] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, 64 * i ,64 * j, 64, 64), tileSize);
            }
        }
    }

    public Bitmap getSprite(int yPos, int xPos){
        return  sprites[yPos][xPos];
    }


}
