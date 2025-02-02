package com.example.zonesshift.environments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zonesshift.gamestates.Playing;


public class Tile {
    private int x, y;
    private int size = 32;
    private final char type;
    public static Playing playing;
    private int tileOffsetX;
    private int tileOffsetY;
    private static boolean inRedZone;
    private int textureID;
    private Bitmap bitmap = null;

    public Tile(int x, int y, int size, char type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }
    public void setSize(int size){
//        System.out.println(this.size);
        this.size = size;
        x *= size;
        y *= size;
        setBitmap();
//        System.out.println(this.size);
    }

    private void setBitmap(){
        switch (type) {
            case '1':
                bitmap = Blocks.SOLID.getSprite(textureID);
                break;
            case 'R':
                bitmap = Blocks.REDZONE.getSprite(textureID);
                break;
            case 'W':
                bitmap = Blocks.WIN.getSprite(0);
                break;
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        if(bitmap != null){
            switch (type) {
                case 'Y': paint.setColor(Color.YELLOW); break;
                case 'B': paint.setColor(Color.BLUE); break;
                default: paint.setColor(Color.WHITE); break;
            }
        }

        
        if(type != '1' && type != 'R' && type != 'W') {
            canvas.drawRect(x + tileOffsetX, y + tileOffsetY, x + size + tileOffsetX, y + size + tileOffsetY, paint);
        }
        else {
            canvas.drawBitmap(bitmap, x + tileOffsetX, y + tileOffsetY, null);
        }
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
                case 'W':
                    playing.nextMap();
                case '1':
                    return true;
            }
        }
        return false;
    }

    public int determineTexture(Tile[][] map, int y, int x, char type){
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

    public static boolean isInRedZone() {
        return inRedZone;
    }

    public char getType() {
        return type;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public void setTileOffsets(int tileOffsetX, int tileOffsetY) {
        this.tileOffsetX = tileOffsetX;
        this.tileOffsetY = tileOffsetY;
    }
}
