package com.example.zonesshift.gamestates;


import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT_RES;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH_RES;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.example.zonesshift.entities.Character;
import com.example.zonesshift.Game;
import com.example.zonesshift.entities.Player;
import com.example.zonesshift.environments.MapLoader;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.MainActivity;

public class Playing extends BaseState implements GameStateInterface {

    private boolean movePlayer;
    private boolean isJumping = true;
    private final Player player;

    //For movement
    private float xTouch;
    private float xDiff;
    private float x;
    private float xSpeed;
    private Tile[][] tiles;
    public Playing(Game game){
        super(game);
        player = new Player();
        tiles =  MapLoader.loadMap(MainActivity.getGameContext(), "maps/map1.txt", 9, 19);
        Tile.player = player;
    }

    @Override
    public void update(double delta) {
        updatePlayerMove(delta);

    }

    @Override
    public void render(Canvas c){
        drawMap(c);
        drawPlayer(c);
    }

    private void drawPlayer(Canvas c) {

        c.drawBitmap(player.getGameCharType().getSprite(0,0),
                player.getHitbox().left + x,
                player.getHitbox().top,
                null);
    }

    private void drawMap(Canvas c) {
        c.drawColor(Color.BLACK); // Clear screen
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) tile.draw(c);
            }
        }
    }

    @Override
    public void touchEvents(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xTouch = event.getX();
                movePlayer = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float xNew = event.getX();
                xDiff = xTouch - xNew;
                movePlayer = true;
                break;
            case MotionEvent.ACTION_UP:
                movePlayer = false;
        }
    }

    private void updatePlayerMove(double delta){
        if(!movePlayer) {
            xSpeed -= xSpeed / 50;
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (tile != null) {if(tile.checkCollision(player.getHitbox().left + x + xSpeed * -1, player.getHitbox().top, (float) GAME_WIDTH / GAME_WIDTH_RES * 2, (float) GAME_HEIGHT / GAME_HEIGHT_RES * 2)){
                        xSpeed = 0;
                        return;
                    }};
                }
            }
            x += xSpeed * -1;
            if(Math.abs(xSpeed) <= 0.5)
                xSpeed = 0;
            return;
        }

        float baseSpeed = (float) delta * 300;
        float speedMultiplayer = Math.abs(xDiff);

        if(speedMultiplayer < 100 && speedMultiplayer >= 80)
            speedMultiplayer = 50;
        else if (speedMultiplayer > 200)
            speedMultiplayer = 200;

        if(xDiff < 0)
            speedMultiplayer *= -1;



        speedMultiplayer /= 100;

        if(Math.abs(xSpeed) < speedMultiplayer * baseSpeed){
            float boost = 0.1f;
                xSpeed +=  (xDiff > 0 ? boost : -boost);
                if(xDiff == 0){
                    if(xSpeed > 0){
//                        xDiff -= boost;
                    } else {
                        xDiff += boost;
                    }
                }
        } else {
            xSpeed = speedMultiplayer * baseSpeed;
        }

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) {if(tile.checkCollision(player.getHitbox().left + x + xSpeed * -1, player.getHitbox().top, (float) GAME_WIDTH / GAME_WIDTH_RES * 2, (float) GAME_HEIGHT / GAME_HEIGHT_RES * 2)){
                    return;
                }};
            }
        }



        x += xSpeed * -1;
//        System.out.println(xSpeed + " " + xDiff);

    }

    public void drawCharacter(Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

    public Player getPlayer() {
        return player;
    }
}
