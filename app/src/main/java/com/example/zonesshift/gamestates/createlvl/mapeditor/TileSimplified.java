package com.example.zonesshift.gamestates.createlvl.mapeditor;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.zonesshift.entities.Player;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.environments.Blocks;

public class TileSimplified implements BitmapMethods {

    private char type;
    private int textureId;
    private Bitmap bitmap;

    public TileSimplified(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public int getTextureId() {
        return textureId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
        updateBitmap();
    }

    public void updateBitmap() {
        switch (type) {
            case '1':
                bitmap = getScaledBitmap(Blocks.SOLID.getSprite(textureId));
                break;
            case 'R':
                bitmap = getScaledBitmap(Blocks.REDZONE.getSprite(textureId));
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
            case 'P':
                bitmap = new Player(new PointF(0,0)).getPlayerBitmap();
                break;
        }
    }

    public int determineTexture(TileSimplified[][] map, int y, int x, char type) {
        boolean up = y > 0 && map[y - 1][x] != null && map[y - 1][x].getType() == type;
        boolean down = y < map.length - 1 && map[y + 1][x] != null && map[y + 1][x].getType() == type;
        boolean left = x > 0 && map[y][x - 1] != null && map[y][x - 1].getType() == type;
        boolean right = x < map[0].length - 1 && map[y][x + 1] != null && map[y][x + 1].getType() == type;

        // All four sides connected
        if (up && down && left && right) return 5;
        if (!up && down && left && right) return 1;
        if (up && !down && left && right) return 9;
        if (up && down && !left && right) return 4;
        if (up && down && left && !right) return 6;

        // Two sides
        if (!up && down && !left && right) return 0;
        if (!up && down && left && !right) return 2;
        if (up && !down && !left && right) return 8;
        if (up && !down && left && !right) return 10;

        // No adjacent
        if (!up && !down && !left && !right) return 14;

        // One side
        if (!up && !down && !left && right) return 11;
        if (!up && !down && left && !right) return 15;
        if (!up && down && !left && !right) return 3;
        if (up && !down && !left && !right) return 7;

        if (up) return 12;
        return 13;
    }

}
