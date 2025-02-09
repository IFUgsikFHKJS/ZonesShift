package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zonesshift.Game;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class StartMenu extends BaseState implements GameStateInterface {

    private CustomButton btnStart;

    public StartMenu(Game game) {
        super(game);
        btnStart = new CustomButton((float) GAME_WIDTH / 2 - (float) ButtonImages.MENU_SINGLEPLAYER.getWidth() / 2,
                (float) GAME_HEIGHT / 2 - (float) ButtonImages.MENU_SINGLEPLAYER.getHeight() / 2,
                ButtonImages.MENU_SINGLEPLAYER.getWidth(), ButtonImages.MENU_SINGLEPLAYER.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        c.drawBitmap(
                ButtonImages.MENU_SINGLEPLAYER.getBtnImg(btnStart.isPushed()),
                btnStart.getHitbox().left,
                btnStart.getHitbox().top, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (btnStart.isIn(event))
                btnStart.setPushed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (btnStart.isIn(event))
                if (btnStart.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.SINGLEPLAYERLVL);

            btnStart.setPushed(false);
        }
    }
}
