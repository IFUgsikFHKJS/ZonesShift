package com.example.zonesshift.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum ButtonImages implements BitmapMethods {

    LVL_RESTART(R.drawable.button_restart, 112, 112);

    private int width, height;
    private Bitmap normal, pushed;

    ButtonImages(int resID, int width, int height) {
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap buttonAtlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        normal = Bitmap.createBitmap(buttonAtlas, 0, 0, width, height);
        pushed = Bitmap.createBitmap(buttonAtlas, width, 0, width, height);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getBtnImg(boolean isBtnPushed) {
        return isBtnPushed ? pushed : normal;
    }
}
