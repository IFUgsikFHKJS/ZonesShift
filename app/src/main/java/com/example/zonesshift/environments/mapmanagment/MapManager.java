package com.example.zonesshift.environments.mapmanagment;


import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.zonesshift.entities.GameCharacters;
import com.example.zonesshift.entities.Player;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.environments.Blocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapManager {
    private String[] mapNames = {"map1.txt", "map2.txt", "map3.txt", "map4.txt", "map5.txt", "map6.txt", "map7.txt"};
    private static int currentMapId;
    private static ArrayList<Map> maps = new ArrayList<Map>();
    private static Map currentMap;
    private static Player player;
//    MapLoader.loadMap(MainActivity.getGameContext(), "maps/map1.txt", 9, 19)

    public MapManager(){
        for (String mapName : mapNames){
            int y;
            int x;
            try {
                y = countLines(MainActivity.getGameContext(),"maps/" + mapName);
                x = countRows(MainActivity.getGameContext(),"maps/" + mapName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tile[][] tiles = MapLoader.loadMap(MainActivity.getGameContext(), "maps/" + mapName, y, x);
//            for (Tile[] row : tiles) {
//                for (Tile tile : row) {
//                    System.out.println(tile);
//                }
//            }
            maps.add(new Map(tiles));
        }
    }





    private int countLines(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    private int countRows(Context context,String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        reader.close();
        return line.length();
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int currentMapId) {
        MapManager.currentMapId = currentMapId;
        currentMap = maps.get(currentMapId);
        float[] cords = currentMap.getPlayerCords();
        player = new Player(new PointF( cords[0], cords[1]));
        GameCharacters.PLAYER.setPlayerBitmap(currentMap.getTileSize());

        currentMap.updateBitmap();
//        Playing.setPlayerCords(maps.get(currentMap).getPlayerCords(maps.get(currentMap).getTiles()));
    }


    public void restartCurrentMap(){
        float[] cords = currentMap.getPlayerCords();
        player = new Player(new PointF( cords[0], cords[1]));
        GameCharacters.PLAYER.setPlayerBitmap(currentMap.getTileSize());
    }

    public void nextMap(){
        this.setCurrentMap(++currentMapId);
    }

    public Player getPlayer() {
        return player;
    }
}
