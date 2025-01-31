package com.example.zonesshift.entities;


import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

public class Player extends Character{

    //For movement
    private boolean movePlayer;
    private float xTouch;
    private float xDiff;
    private float x;
    private float xSpeed;
    private float maxSpeed;
    private float minSpeed;

    //For jumping
    private boolean isJumping = true;
    private float yTouch;
    private float yDiff;
    private float y;
    private float ySpeed;
    private float maxJumpSpeed = 0.15625f;

    private int tileSize;

    //For zones
    public static boolean inRedZone;

    //UI


    public Player(PointF p) {
        super(new PointF(p.x, p.y), (float) (p.x + Playing.getTileSize() * 0.7), (float) (p.y + Playing.getTileSize() * 1.6), GameCharacters.PLAYER);
        tileSize = Playing.getTileSize();
        maxSpeed = (float) tileSize / 12;
        minSpeed = maxSpeed / 4;
        maxJumpSpeed *= tileSize;
    }

    public void drawPlayer(Canvas c) {
        float playerWidth = (float) ((float) ((tileSize) * 2) - (tileSize * 0.7)) / 2;
        float playerHeight = (float) (((double) tileSize * 2) - (tileSize * 1.5)) / 2;
        c.drawBitmap(getGameCharType().getSprite(getAniIndex(),
                        getFaceDir()),
                getHitbox().left + x - playerWidth,
                getHitbox().top + y - playerHeight,
                null);



//        Paint hitboxPaint =  new Paint();
//        hitboxPaint.setColor(Color.RED);
//        hitboxPaint.setStyle(Paint.Style.STROKE);
//        c.drawRect(getHitbox().left + x, getHitbox().top + y,
//                getHitbox().right + x, getHitbox().bottom + y,hitboxPaint);
    }

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
                resetAnimation();
        }
    }

    public void updatePlayerMove(double delta){
        Tile[][] tiles = Playing.getTiles();
        loop:
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) { tile.checkCollision(getHitbox().left + x ,
                        getHitbox().top + y,
                        getHitbox().right + x,
                        getHitbox().bottom + y);}
                if (Tile.isInRedZone())
                    break loop;
            }
        }
        inRedZone = Tile.isInRedZone();
        if(!movePlayer || inRedZone) {
            if (xSpeed != 0)
                xSpeed -= xSpeed > 0 ? maxSpeed / 50 : -maxSpeed / 50;
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


        if (!isJumping && !inRedZone)
            xSpeed = calculateSpeed(delta);

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (checkCollisions(tile)) {
                    xSpeed = xDiff > 0 ? minSpeed : -minSpeed;
                    if (xDiff == 0) {
                        xSpeed = 0;
                    }
                    return;
                }
            }
        }

        x += xSpeed * -1;
    }

    private float calculateSpeed(double delta){
        float baseSpeed = (float) delta * 300;
        float speedMultiplayer = Math.abs(xDiff) / 200 * maxSpeed;

        if(speedMultiplayer < minSpeed)
            speedMultiplayer = 0;
        else if (speedMultiplayer >= maxSpeed)
            speedMultiplayer = maxSpeed;





        if(xDiff < 0)
            speedMultiplayer *= -1;



        if(Math.abs(xSpeed) < Math.abs(speedMultiplayer * baseSpeed) || speedMultiplayer == 0){
            float boost = maxSpeed / 50;
            xSpeed +=  (xDiff > 0 ? boost : -boost);
        } else {
            xSpeed = speedMultiplayer * baseSpeed;
        }

        if (xSpeed > 0)
            setFaceDir(0);
        else if (xSpeed < 0)
            setFaceDir(1);

        return xSpeed;
    }

    public void updatePlayerJump(double delta){

        Tile[][] tiles = Playing.getTiles();

        if(isJumping){
            ySpeed += maxJumpSpeed / 25;
        }


        if (yDiff >= 100 && !isJumping && !inRedZone) {
            ySpeed = -maxJumpSpeed;
            isJumping = true;
        }



        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if (tile != null) {if(tile.checkCollision(getHitbox().left + x,
                        getHitbox().top + y + ySpeed,
                        getHitbox().right + x,
                        getHitbox().bottom + y + ySpeed)){

                    if(ySpeed >= 0)
                        isJumping = false;
                    ySpeed = 0;
                    return;

                }} else if (ySpeed == 0) {
                    ySpeed += maxJumpSpeed / 25;
                    isJumping = true;
                }

            }
        }

        y += ySpeed;

    }

    private boolean checkCollisions(Tile tile){
        if (tile != null) {return (tile.checkCollision(getHitbox().left + x + xSpeed * -1,
                getHitbox().top + y,
                getHitbox().right + x + xSpeed * -1,
                getHitbox().bottom + y));}
        return false;
    }


    public void setPlayerCords(float[] cords){
        x = cords[0];
        y = cords[1];
    }

//    public void update(double delta, boolean movePlayer){
//        if(movePlayer)
//            updateAnimation();
//    }

}
