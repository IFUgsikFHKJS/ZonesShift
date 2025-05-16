package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zonesshift.Game;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.environments.mapmanagment.Map;
import com.example.zonesshift.environments.mapmanagment.MapLoader;
import com.example.zonesshift.environments.mapmanagment.mapcreating.LoadMap;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.gamestates.searchlvl.SearchMainScreen;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class StartMenu extends BaseState implements GameStateInterface {

    private CustomButton btnSinglePlayer;
    private CustomButton btnSettings;
    private CustomButton btnCreateLvl;
    private CustomButton btnSearchLvl;

    public StartMenu(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons(){
        float btnX = (float) GAME_WIDTH / 2 - (float) ButtonImages.MENU_SINGLE_PLAYER.getWidth() / 2;
        float btnUnit = ButtonImages.MENU_CREATE_LVL.getHeight();
        btnSinglePlayer = new CustomButton(btnX, (float) (btnUnit * 1),
                ButtonImages.MENU_SINGLE_PLAYER.getWidth(), ButtonImages.MENU_SINGLE_PLAYER.getHeight());

        btnCreateLvl = new CustomButton(btnX, (float) (btnUnit * 2.5),
                ButtonImages.MENU_CREATE_LVL.getWidth(), ButtonImages.MENU_CREATE_LVL.getHeight());

        btnSearchLvl = new CustomButton(btnX, (float) (btnUnit * 4),
                ButtonImages.MENU_SEARCH_LVL.getWidth(), ButtonImages.MENU_SEARCH_LVL.getHeight());

        btnSettings = new CustomButton((float) (GAME_WIDTH - ButtonImages.MENU_SETTINGS.getWidth() * 1.5),
                (float) (ButtonImages.MENU_SETTINGS.getWidth() * .5),
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
                ButtonImages.MENU_SINGLE_PLAYER.getBtnImg(btnSinglePlayer.isPushed()),
                btnSinglePlayer.getHitbox().left,
                btnSinglePlayer.getHitbox().top, null);

        c.drawBitmap(
                ButtonImages.MENU_CREATE_LVL.getBtnImg(btnCreateLvl.isPushed()),
                btnCreateLvl.getHitbox().left,
                btnCreateLvl.getHitbox().top, null);

        c.drawBitmap(
                ButtonImages.MENU_SEARCH_LVL.getBtnImg(btnSearchLvl.isPushed()),
                btnSearchLvl.getHitbox().left,
                btnSearchLvl.getHitbox().top, null);

        c.drawBitmap(
                ButtonImages.MENU_SETTINGS.getBtnImg(btnSettings.isPushed()),
                btnSettings.getHitbox().left,
                btnSettings.getHitbox().top, null);


    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (btnSinglePlayer.isIn(event))
                btnSinglePlayer.setPushed(true);

            else if (btnCreateLvl.isIn(event))
                btnCreateLvl.setPushed(true);

            else if (btnSearchLvl.isIn(event))
                btnSearchLvl.setPushed(true);

            else if (btnSettings.isIn(event))
                btnSettings.setPushed(true);


        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (btnSinglePlayer.isIn(event)) {
                if (btnSinglePlayer.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.SINGLEPLAYER_LVL);
            } else if (btnCreateLvl.isIn(event)) {
                if (btnCreateLvl.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.CREATEDLVLSLIST);
            } else if (btnSearchLvl.isIn(event)) {
                if (btnSearchLvl.isPushed()) {
                    game.getMenu().setSearchMainScreen(new SearchMainScreen(game));
                    game.getMenu().setCurrentMenuState(Menu.MenuState.SEARCHLVL);
                }
            }

            else if (btnSettings.isIn(event)) {
                if (btnSettings.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.GAME_SETTINGS);
                }


                btnSinglePlayer.setPushed(false);
                btnCreateLvl.setPushed(false);
                btnSearchLvl.setPushed(false);
                btnSettings.setPushed(false);
            }
        }

}
