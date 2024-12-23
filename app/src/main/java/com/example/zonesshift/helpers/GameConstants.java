package com.example.zonesshift.helpers;

import com.example.zonesshift.main.MainActivity;

public class GameConstants {
    public static class GameSize{
        public static final int GAME_WIDTH = MainActivity.GAME_WIDTH;
        public static final int GAME_HEIGHT = MainActivity.GAME_HEIGHT;
        public static final int GAME_WIDTH_RES = MainActivity.GAME_WIDTH_RES;
        public static final int GAME_HEIGHT_RES = MainActivity.GAME_HEIGHT_RES;
    }
    public static final int TILE_WIDTH = GameSize.GAME_WIDTH / GameSize.GAME_WIDTH_RES;
    public static final int TILE_HEIGHT = GameSize.GAME_HEIGHT / GameSize.GAME_HEIGHT_RES;
}
