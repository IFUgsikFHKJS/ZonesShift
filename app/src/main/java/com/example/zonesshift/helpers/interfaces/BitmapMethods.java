package com.example.zonesshift.helpers.interfaces;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.GameConstants;


public interface BitmapMethods {



    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaleBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, Playing.getTileSize() * 2, Playing.getTileSize() * 2, false);
    }
}
