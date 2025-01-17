package com.example.zonesshift.environments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zonesshift.gamestates.Playing;

public class Tile {
    private final int x, y;
    private int size;
    private final char type;
    public static Playing playing;
    private float tileOffsetX;
    private float tileOffsetY;
    private static boolean inRedZone;

    public Tile(int x, int y, int size, char type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }
    public void setSize(int size){
        System.out.println(this.size);
        this.size = size;
        System.out.println(this.size);
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
//        System.out.println(size);
    }

    public boolean checkCollision(float playerX,float playerY,float playerWidth,float playerHeight) {
        inRedZone = false;
        if(playerX < x + tileOffsetX + size && playerWidth > x + tileOffsetX &&
                playerY < y + size + tileOffsetY && playerHeight > y + tileOffsetY){
            switch (type){
                case 'R':
                    inRedZone = true;
                    break;
                case '1':
                    return true;
            }
        }
        return false;
    }

    public static boolean isInRedZone() {
        return inRedZone;
    }
}
