package com.example.zonesshift.helpers.interfaces;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.GameConstants;


public interface BitmapMethods {



    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaleBitmap(Bitmap bitmap, int tileSize){
        return Bitmap.createScaledBitmap(bitmap, tileSize * 2, tileSize * 2, false);
    }

    default Bitmap getScaleBitmapBlock(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, Playing.getTileSize(), Playing.getTileSize(), false);
    }
}
