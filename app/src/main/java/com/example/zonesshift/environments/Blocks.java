package com.example.zonesshift.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum Blocks implements BitmapMethods {

    SOLID(R.drawable.block_solid, 4, 4),
    REDZONE(R.drawable.block_redzone, 4, 4),
    WIN(R.drawable.win,1,1);

    private Bitmap[] sprites;
    private int resID, tilesInWidth, tilesInHeight;

    Blocks(int resID, int tilesInWidth, int tilesInHeight){
        this.resID = resID;
        this.tilesInHeight = tilesInHeight;
        this.tilesInWidth = tilesInWidth;
        options.inScaled = false;
        sprites = new Bitmap[tilesInHeight * tilesInWidth];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for(int j = 0; j < tilesInHeight; j++){
            for(int i = 0; i < tilesInWidth; i++){
                int index = j * tilesInWidth + i;
                int tileSize = resID == R.drawable.win ? 16 : 32;
                sprites[index] = getScaleBitmapBlock(Bitmap.createBitmap(spriteSheet, tileSize * i, tileSize * j, tileSize, tileSize));
            }

        }
    }

    public void updateBitmap(){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        options.inScaled = false;
        sprites = new Bitmap[tilesInHeight * tilesInWidth];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for(int j = 0; j < tilesInHeight; j++){
            for(int i = 0; i < tilesInWidth; i++){
                int index = j * tilesInWidth + i;
                int tileSize = resID == R.drawable.win ? 16 : 32;
                sprites[index] = Bitmap.createScaledBitmap(Bitmap.createBitmap(spriteSheet, tileSize * i, tileSize * j, tileSize, tileSize), Playing.getTileSize(), Playing.getTileSize(), false);
            }

        }
    }

    public Bitmap getSprite(int id){
        return sprites[id];
    }
}
