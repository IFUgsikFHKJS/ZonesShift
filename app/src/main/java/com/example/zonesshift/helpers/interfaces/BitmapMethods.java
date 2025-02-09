package com.example.zonesshift.helpers.interfaces;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.gamestates.Playing;


public interface BitmapMethods {



    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaleBitmap(Bitmap bitmap, int tileSize){
        return Bitmap.createScaledBitmap(bitmap, tileSize * 2, tileSize * 2, false);
    }

    default Bitmap getScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, (int) (Playing.getTileSize()), (int) (Playing.getTileSize()), false);
    }

    default Bitmap getScaledBitmapButton(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}