package com.example.zonesshift.gamestates;


import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT_RES;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH_RES;
import static com.example.zonesshift.helpers.GameConstants.TILE_WIDTH;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.zonesshift.entities.Character;
import com.example.zonesshift.Game;
import com.example.zonesshift.entities.Player;
import com.example.zonesshift.environments.MapLoader;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.MainActivity;

public class Playing extends BaseState implements GameStateInterface {

    private final Player player;

    //For movement
    private boolean movePlayer;
    private float xTouch;
    private float xDiff;
    private float x;
    private float xSpeed;

    //For jumping
    private boolean isJumping = true;
    private float yTouch;
    private float yDiff;
    private float y;
    private float ySpeed;

    //For zones
    public static boolean inRedZone;

    private final Tile[][] tiles;
    private final float tileOffsetX = (GAME_WIDTH - (float) (TILE_WIDTH * 19)) / 2;
    private final float tileOffsetY = (GAME_HEIGHT - (float) (TILE_WIDTH * 9)) / 2;
    public Playing(Game game){
        super(game);
        player = new Player();
        tiles =  MapLoader.loadMap(MainActivity.getGameContext(), "maps/map1.txt", 9, 19);
    }

    @Override
    public void update(double delta) {
        updatePlayerMove(delta);
        updatePlayerJump(delta);
//        player.update(delta, movePlayer);
    }

    @Override
    public void render(Canvas c){
        drawMap(c);
        drawPlayer(c);
    }

    private void drawPlayer(Canvas c) {
        float playerWidth = (float) ((float) ((GAME_WIDTH / GAME_WIDTH_RES) * 2) - (GAME_WIDTH*0.7 / GAME_WIDTH_RES)) / 2;
        float playerHeight = (float) (((double) GAME_HEIGHT / GAME_HEIGHT_RES * 2) - (GAME_HEIGHT * 1.5 / GAME_HEIGHT_RES)) / 2;
        c.drawBitmap(player.getGameCharType().getSprite(player.getAniIndex(),
                        player.getFaceDir()),
                player.getHitbox().left + x - playerWidth,
                player.getHitbox().top + y - playerHeight,
                null);

        Paint hitboxPaint =  new Paint();
        hitboxPaint.setColor(Color.RED);
        hitboxPaint.setStyle(Paint.Style.STROKE);
        c.drawRect(player.getHitbox().left + x, player.getHitbox().top + y,
                player.getHitbox().right + x, player.getHitbox().bottom + y,hitboxPaint);
//        System.out.println(player.getHitbox().left + x + " " + player.getHitbox().top + y + " " + player.getHitbox().right + x + " " +  player.getHitbox().bottom + y);
//        System.out.println(player.getHitbox().left + " " +  player.getHitbox().right + " " + player.getHitbox().top + " " + player.getHitbox().bottom);
    }

    private void drawMap(Canvas c) {
        c.drawColor(Color.BLACK); // Clear screen
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) tile.draw(c, tileOffsetX, tileOffsetY);
            }
        }
    }

    @Override
    public void touchEvents(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xTouch = event.getX();
                yTouch = event.getY();
                movePlayer = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float xNew = event.getX();
                float yNew = event.getY();
                xDiff = xTouch - xNew;
                yDiff = yTouch - yNew;

                movePlayer = true;
                break;
            case MotionEvent.ACTION_UP:
                xTouch = 0; yTouch = 0; xDiff = 0; yDiff = 0;
                movePlayer = false;
                player.resetAnimation();
        }
    }

    private void updatePlayerMove(double delta){
        Playing.inRedZone = false;
        if(!movePlayer) {
            xSpeed -= xSpeed / 50;
            // Blocking moving and setting speed to 0 if player hits solid block
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (checkCollisions(tile))
                        return;
                }
            }
            x += xSpeed * -1;
            if(Math.abs(xSpeed) <= 0.5)
                xSpeed = 0;
            return;
        }
        if(inRedZone)
            System.out.println(true);
        if (!isJumping && !inRedZone)
            xSpeed = calculateSpeed(delta);
        else if(inRedZone) return;

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (checkCollisions(tile))
                    return;
            }
        }

        x += xSpeed * -1;
    }

    private boolean checkCollisions(Tile tile){
        if (tile != null) {return (tile.checkCollision(player.getHitbox().left + x + xSpeed * -1,
                player.getHitbox().top + y,
                player.getHitbox().right + x + xSpeed * -1,
                player.getHitbox().bottom + y));}
        return false;
    }

    private float calculateSpeed(double delta){
        float baseSpeed = (float) delta * 300;
        float speedMultiplayer = Math.abs(xDiff);

        if(speedMultiplayer < 100 && speedMultiplayer >= 80)
            speedMultiplayer = 50;
        else if (speedMultiplayer > 200)
            speedMultiplayer = 200;

        if(xDiff < 0)
            speedMultiplayer *= -1;


        speedMultiplayer /= 100;

        if(Math.abs(xSpeed) < speedMultiplayer * baseSpeed || speedMultiplayer == 0){
            float boost = 0.1f;
            xSpeed +=  (xDiff > 0 ? boost : -boost);
//                        xDiff += boost;
        } else {
            xSpeed = speedMultiplayer * baseSpeed;
        }

        if (xSpeed > 0)
            player.setFaceDir(0);
        else if (xSpeed < 0)
            player.setFaceDir(1);

        return xSpeed;
    }

    private void updatePlayerJump(double delta){
        float baseSpeed = (float) delta * 300;

        if(isJumping){
            ySpeed += 0.5f;
        }


        if (yDiff >= 100 && !isJumping) {
            ySpeed = -12;
            isJumping = true;
        }



        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if (tile != null) {if(tile.checkCollision(player.getHitbox().left + x,
                        player.getHitbox().top + y + ySpeed,
                        player.getHitbox().right + x,
                        player.getHitbox().bottom + y + ySpeed)){

                    if(ySpeed >= 0)
                        isJumping = false;
                    ySpeed = 0;
                    return;

                }} else if (ySpeed == 0) {
                    ySpeed += 0.5f;
                    isJumping = true;
                }

            }
        }

        y += ySpeed;

//        System.out.println(yDiff + " " + ySpeed + " " + y + " " + isJumping);
    }

    public void drawCharacter(Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

}
