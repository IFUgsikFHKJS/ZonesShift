package com.example.zonesshift.environments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zonesshift.gamestates.Playing;

public class Tile {
    private final int x, y;
    private int size = 32;
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
//        System.out.println(this.size);
        this.size = size;
//        System.out.println(this.size);
    }

    public void draw(Canvas canvas, float tileOffsetX, float tileOffsetY, int tileTexture) {
        Paint paint = new Paint();
        Bitmap bitmap = null;
        switch (type) {
            case '1': paint.setColor(Color.GRAY);
                bitmap = Blocks.SOLID.getSprite(tileTexture);
                break;
            case 'R': paint.setColor(Color.RED);
                bitmap = Blocks.REDZONE.getSprite(tileTexture);
                break;
            case 'Y': paint.setColor(Color.YELLOW); break;
            case 'B': paint.setColor(Color.BLUE); break;
            default: paint.setColor(Color.WHITE); break;
        }
        
        if(type != '1' && type != 'R') {
            canvas.drawRect(x + tileOffsetX, y + tileOffsetY, x + size + tileOffsetX, y + size + tileOffsetY, paint);
        }
        else {
            canvas.drawBitmap(bitmap, x + tileOffsetX, y + tileOffsetY, null);
        }
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
                    if(playerX < (x + tileOffsetX + size) * 1.1 && playerWidth > (x + tileOffsetX) * 0.9 &&
                            playerY < (y + size + tileOffsetY * 1.1) && playerHeight > (y + tileOffsetY) * 0.9)
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

    public char getType() {
        return type;
    }
}
