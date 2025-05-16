package com.example.zonesshift.environments.mapmanagment;



import android.content.Context;
import android.graphics.PointF;

import com.example.zonesshift.authentication.UserInfo;
import com.example.zonesshift.entities.GameCharacters;
import com.example.zonesshift.entities.Player;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.ui.TimerLevel;
import com.example.zonesshift.userresults.AddBestTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapManager {
    private String[] mapNames = {"map1.txt", "map2.txt", "map3.txt", "map4.txt", "map5.txt", "map6.txt", "map7.txt"};
    private int currentMapId;
    private static ArrayList<Map> maps = new ArrayList<Map>();
    private static Map currentMap;
    private static boolean isOnline = false;
    private static Player player;
    private static int userId;
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
//            if (mapName.equals("map7.txt")){
//                AddMap.addMap(tiles, "Level 07", 0, "00:30:00", "00:35:00", "00:45:00");
//            }
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

    public int getCurrentMapId() {
        return currentMapId;
    }

    public void setCurrentMap(int currentMapId) {
        isOnline = false;
        this.currentMapId = currentMapId;
        currentMap = maps.get(currentMapId);
        float[] cords = currentMap.getPlayerCords();
        player = new Player(new PointF( cords[0], cords[1]));
        GameCharacters.PLAYER.setPlayerBitmap(currentMap.getTileSize());
        TimerLevel timer = new TimerLevel();
        timer.startTimer();
        currentMap.addTimer(timer);
        currentMap.updateBitmap();
//        Playing.setPlayerCords(maps.get(currentMap).getPlayerCords(maps.get(currentMap).getTiles()));
    }

    public void setCurrentOnlineMap(Map map, int id){
        isOnline = true;
        currentMap = map;
        currentMapId = id;
        float[] cords = currentMap.getPlayerCords();
        player = new Player(new PointF( cords[0], cords[1]));
        GameCharacters.PLAYER.setPlayerBitmap(currentMap.getTileSize());
        TimerLevel timer = new TimerLevel();
        timer.startTimer();
        currentMap.addTimer(timer);
        currentMap.updateBitmap();
    }


    public void restartCurrentMap(){
        float[] cords = currentMap.getPlayerCords();
        player = new Player(new PointF( cords[0], cords[1]));
        GameCharacters.PLAYER.setPlayerBitmap(currentMap.getTileSize());
        TimerLevel timer = new TimerLevel();
        timer.startTimer();
        currentMap.addTimer(timer);
    }

    public void nextMap(){
        this.setCurrentMap(++currentMapId);
    }

    public Player getPlayer() {
        return player;
    }

    public void win() {
            String time = getCurrentMap().getTime();

            if (currentMapId == -1){

            } else {
                UserInfo.getUserId(new UserInfo.UserIdCallback() {
                    @Override
                    public void onUserIdReceived(int userId) {
                        if (!isOnline)
                            AddBestTime.saveBestTime(userId, getCurrentMapId() + 1, time);
                        else
                            AddBestTime.saveBestTime(userId, getCurrentMapId(), time);
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error: " + error);
                    }
                });
            }


    }

    public static void setUserId(int userId) {
        MapManager.userId = userId;
    }

    public void updateMap(double delta) {
        currentMap.update(delta);
    }

    public void setCurrentMapId(int currentMapId) {
        this.currentMapId = currentMapId;
    }
}
