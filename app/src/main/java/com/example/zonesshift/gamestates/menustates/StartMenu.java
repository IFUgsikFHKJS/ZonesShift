package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zonesshift.Game;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.environments.mapmanagment.Map;
import com.example.zonesshift.environments.mapmanagment.MapLoader;
import com.example.zonesshift.environments.mapmanagment.mapcreating.AddMap;
import com.example.zonesshift.environments.mapmanagment.mapcreating.LoadMap;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class StartMenu extends BaseState implements GameStateInterface {

    private CustomButton btnSinglePlayer;
    private CustomButton btnSettings;
    private CustomButton btnTestLevel;

    public StartMenu(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons(){
        btnSinglePlayer = new CustomButton((float) GAME_WIDTH / 2 - (float) ButtonImages.MENU_SINGLEPLAYER.getWidth() / 2,
                (float) GAME_HEIGHT / 2 - (float) ButtonImages.MENU_SINGLEPLAYER.getHeight() / 2,
                ButtonImages.MENU_SINGLEPLAYER.getWidth(), ButtonImages.MENU_SINGLEPLAYER.getHeight());
        btnSettings = new CustomButton((float) (GAME_WIDTH - ButtonImages.MENU_SETTINGS.getWidth() * 1.5),
                (float) (ButtonImages.MENU_SETTINGS.getWidth() * .5),
                ButtonImages.MENU_SETTINGS.getWidth(), ButtonImages.MENU_SETTINGS.getHeight());


        btnTestLevel = new CustomButton((float) (GAME_WIDTH - ButtonImages.MENU_SETTINGS.getWidth() * 1.5),
                (float) (GAME_HEIGHT - ButtonImages.MENU_SETTINGS.getHeight() - 40),
                ButtonImages.MENU_SETTINGS.getWidth(), ButtonImages.MENU_SETTINGS.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        drawButtons(c);
    }

    private void drawButtons(Canvas c){
        c.drawBitmap(
                ButtonImages.MENU_SINGLEPLAYER.getBtnImg(btnSinglePlayer.isPushed()),
                btnSinglePlayer.getHitbox().left,
                btnSinglePlayer.getHitbox().top, null);

        c.drawBitmap(
                ButtonImages.MENU_SETTINGS.getBtnImg(btnSettings.isPushed()),
                btnSettings.getHitbox().left,
                btnSettings.getHitbox().top, null);

        c.drawBitmap(
                ButtonImages.MENU_SETTINGS.getBtnImg(btnTestLevel.isPushed()),
                btnTestLevel.getHitbox().left,
                btnTestLevel.getHitbox().top, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (btnSinglePlayer.isIn(event))
                btnSinglePlayer.setPushed(true);

            else if(btnSettings.isIn(event))
                btnSettings.setPushed(true);

            else if(btnTestLevel.isIn(event))
                btnTestLevel.setPushed(true);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (btnSinglePlayer.isIn(event)) {
                if (btnSinglePlayer.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.SINGLEPLAYER_LVL);
            } else if (btnSettings.isIn(event)){
                if (btnSettings.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.GAME_SETTINGS);}
            else if (btnTestLevel.isIn(event)){
                if (btnTestLevel.isPushed())
                    loadMapFromDB();}

            btnSinglePlayer.setPushed(false);
            btnSettings.setPushed(false);
            btnTestLevel.setPushed(false);
        }
    }


    private void loadMapFromDB(){
        LoadMap.getMapById(8,
                mapData -> {
                    String mapString = (String) mapData.get("map_data");
                    System.out.println("Загружена карта: " + mapString);
                    Tile[][] tiles = MapLoader.loadMapFromString(mapString);


                    game.getPlaying().setCurrentOnlineMap(new Map(tiles), 8);
                    game.setCurrentGameStateLvlScreen(new LevelScreen(game, 8));
                },
                e -> System.out.println("Ошибка загрузки карты: " + e.getMessage()));
    }
}
