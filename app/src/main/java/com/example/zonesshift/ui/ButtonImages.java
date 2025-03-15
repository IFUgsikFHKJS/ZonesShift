package com.example.zonesshift.ui;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zonesshift.R;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.main.MainActivity;

public enum ButtonImages implements BitmapMethods {

    LVL_RESTART(R.drawable.button_restart, 16, 16,  GAME_WIDTH / 13, GAME_WIDTH / 13),
    LEADERBOARD(R.drawable.button_leaderboard, 16, 16,  GAME_WIDTH / 13, GAME_WIDTH / 13),
    PLAY(R.drawable.button_play, 16, 16,  GAME_WIDTH / 10, GAME_WIDTH / 10),
    MENU_SINGLEPLAYER(R.drawable.button_singleplayer, 128, 32, (int) (GAME_WIDTH / 3.7), (int) (GAME_WIDTH / (3.7 * 128) * 32)),
    MENU_SETTINGS(R.drawable.button_settings, 16, 16, GAME_WIDTH / 13, GAME_WIDTH / 13),
    PLAYING_TO_LVL(R.drawable.button_lvls, 16, 16, GAME_WIDTH / 13, GAME_WIDTH / 13),
    SETTINGS_SIGNOUT(R.drawable.button_signout, 80, 32, GAME_WIDTH / 6,  (GAME_WIDTH / (6 * 80) * 32)),
    HOME(R.drawable.button_home, 16, 16, GAME_WIDTH / 13, GAME_WIDTH / 13),

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
