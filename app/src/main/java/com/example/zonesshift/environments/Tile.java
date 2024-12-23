package com.example.zonesshift.environments;

import static java.lang.Thread.sleep;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.zonesshift.entities.Player;
import com.example.zonesshift.gamestates.Playing;

public class Tile {
    private int x, y, size;
    private char type;
    public static Player player;

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
        if(playerX < x + size && playerX + playerWidth > x &&
                playerY < y + size && playerY + playerHeight > y){
            System.out.println(type);
            switch (type){
                case 'R':

                    break;
                case '1':
                    return true;
            }
        }
        return false;
    }
}
