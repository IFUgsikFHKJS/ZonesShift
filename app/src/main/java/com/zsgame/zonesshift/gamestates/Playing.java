package com.zsgame.zonesshift.gamestates;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.zsgame.zonesshift.entities.Character;
import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.environments.Tile;
import com.zsgame.zonesshift.environments.mapmanagment.Map;
import com.zsgame.zonesshift.gamestates.createlvl.mapeditor.MapEditorActivity;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.environments.mapmanagment.MapManager;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.main.MainActivity;
import com.zsgame.zonesshift.ui.ButtonImages;
import com.zsgame.zonesshift.ui.CustomButton;

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

    public void setCurrentOnlineMap(Map map, int id){
        mapManager.setCurrentOnlineMap(map, id);
    }


    public static int getTileSize() {
        return mapManager.getCurrentMap().getTileSize();
    }

    public static Tile[][] getTiles(){
        return mapManager.getCurrentMap().getTiles();
    }


    @Override
    public void update(double delta) {
        mapManager.getPlayer().updatePlayerMove(delta);
        mapManager.getPlayer().updatePlayerJump(delta);
        mapManager.updateMap(delta);
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
                if (btnLvls.isPushed()){
                    if (mapManager.getCurrentMapId() != -1)
                        if (mapManager.getCurrentMapId() >= 100)
                            game.setCurrentGameState(Game.GameState.MENU);
                        else
                            game.setCurrentGameState(Game.GameState.LEVELSCREEN);
                    else {
                        MapEditorActivity a = (MapEditorActivity) GamePanel.getGameContext();
                        a.returnToEditor(false);
                    }

                }

            btnRestart.setPushed(false);
            btnLvls.setPushed(false);
        }
    }

    public void restartCurrentMap(){
        mapManager.restartCurrentMap();
    }



    public void drawCharacter(@NonNull Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

    public void win() {
        mapManager.win();
        if (mapManager.getCurrentMapId() != -1) {
            if (mapManager.getCurrentMapId() >= 100) {
                MainActivity.getGame().setCurrentGameState(Game.GameState.MENU);
            } else
                MainActivity.getGame().setCurrentGameState(Game.GameState.LEVELSCREEN);
        }
        else {
            MapEditorActivity a = (MapEditorActivity) GamePanel.getGameContext();
            if (a.isContentView())
                a.returnToEditor(true);
        }
    }

    public static MapManager getMapManager(){
        return mapManager;
    }
}
