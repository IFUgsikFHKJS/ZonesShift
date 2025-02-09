package com.example.zonesshift.gamestates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.zonesshift.Game;
import com.example.zonesshift.R;
import com.example.zonesshift.gamestates.menustates.SinglePlayerLevels;
import com.example.zonesshift.gamestates.menustates.StartMenu;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.MainActivity;

public class Menu extends BaseState implements GameStateInterface, BitmapMethods {

    private Paint paint;
    private StartMenu startMenu;
    private SinglePlayerLevels singlePlayerLevels;
    private MenuState currentMenuState = MenuState.START_MENU;

    public Menu(Game game) {
        super(game);
        startMenu = new StartMenu(game);
        singlePlayerLevels = new SinglePlayerLevels(game);
    }

    @Override
    public void update(double delta) {
        switch (currentMenuState){
            case START_MENU -> startMenu.update(delta);
            case SINGLEPLAYERLVL -> singlePlayerLevels.update(delta);
        }
    }

    @Override
    public void render(Canvas c) {
        drawMenu(c);
        switch (currentMenuState){
            case START_MENU -> startMenu.render(c);
            case SINGLEPLAYERLVL -> singlePlayerLevels.render(c);
        }
    }

    private void drawMenu(Canvas c) {
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.menu_background, options);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 144, 81);
        bitmap = getScaledBitmapButton(bitmap, GAME_WIDTH, GAME_HEIGHT);
        c.drawBitmap(bitmap, 0, 0, null);
    }

    public void setCurrentMenuState(MenuState state){
        currentMenuState = state;
    }

    @Override
    public void touchEvents(MotionEvent event) {
        switch (currentMenuState){
            case START_MENU -> startMenu.touchEvents(event);
            case SINGLEPLAYERLVL -> singlePlayerLevels.touchEvents(event);
        }
    }

    public enum MenuState{
        START_MENU, SINGLEPLAYERLVL;
    }

}
