package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.HOME;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zonesshift.Game;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class SinglePlayerLevels extends BaseState implements GameStateInterface {
    private ButtonImages[] lvlButtonImages = new ButtonImages[7];
    private CustomButton[] lvlButtons = new  CustomButton[7];
    private CustomButton btnHome;


    public SinglePlayerLevels(Game game) {
        super(game);
        initLvlButtons();
    }

    private void initLvlButtons() {

        lvlButtonImages[0] = ButtonImages.LVL1;
        lvlButtonImages[1] = ButtonImages.LVL2;
        lvlButtonImages[2] = ButtonImages.LVL3;
        lvlButtonImages[3] = ButtonImages.LVL4;
        lvlButtonImages[4] = ButtonImages.LVL5;
        lvlButtonImages[5] = ButtonImages.LVL6;
        lvlButtonImages[6] = ButtonImages.LVL7;

        int unit = GAME_WIDTH / 13;

        for (int i = 0; i < lvlButtons.length; i++){
            lvlButtons[i] = new CustomButton(2 * unit + i % 5 * 2 * unit,
                    (i >= 5 ? unit / 2 : unit) + unit / 2 +  ( i / 5) * 2 * unit,
                    ButtonImages.LVL1.getWidth(), ButtonImages.LVL1.getHeight());
        }

        btnHome = new CustomButton(unit / 2, unit / 2, HOME.getWidth(), HOME.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        drawButtons(c);
    }

    private void drawButtons(Canvas c) {
        for(int i = 0; i < lvlButtons.length; i++){
            c.drawBitmap(
                    lvlButtonImages[i].getBtnImg(lvlButtons[i].isPushed()),
                    lvlButtons[i].getHitbox().left,
                    lvlButtons[i].getHitbox().top, null);
        }

        c.drawBitmap(HOME.getBtnImg(btnHome.isPushed()),
                btnHome.getHitbox().left,
                btnHome.getHitbox().top, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < lvlButtons.length; i++){
                if (lvlButtons[i].isIn(event)) {
                    lvlButtons[i].setPushed(true);
                    break;
                }
            }

            if(btnHome.isIn(event))
                btnHome.setPushed(true);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            for (int i = 0; i < lvlButtons.length; i++){
                if (lvlButtons[i].isIn(event))
                    if (lvlButtons[i].isPushed()) {
                        game.getPlaying().setCurrentMap(i);
                        game.setCurrentGameState(Game.GameState.PLAYING);
                        lvlButtons[i].setPushed(false);
                        break;
                    }
            }

            if (btnHome.isIn(event))
                if (btnHome.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.START_MENU);


            for (CustomButton button : lvlButtons){
                button.setPushed(false);
            }
            btnHome.setPushed(false);
        }
    }
}
