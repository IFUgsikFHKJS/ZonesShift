package com.zsgame.zonesshift.gamestates.menustates;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.gamestates.BaseState;
import com.zsgame.zonesshift.gamestates.Menu;
import com.zsgame.zonesshift.gamestates.searchlvl.SearchMainScreen;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.multiplayer.GameClient;
import com.zsgame.zonesshift.ui.ButtonImages;
import com.zsgame.zonesshift.ui.CustomButton;

import java.io.IOException;

public class StartMenu extends BaseState implements GameStateInterface {

    private CustomButton btnSinglePlayer;
    private CustomButton btnSettings;
    private CustomButton btnCreateLvl;
    private CustomButton btnMultiPlayer;
    private CustomButton btnSearchLvl;

    public StartMenu(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons(){
        float btnX = (float) GAME_WIDTH / 2 - (float) ButtonImages.MENU_SINGLE_PLAYER.getWidth() / 2;
        float btnUnit = GAME_HEIGHT / 9;
        btnSinglePlayer = new CustomButton(btnX, (float) (btnUnit * 0.5),
                ButtonImages.MENU_SINGLE_PLAYER.getWidth(), ButtonImages.MENU_SINGLE_PLAYER.getHeight());

        btnCreateLvl = new CustomButton(btnX, (float) (btnUnit * 2.5),
                ButtonImages.MENU_CREATE_LVL.getWidth(), ButtonImages.MENU_CREATE_LVL.getHeight());

        btnSearchLvl = new CustomButton(btnX, (float) (btnUnit * 4.5),
                ButtonImages.MENU_SEARCH_LVL.getWidth(), ButtonImages.MENU_SEARCH_LVL.getHeight());

        btnMultiPlayer = new CustomButton(btnX, (float) (btnUnit * 6.5),
                ButtonImages.MENU_MULTI_PLAYER.getWidth(), ButtonImages.MENU_MULTI_PLAYER.getHeight());

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

        c.drawBitmap(
                ButtonImages.MENU_MULTI_PLAYER.getBtnImg(btnMultiPlayer.isPushed()),
                btnMultiPlayer.getHitbox().left,
                btnMultiPlayer.getHitbox().top, null);
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

            else if (btnMultiPlayer.isIn(event))
                btnMultiPlayer.setPushed(true);

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
            } else if (btnMultiPlayer.isIn(event)) {
                if (btnMultiPlayer.isPushed()) {
                    try {
                        GameClient gameClient = new GameClient();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            else if (btnSettings.isIn(event)) {
                if (btnSettings.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.GAME_SETTINGS);
                }


                btnSinglePlayer.setPushed(false);
                btnCreateLvl.setPushed(false);
                btnSearchLvl.setPushed(false);
                btnMultiPlayer.setPushed(false);
                btnSettings.setPushed(false);
            }
        }

}
