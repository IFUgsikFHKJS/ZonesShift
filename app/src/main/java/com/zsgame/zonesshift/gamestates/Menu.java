package com.zsgame.zonesshift.gamestates;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.gamestates.createlvl.CreatedLvlsList;
import com.zsgame.zonesshift.gamestates.menustates.GameSettings;
import com.zsgame.zonesshift.gamestates.menustates.SinglePlayerLevels;
import com.zsgame.zonesshift.gamestates.menustates.StartMenu;
import com.zsgame.zonesshift.gamestates.searchlvl.SearchMainScreen;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.main.MainActivity;

public class Menu extends BaseState implements GameStateInterface, BitmapMethods {

    private Paint paint;
    private StartMenu startMenu;
    private SinglePlayerLevels singlePlayerLevels;
    private GameSettings gameSettings;
    private CreatedLvlsList createdLvlsList;
    private SearchMainScreen searchMainScreen;

    private MenuState currentMenuState = MenuState.START_MENU;

    public Menu(Game game) {
        super(game);
        startMenu = new StartMenu(game);
        singlePlayerLevels = new SinglePlayerLevels(game);
        gameSettings = new GameSettings(game);
        createdLvlsList = new CreatedLvlsList(game);
        searchMainScreen = new SearchMainScreen(game);
    }

    @Override
    public void update(double delta) {
        switch (currentMenuState){
            case START_MENU -> startMenu.update(delta);
            case SINGLEPLAYER_LVL -> singlePlayerLevels.update(delta);
            case GAME_SETTINGS -> gameSettings.update(delta);
            case CREATEDLVLSLIST -> createdLvlsList.update(delta);
            case SEARCHLVL -> searchMainScreen.update(delta);
        }
    }

    @Override
    public void render(Canvas c) {
        drawMenu(c);
        switch (currentMenuState){
            case START_MENU -> startMenu.render(c);
            case SINGLEPLAYER_LVL -> singlePlayerLevels.render(c);
            case GAME_SETTINGS -> gameSettings.render(c);
            case CREATEDLVLSLIST -> createdLvlsList.render(c);
            case SEARCHLVL -> searchMainScreen.render(c);
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
            case SINGLEPLAYER_LVL -> singlePlayerLevels.touchEvents(event);
            case GAME_SETTINGS -> gameSettings.touchEvents(event);
            case CREATEDLVLSLIST -> createdLvlsList.touchEvents(event);
            case SEARCHLVL -> searchMainScreen.touchEvents(event);
        }
    }

    public enum MenuState{
        START_MENU, SINGLEPLAYER_LVL, GAME_SETTINGS, CREATEDLVLSLIST, SEARCHLVL;
    }

    public void setSearchMainScreen(SearchMainScreen searchMainScreen) {
        this.searchMainScreen = searchMainScreen;
    }
}
