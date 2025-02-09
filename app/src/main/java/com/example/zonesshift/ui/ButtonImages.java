package com.example.zonesshift.ui;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum ButtonImages implements BitmapMethods {

    LVL_RESTART(R.drawable.button_restart, 112, 112,  GAME_WIDTH / 13, GAME_WIDTH / 13),
    MENU_SINGLEPLAYER(R.drawable.button_singleplayer, 128, 32, (int) (GAME_WIDTH / 3.7), (int) (GAME_WIDTH / (3.7 * 128) * 32)),
    LVL1(R.drawable.button_lvl1, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL2(R.drawable.button_lvl2, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL3(R.drawable.button_lvl3, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL4(R.drawable.button_lvl4, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL5(R.drawable.button_lvl5, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL6(R.drawable.button_lvl6, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13),
    LVL7(R.drawable.button_lvl7, 32, 32, GAME_WIDTH / 13, GAME_WIDTH / 13);

    private int width, height;
    private Bitmap normal, pushed;

    ButtonImages(int resID, int width, int height, int dsWidth, int dsHeight) {
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap buttonAtlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
            normal = getScaledBitmapButton(Bitmap.createBitmap(buttonAtlas, 0, 0, width, height), dsWidth, dsHeight);
            pushed = getScaledBitmapButton(Bitmap.createBitmap(buttonAtlas, width, 0, width, height),dsWidth, dsHeight);

        this.width = normal.getWidth();
        this.height = normal.getHeight();

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
