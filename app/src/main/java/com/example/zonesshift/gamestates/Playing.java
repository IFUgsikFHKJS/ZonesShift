package com.example.zonesshift.gamestates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.zonesshift.R;
import com.example.zonesshift.entities.Character;
import com.example.zonesshift.Game;
import com.example.zonesshift.environments.Blocks;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.environments.mapmanagment.MapManager;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

public class Playing extends BaseState implements GameStateInterface, BitmapMethods {

    private static MapManager mapManager;
    private CustomButton btnRestart;
    private CustomButton btnLvls;

    public Playing(Game game){
        super(game);
        mapManager = new MapManager();
        setCurrentMap(0);
        btnRestart = new CustomButton(20, 20, ButtonImages.LVL_RESTART.getWidth(), ButtonImages.LVL_RESTART.getHeight());
        btnLvls = new CustomButton(GAME_WIDTH - 20 - ButtonImages.PLAYING_TO_LVL.getWidth(), 20, ButtonImages.PLAYING_TO_LVL.getWidth(), ButtonImages.PLAYING_TO_LVL.getHeight());}

    public void setCurrentMap(int mapID){
        mapManager.setCurrentMap(mapID);
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
        drawButtons(c);
    }

    private void drawButtons(Canvas c){
        c.drawBitmap(
                ButtonImages.LVL_RESTART.getBtnImg(btnRestart.isPushed()),
                btnRestart.getHitbox().left,
                btnRestart.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.PLAYING_TO_LVL.getBtnImg(btnLvls.isPushed()),
                btnLvls.getHitbox().left,
                btnLvls.getHitbox().top, null);
    }




    @Override
    public void touchEvents(MotionEvent event) {

        if(!btnRestart.isIn(event) && !btnLvls.isIn(event)){
            mapManager.getPlayer().touchEvents(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (btnRestart.isIn(event))
                btnRestart.setPushed(true);
            else if (btnLvls.isIn(event))
                btnLvls.setPushed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (btnRestart.isIn(event))
                if (btnRestart.isPushed())
                    mapManager.restartCurrentMap();
            if (btnLvls.isIn(event))
                if (btnLvls.isPushed())
                    game.setCurrentGameState(Game.GameState.MENU);

            btnRestart.setPushed(false);
            btnLvls.setPushed(false);
        }
    }



    public void drawCharacter(@NonNull Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

}
