package com.example.zonesshift.environments.mapmanagment;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.zonesshift.R;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.ui.TimerLevel;


public class Map {


    private Tile[][] tiles;
    private int tileSize;
    private int tileOffsetX, tileOffsetY;

    // for Timer
    private TimerLevel timer;
    private Typeface typeface;
    private Paint paint;
    private String timerText;

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

        typeface = ResourcesCompat.getFont(GamePanel.getGameContext(), R.font.minecraft);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(GamePanel.getGameContext(), R.color.time_text_color));
//        paint.setAlpha(150);
        paint.setTypeface(typeface);
        paint.setTextSize((float) GAME_WIDTH / 30);
    }


    public void updateBitmap(){
        for (Tile[] row : tiles){
            for (Tile tile : row){
                if(tile != null) tile.updateBitmap();
            }
        }
    }


    public void draw(Canvas c){
//        timerText = timer.tickTimer();

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
        if (timerText != null)
            c.drawText(timerText, (float) (GAME_WIDTH / 2) - paint.measureText(timerText) / 2, (float) GAME_HEIGHT / 10, paint);

    }

    private void drawTimer(String time){
    }

    public void addTimer(TimerLevel timer){
        this.timer = timer;
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


    public String getTime(){ return timerText;}

    public float getTileOffsetX() {
        return tileOffsetX;
    }

    public float getTileOffsetY() {
        return tileOffsetY;
    }


    public void update(double delta) {
        timerText = timer.tickTimer(delta);
    }
}
