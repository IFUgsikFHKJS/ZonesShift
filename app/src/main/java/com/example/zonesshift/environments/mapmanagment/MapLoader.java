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

}
