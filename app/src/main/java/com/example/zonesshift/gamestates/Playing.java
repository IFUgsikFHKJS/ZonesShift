package com.example.zonesshift.gamestates;


import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.zonesshift.entities.Character;
import com.example.zonesshift.Game;
import com.example.zonesshift.entities.Player;
import com.example.zonesshift.environments.MapLoader;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.helpers.GameConstants;
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
    private static int tileSize = GAME_HEIGHT / 9;
    private final float tileOffsetX = (GAME_WIDTH - (float) (tileSize * 19)) / 2;
    private final float tileOffsetY = (GAME_HEIGHT - (float) (tileSize * 9)) / 2;
    public Playing(Game game){
        super(game);
        player = new Player(new PointF(tileSize * 2 + tileOffsetX, GAME_HEIGHT - tileSize * 3 - tileOffsetY));
        tiles =  MapLoader.loadMap(MainActivity.getGameContext(), "maps/map1.txt", 9, 19);
        if (GAME_WIDTH < tileSize * 19)
            tileSize = GAME_WIDTH / 19;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) tile.setSize(tileSize);
            }
        }
    }

    public static int getTileSize() {
        return tileSize;
    }

    @Override
    public void update(double delta) {
        updatePlayerMove(delta);
        updatePlayerJump(delta);
//        System.out.println(GAME_WIDTH / GAME_WIDTH_RES);
//        System.out.println(GAME_WIDTH);
//        System.out.println(GAME_WIDTH_RES);
//        System.out.println(GAME_HEIGHT_RES);
//        player.update(delta, movePlayer);
    }

    @Override
    public void render(Canvas c){
        c.drawColor(Color.BLACK); // Clear screen
        drawPlayer(c);
        drawMap(c);
    }

    private void drawPlayer(Canvas c) {
        float playerWidth = (float) ((float) ((tileSize) * 2) - (tileSize * 0.7)) / 2;
        float playerHeight = (float) (((double) tileSize * 2) - (tileSize * 1.5)) / 2;
        c.drawBitmap(player.getGameCharType().getSprite(player.getAniIndex(),
                        player.getFaceDir()),
                player.getHitbox().left + x - playerWidth,
                player.getHitbox().top + y - playerHeight,
                null);

//        Paint hitboxPaint =  new Paint();
//        hitboxPaint.setColor(Color.RED);
//        hitboxPaint.setStyle(Paint.Style.STROKE);
//        c.drawRect(player.getHitbox().left + x, player.getHitbox().top + y,
//                player.getHitbox().right + x, player.getHitbox().bottom + y,hitboxPaint);
//        System.out.println(player.getHitbox().left + x + " " + player.getHitbox().top + y + " " + player.getHitbox().right + x + " " +  player.getHitbox().bottom + y);
//        System.out.println(player.getHitbox().left + " " +  player.getHitbox().right + " " + player.getHitbox().top + " " + player.getHitbox().bottom);
    }

    private void drawMap(Canvas c) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[y][x] != null) {
                    int tileTexture = determineTexture(tiles, y, x, tiles[y][x].getType());
                    tiles[y][x].draw(c, tileOffsetX, tileOffsetY, tileTexture);
                }
            }
        }
    }

    private int determineTexture(Tile[][] map, int y, int x, char type){
        boolean up = y > 0 && map[y - 1][x] != null && map[y - 1][x].getType() == type;
        boolean down = y < map.length - 1 && map[y + 1][x] != null && map[y + 1][x].getType() == type;
        boolean left = x > 0 && map[y][x - 1] != null && map[y][x - 1].getType() == type;
        boolean right = x < map[0].length - 1 && map[y][x + 1] != null && map[y][x + 1].getType() == type;

        //Border on one side
        if (up && down && left && right) return 5;
        if (!up && down && left && right) return 1;
        if (up && !down && left && right) return 9;
        if (up && down && !left && right) return 4;
        if (up && down && left && !right) return 6;

        //Borders on adjacent sides
        if (!up && down && !left && right) return 0;
        if (!up && down && left && !right) return 2;
        if (up && !down && !left && right) return 8;
        if (up && !down && left && !right) return 10;

        //Borders on all sides
        if (!up && !down && !left && !right) return 14;

        //Borders on three sides
        if (!up && !down && !left && right) return 11;
        if (!up && !down && left && !right) return 15;
        if (!up && down && !left && !right) return 3;
        if (up && !down && !left && !right) return 7;

        if (up && down && !left && !right) return 12;
        if (!up && !down && left && right) return 13;


        return 14; // Default case
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
        loop:
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                checkCollisions(tile);
                if (Tile.isInRedZone())
                    break loop;
            }
        }
        inRedZone = Tile.isInRedZone();
        if(!movePlayer || inRedZone) {
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


        System.out.println(inRedZone);

        if (!isJumping && !inRedZone)
            xSpeed = calculateSpeed(delta);

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

        if(Math.abs(xSpeed) < Math.abs(speedMultiplayer * baseSpeed) || speedMultiplayer == 0){
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


        if (yDiff >= 100 && !isJumping && !inRedZone) {
            ySpeed = (float) -12.5;
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


    public void drawCharacter(@NonNull Canvas canvas, Character c){
        canvas.drawBitmap(c.getGameCharType().getSprite(1,1),
                c.getHitbox().left,
                c.getHitbox().top,
                null );
    }

}
