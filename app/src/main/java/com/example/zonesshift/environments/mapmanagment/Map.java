package com.example.zonesshift.environments.mapmanagment;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;

import com.example.zonesshift.environments.Tile;


public class Map {


    private Tile[][] tiles;
    private int tileSize;
    private int tileOffsetX, tileOffsetY;

    public Map(Tile[][] tiles){
        this.tiles = tiles;
        this.tileSize = GAME_WIDTH / tiles[0].length;
//        while (tileSize % 32 != 0){
//            tileSize--;
//        }
        System.out.println("Size " + tileSize);
        System.out.println(GAME_WIDTH);
//        while (tileSize * tiles.length > GAME_HEIGHT){
//            tileSize -= tileSize / 50;
//        }
        this.tileOffsetX = (GAME_WIDTH - (tileSize * tiles[0].length)) / 2;
        this.tileOffsetY = (GAME_HEIGHT - (tileSize * tiles.length)) / 2;
        for (Tile[] row : this.tiles) {
            for (Tile tile : row) {
                if (tile != null) tile.setSize(tileSize);
            }
        }
    }





    public void draw(Canvas c){
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[y][x] != null) {
                    if(tiles[y][x].getType() != 'P') {
                        int tileTexture = determineTexture(tiles, y, x, tiles[y][x].getType());
                        tiles[y][x].draw(c, tileOffsetX, tileOffsetY, tileTexture);
//                        System.out.println(1);
                    }
                }
            }
        }
    }

    public float[] getPlayerCords(){
        float[] cords = new float[2];
        for(int y = 0; y < tiles.length; y++){
            for(int x = 0; x < tiles[y].length; x++){
                if (tiles[y][x] != null) {
                    if (tiles[y][x].getType() == 'P') {
                        cords[0] = x * tileSize + tileOffsetX;
                        cords[1] = y * tileSize + tileOffsetY;
                        System.out.println("CORDS " +cords[0] + " " + cords[1] + " " + getTileSize());

                    }
                }
            }
        }
        return cords;
    }

    private int determineTexture(Tile[][] map, int y, int x, char type){
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

    public int getTileSize(){
        return tileSize;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public float getTileOffsetX() {
        return tileOffsetX;
    }

    public float getTileOffsetY() {
        return tileOffsetY;
    }
}
