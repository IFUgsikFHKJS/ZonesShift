package com.zsgame.zonesshift.entities;


import com.zsgame.zonesshift.environments.Tile;
import com.zsgame.zonesshift.gamestates.Playing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    private boolean inRedZone;
    private byte gravitationDirection;




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



//
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

        checkForRedZone(tiles);
        inRedZone = Tile.isInRedZone();
        boolean canMove = true;

        checkForGraviZone(tiles);
        gravitationDirection = Tile.getGravitationDirection();

        canMove = switch (gravitationDirection) {
            case 0, 1 -> moveGravitationUpOrDown(tiles, delta);
            case 2 -> moveGravitationRight(tiles, delta);
            default -> canMove;
        };



        if (canMove)
            if (gravitationDirection == 0 || gravitationDirection == 1)
                x += xSpeed * -1;
            else
                y += ySpeed * -1;
    }



    private float calculateSpeed(double delta){
        float baseSpeed = (float) delta * 300;
        float speedMultiplayer;

        switch (gravitationDirection){
            case 0,1:
                speedMultiplayer = Math.abs(xDiff) / 200 * maxSpeed;

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
            case 2:
                speedMultiplayer = Math.abs(yDiff) / 200 * maxSpeed;

                if(speedMultiplayer < minSpeed)
                    speedMultiplayer = 0;
                else if (speedMultiplayer >= maxSpeed)
                    speedMultiplayer = maxSpeed;





                if(yDiff < 0)
                    speedMultiplayer *= -1;



                if(Math.abs(ySpeed) < Math.abs(speedMultiplayer * baseSpeed) || speedMultiplayer == 0){
                    float boost = maxSpeed / 50;
                    ySpeed +=  (yDiff > 0 ? boost : -boost);
                } else {
                    ySpeed = speedMultiplayer * baseSpeed;
                }

                if (ySpeed > 0)
                    setFaceDir(0);
                else if (ySpeed < 0)
                    setFaceDir(1);

                return ySpeed;
        }

        return 0;


    }

    private boolean moveGravitationUpOrDown(Tile[][] tiles, double delta){
        if(!movePlayer || inRedZone) {
            if (xSpeed != 0)
                xSpeed -= xSpeed > 0 ? maxSpeed / 50 : -maxSpeed / 50;
            // Blocking moving and setting speed to 0 if player hits solid block
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (checkCollisions(tile))
                        return false;
                }
            }
            x += xSpeed * -1;
            if(Math.abs(xSpeed) <= 0.5)
                xSpeed = 0;
            return false;
        }


        if (!isJumping || gravitationDirection == 1)
            if (gravitationDirection == 1 && Math.abs(xSpeed) < minSpeed && isJumping) {
                xSpeed = xDiff > 0 ? minSpeed : -minSpeed;
            }
            else
                xSpeed = calculateSpeed(delta);

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (checkCollisions(tile)) {
                    xSpeed = xDiff > 0 ? minSpeed : -minSpeed;
                    if (xDiff == 0) {
                        xSpeed = 0;
                    }
                    return false;
                }
            }
        }

        return true;
    }


    private boolean moveGravitationRight(Tile[][] tiles, double delta) {
        if(!movePlayer || inRedZone) {
            if (ySpeed != 0)
                ySpeed -= ySpeed > 0 ? maxSpeed / 50 : -maxSpeed / 50;
            // Blocking moving and setting speed to 0 if player hits solid block
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (checkCollisions(tile)) {
                        return false;
                    }
                }
            }
            y += ySpeed * -1;
            if(Math.abs(ySpeed) <= 0.5)
                ySpeed = 0;
            return false;
        }


        if (!isJumping)
                ySpeed = calculateSpeed(delta);

        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (checkCollisions(tile)) {
                    ySpeed = yDiff > 0 ? minSpeed : -minSpeed;
                    if (yDiff == 0) {
                        ySpeed = 0;
                    }
                    return false;
                }
            }
        }


        return true;
    }




    public void updatePlayerJump(double delta){



        Tile[][] tiles = Playing.getTiles();

        loop:
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) { tile.checkCollision(getHitbox().left + x ,
                        getHitbox().top + y,
                        getHitbox().right + x,
                        getHitbox().bottom + y);}
                if (Tile.getGravitationDirection() != 0)
                    break loop;
            }
        }

        gravitationDirection = Tile.getGravitationDirection();


        switch (gravitationDirection){
            case 0:
                jumpGravitationDown(tiles);
                if (hitboxDirection != 0) {
                    turnHitbox(0);
                    for (Tile[] row : tiles) {
                        for (Tile tile : row) {

                            if (tile != null) {
                                if (tile.checkCollision(getHitbox().left + x,
                                        getHitbox().top + y + ySpeed,
                                        getHitbox().right + x,
                                        getHitbox().bottom + y + ySpeed)) {
                                    System.out.println(1);
                                    y--;

                                }
                            }
                        }
                    }
                }
                break;
            case 1:
                jumpGravitationUp(tiles);
                break;
            case 2:
                if (hitboxDirection != 2)
                     turnHitbox(2);
                jumpGravitationRight(tiles);
                break;
        }

        y += ySpeed;

    }



    private void jumpGravitationDown(Tile[][] tiles) {
        if(isJumping){
            ySpeed += maxJumpSpeed / 25;
        }


        if (yDiff >= 100 && !isJumping && !inRedZone) {
            ySpeed = -maxJumpSpeed;
            isJumping = true;
        }



        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if (tile != null) {
                    if (tile.checkCollision(getHitbox().left + x,
                            getHitbox().top + y + ySpeed,
                            getHitbox().right + x,
                            getHitbox().bottom + y + ySpeed)) {


                        if (ySpeed >= 0)
                            isJumping = false;
                        ySpeed = 0;
                        return;

                    } else if (ySpeed == 0) {

                        ySpeed += maxJumpSpeed / 25;
                        isJumping = true;
                    }
                }
            }
        }

    }

    private void jumpGravitationUp(Tile[][] tiles) {
        if(isJumping){
            ySpeed -= maxJumpSpeed / 25;
        }


        if (yDiff <= -100 && !isJumping && !inRedZone) {
            ySpeed = maxJumpSpeed;
            isJumping = true;
        }



        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if (tile != null) {
                    if (tile.checkCollision(getHitbox().left + x,
                            getHitbox().top + y + ySpeed,
                            getHitbox().right + x,
                            getHitbox().bottom + y + ySpeed)) {


                        if (ySpeed <= 0)
                            isJumping = false;
//                    if(ySpeed < 0)
//                        y++;
                        ySpeed = 0;
                        return;

                    } else if (ySpeed == 0) {
                        ySpeed -= maxJumpSpeed / 25;
                        isJumping = true;
                    }
                }
            }
        }
    }

    private void jumpGravitationRight(Tile[][] tiles){
        if(isJumping){
            xSpeed += maxJumpSpeed / 25;
        }


        if (xDiff <= -100 && !isJumping && !inRedZone) {
            xSpeed = -maxJumpSpeed;
            isJumping = true;
        }



        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if (tile != null) {
                    if (tile.checkCollision(getHitbox().left + x,
                            getHitbox().top + y + xSpeed,
                            getHitbox().right + x,
                            getHitbox().bottom + y + xSpeed)) {


                        if (xSpeed >= 0)
                            isJumping = false;
                        xSpeed = 0;
                        return;

                    } else if (xSpeed == 0) {

                        xSpeed += maxJumpSpeed / 25;
                        isJumping = true;
                    }
                }
            }
        }
    }

    private boolean checkCollisions(Tile tile){

        return switch (gravitationDirection) {
            case 0, 1 -> {
                if (tile != null) {
                    yield (tile.checkCollision(getHitbox().left + x + xSpeed * -1,
                            getHitbox().top + y,
                            getHitbox().right + x + xSpeed * -1,
                            getHitbox().bottom + y));
                }
                yield false;
            }
            case 2 -> {
                if (tile != null) {
                    yield (tile.checkCollision(getHitbox().left + x,
                            getHitbox().top + y + ySpeed * -1,
                            getHitbox().right + x,
                            getHitbox().bottom + y + ySpeed * -1));
                }
                yield false;
            }
            default -> true;
        };


    }

    private void checkForRedZone(Tile[][] tiles){

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

    }

    private void checkForGraviZone(Tile[][] tiles){
        loop:
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) { tile.checkCollision(getHitbox().left + x ,
                        getHitbox().top + y,
                        getHitbox().right + x,
                        getHitbox().bottom + y);}
                if (Tile.getGravitationDirection() != 0)
                    break loop;
            }
        }
    }

    public Bitmap getPlayerBitmap(){
        return getGameCharType().getSprite(getAniIndex(),
                getFaceDir());
    }




//    public void update(double delta, boolean movePlayer){
//        if(movePlayer)
//            updateAnimation();
//    }

}
