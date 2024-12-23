package com.example.zonesshift.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum GameCharacters implements BitmapMethods {

    PLAYER(R.drawable.ciz25new);

    private final Bitmap[][] sprites = new Bitmap[7][4];

    GameCharacters(int resId) {
        options.inScaled = false;
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);

        sprites[0][0] = getScaleBitmap(Bitmap.createBitmap(spriteSheet, 0, 0, 64, 64));
    }

    public Bitmap getSprite(int yPos, int xPos){
        return  sprites[yPos][xPos];
    }


}
