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
        while (tileSize * tiles.length > GAME_HEIGHT){
            tileSize -= 1;
        }
        this.tileOffsetX = (GAME_WIDTH - (tileSize * tiles[0].length)) / 2;
        this.tileOffsetY = (GAME_HEIGHT - (tileSize * tiles.length)) / 2;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[y][x] != null) {
                    tiles[y][x].setSize(tileSize);
                    tiles[y][x].setTextureID(tiles[y][x].determineTexture(tiles, y, x, tiles[y][x].getType()));
                    tiles[y][x].setTileOffsets(tileOffsetX, tileOffsetY);
                }
            }
        }
    }


    public void updateBitmap(){
        for (Tile[] row : tiles){
            for (Tile tile : row){
                if(tile != null) tile.updateBitmap();
            }
        }
    }


    public void draw(Canvas c){
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                if (tiles[y][x] != null) {
                    if(tiles[y][x].getType() != 'P') {
                        tiles[y][x].draw(c);
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

                    }
                }
            }
        }
        return cords;
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
