package com.example.zonesshift.gamestates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.zonesshift.entities.Character;
import com.example.zonesshift.Game;
import com.example.zonesshift.environments.Blocks;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.environments.mapmanagment.MapManager;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class Playing extends BaseState implements GameStateInterface, BitmapMethods {

    private static MapManager mapManager;
    private CustomButton btnRestart;

    public Playing(Game game){
        super(game);
        mapManager = new MapManager();
        mapManager.setCurrentMap(0);
        btnRestart = new CustomButton(20, 20, ButtonImages.LVL_RESTART.getWidth(), ButtonImages.LVL_RESTART.getHeight());
    }

    public static int getTileSize() {
        return mapManager.getCurrentMap().getTileSize();
    }

    public static Tile[][] getTiles(){
        return mapManager.getCurrentMap().getTiles();
    }

    public void nextMap(){
        mapManager.nextMap();
    }

    @Override
    public void update(double delta) {
        mapManager.getPlayer().updatePlayerMove(delta);
        mapManager.getPlayer().updatePlayerJump(delta);
    }

    @Override
    public void render(Canvas c){
        mapManager.getPlayer().drawPlayer(c);
        mapManager.getCurrentMap().draw(c);
        c.drawBitmap(
                ButtonImages.LVL_RESTART.getBtnImg(btnRestart.isPushed()),
                btnRestart.getHitbox().left,
                btnRestart.getHitbox().top, null);
    }



    @Override
    public void touchEvents(MotionEvent event) {

        if(!isIn(event, btnRestart)){
            mapManager.getPlayer().touchEvents(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isIn(event, btnRestart))
                btnRestart.setPushed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isIn(event, btnRestart))
                if (btnRestart.isPushed())
                    mapManager.restartCurrentMap();

            btnRestart.setPushed(false);
        }
    }

    private boolean isIn(MotionEvent e, CustomButton b) {
        return b.getHitbox().contains(e.getX(), e.getY());
    }

    public void drawCharacter(@NonNull Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

}
