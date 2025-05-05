package com.example.zonesshift.environments;


import android.graphics.Bitmap;
import android.graphics.Canvas;


import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;


public class Tile implements BitmapMethods {
    private int x, y;
    private int size = 32;
    private final char type;
    public static Playing playing;
    private int tileOffsetX;
    private int tileOffsetY;
    private static boolean inRedZone;
    private static byte gravitationDirection;
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
//        System.out.println(this.size);
    }

    public void updateBitmap(){
        switch (type) {
            case '1':
                bitmap = getScaledBitmap(Blocks.SOLID.getSprite(textureID));
                break;
            case 'R':
                bitmap = getScaledBitmap(Blocks.REDZONE.getSprite(textureID));
                break;
            case 'W':
                bitmap = getScaledBitmap(Blocks.WIN.getSprite(0));
                break;
            case 'G':
                bitmap = getScaledBitmap(Blocks.GRAVIZONE.getSprite(0));
                break;
            case 'g':
                bitmap = getScaledBitmap(Blocks.GRAVIZONE.getSprite(1));
                break;
        }
    }


    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        switch (type) {
//            case 'G': case 'g': case 'ℊ': paint.setColor(Color.YELLOW); paint.setAlpha(100); break;
////            case 'B': paint.setColor(Color.BLUE); break;
////            default: paint.setColor(Color.WHITE); break;
//        }
        
//        if(type == 'G' || type == 'g' || type == 'ℊ') {
//            canvas.drawRect(x + tileOffsetX, y + tileOffsetY, x + size + tileOffsetX, y + size + tileOffsetY, paint);
//        }
//        else {
            canvas.drawBitmap(bitmap, x + tileOffsetX, y + tileOffsetY, null);
//        }
    }

    public boolean checkCollision(float playerX,float playerY,float playerWidth,float playerHeight) {
        inRedZone = false;
        gravitationDirection = 0;
        if(playerX < x + tileOffsetX + size && playerWidth > x + tileOffsetX &&
                playerY < y + size + tileOffsetY && playerHeight > y + tileOffsetY){
            switch (type){
                case 'R':
                    inRedZone = true;
                    break;
                case 'G':
                    gravitationDirection = 1;
                    break;
                case 'ℊ':
                    gravitationDirection = 3;
                    break;
                case 'g':
                    gravitationDirection = 2;
                    break;
                case 'W':
                    playing.win();
                    break;
//                    playing.nextMap();
                case '1':
//                    System.out.println(playerY + " " + ( y + size + tileOffsetY) + " " + playerHeight + " " + (y + tileOffsetY));
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
        if (up && down && left) return 6;

        //Borders on adjacent sides
        if (!up && down && !left && right) return 0;
        if (!up && down && left) return 2;
        if (up && !down && !left && right) return 8;
        if (up && !down && left) return 10;

        //Borders on all sides
        if (!up && !down && !left && !right) return 14;

        //Borders on three sides
        if (!up && !down && !left) return 11;
        if (!up && !down && !right) return 15;
        if (!up && down) return 3;
        if (up && !down) return 7;

        if (up) return 12;
        return 13;
    }

    public static boolean isInRedZone() {
        return inRedZone;
    }

    public static byte getGravitationDirection(){
        return gravitationDirection;
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
