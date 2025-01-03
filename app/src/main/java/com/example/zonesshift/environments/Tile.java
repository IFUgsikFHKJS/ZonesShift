package com.example.zonesshift.environments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zonesshift.gamestates.Playing;

public class Tile {
    private int x, y, size;
    private char type;
    public static Playing playing;
    private float tileOffsetX;
    private float tileOffsetY;

    public Tile(int x, int y, int size, char type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }

    public void draw(Canvas canvas, float tileOffsetX, float tileOffsetY) {
        Paint paint = new Paint();
        switch (type) {
            case '1': paint.setColor(Color.GRAY); break;
            case 'R': paint.setColor(Color.RED); break;
            case 'Y': paint.setColor(Color.YELLOW); break;
            case 'B': paint.setColor(Color.BLUE); break;
            default: paint.setColor(Color.WHITE); break;
        }
        canvas.drawRect(x + tileOffsetX, y + tileOffsetY, x + size + tileOffsetX, y + size + tileOffsetY, paint);
        this.tileOffsetX = tileOffsetX;
        this.tileOffsetY = tileOffsetY;
    }

    public boolean checkCollision(float playerX,float playerY,float playerWidth,float playerHeight) {
        if(playerX < x + tileOffsetX + size && playerWidth > x + tileOffsetX &&
                playerY < y + size + tileOffsetY && playerHeight > y + tileOffsetY){
            switch (type){
                case 'R':
                    Playing.inRedZone = true;
                    break;
                case '1':
                    return true;
            }
        }
        return false;
    }
}
