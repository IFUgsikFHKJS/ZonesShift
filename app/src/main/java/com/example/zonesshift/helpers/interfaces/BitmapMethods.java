package com.example.zonesshift.helpers.interfaces;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.helpers.GameConstants;


public interface BitmapMethods {
    int GAME_WIDTH = GameConstants.GameSize.GAME_WIDTH, GAME_HEIGHT= GameConstants.GameSize.GAME_HEIGHT, GAME_WIDTH_RES = GameConstants.GameSize.GAME_WIDTH_RES, GAME_HEIGHT_RES = GameConstants.GameSize.GAME_HEIGHT_RES;



    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaleBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, GAME_WIDTH / GAME_WIDTH_RES * 2, GAME_HEIGHT/ GAME_HEIGHT_RES * 2, false);
    }
}
