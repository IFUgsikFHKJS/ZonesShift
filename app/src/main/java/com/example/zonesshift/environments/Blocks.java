package com.example.zonesshift.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum Blocks implements BitmapMethods {

    SOLID(R.drawable.block_solid, 4, 4),
    REDZONE(R.drawable.block_redzone, 4, 4),
    GRAVIZONE(R.drawable.block_gravizone, 3, 1),
    WIN(R.drawable.block_win,1,1),
    BACKGROUND(R.drawable.background, 1, 1);


    private Bitmap[] sprites;

    Blocks(int resID, int tilesInWidth, int tilesInHeight){
        options.inScaled = false;
        sprites = new Bitmap[tilesInHeight * tilesInWidth];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for(int j = 0; j < tilesInHeight; j++){
            for(int i = 0; i < tilesInWidth; i++){
                int index = j * tilesInWidth + i;
                int tileSize = resID == R.drawable.block_win ? 16 : 32;
                sprites[index] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, tileSize * i, tileSize * j, tileSize, tileSize));
            }

        }
    }

    public Bitmap getSprite(int id){
        return sprites[id];
    }
}