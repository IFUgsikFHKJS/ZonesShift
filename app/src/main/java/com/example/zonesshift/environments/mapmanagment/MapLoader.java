package com.example.zonesshift.environments.mapmanagment;

import android.content.Context;

import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.gamestates.Playing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapLoader {
    public static Tile[][] loadMap(Context context, String filename, int rows, int cols) {
        Tile[][] tiles = new Tile[rows][cols];
        try {
            InputStream inputStream = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {

                for (int col = 0; col < line.length(); col++) {
                    char c = line.charAt(col);
                    if (c != '.') {
                        tiles[row][col] = new Tile(col, row, 32, c);
                    }
                }
                row++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tiles;
    }

    public static Tile[][] loadMapFromString(String mapData) {
        String[] lines = mapData.split("\n");
        int rows = lines.length;
        int cols = 0;

        // Находим максимальную длину строки (на случай, если строки не одинаковой длины)
        for (String line : lines) {
            if (line.length() > cols) {
                cols = line.length();
            }
        }

        Tile[][] tiles = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            String line = lines[row];
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                if (c != '.') {
                    tiles[row][col] = new Tile(col, row, 32, c);
                }
            }
        }

        return tiles;
    }

}
