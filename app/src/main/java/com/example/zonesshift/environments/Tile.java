package com.example.zonesshift.environments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zonesshift.gamestates.Playing;

public class Tile {
    private int x, y, size;
    private char type;
    public static Playing playing;

    public Tile(int x, int y, int size, char type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        switch (type) {
            case '1': paint.setColor(Color.GRAY); break;
            case 'R': paint.setColor(Color.RED); break;
            case 'Y': paint.setColor(Color.YELLOW); break;
            case 'B': paint.setColor(Color.BLUE); break;
            default: paint.setColor(Color.WHITE); break;
        }
        canvas.drawRect(x, y, x + size, y + size, paint);
    }

    public boolean checkCollision(float playerX,float playerY,float playerWidth,float playerHeight) {
//        System.out.println(playerX + " " + playerWidth + " " + playerY + " " + playerHeight);
        if(playerX < x + size && playerWidth > x &&
                playerY < y + size && playerHeight > y){
//            System.out.println(type);
            switch (type){
                case 'R':
//                    System.out.println(type);
                    Playing.inRedZone = true;
                    break;
                case '1':
                    return true;
            }
        }
        return false;
    }
}
