//package com.example.zonesshift.environments;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.view.View;
//
//public class MapManager {
//
//    public class MapEditorView extends View {
//        int[][] map;
//        int tileSize = 64; // Each tile is 64x64 pixels
//        int rows, cols; // Number of rows and columns in the map
//        int currentTileType = 1; // The current tile being placed (1 = ground, 2 = platform, etc.)
//
//        public MapEditorView(Context context) {
//            super(context);
//            cols = getWidth() / tileSize;
//            rows = getHeight() / tileSize;
//            map = new int[rows][cols]; // Empty map
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            drawGrid(canvas);
//            drawTiles(canvas);
//        }
//
//        public void drawGrid(Canvas canvas) {
//            Paint gridPaint = new Paint();
//            gridPaint.setColor(Color.GRAY);
//            gridPaint.setStyle(Paint.Style.STROKE);
//
//            for (int row = 0; row < rows; row++) {
//                for (int col = 0; col < cols; col++) {
//                    canvas.drawRect(col * tileSize, row * tileSize,
//                            (col + 1) * tileSize, (row + 1) * tileSize, gridPaint);
//                }
//            }
//        }
//
//        public void drawTiles(Canvas canvas) {
//            Paint tilePaint = new Paint();
//            for (int row = 0; row < rows; row++) {
//                for (int col = 0; col < cols; col++) {
//                    if (map[row][col] != 0) { // If not empty, draw the tile
//                        tilePaint.setColor(getTileColor(map[row][col])); // Choose color based on type
//                        canvas.drawRect(col * tileSize, row * tileSize,
//                                (col + 1) * tileSize, (row + 1) * tileSize, tilePaint);
//                    }
//                }
//            }
//        }
//
//        private int getTileColor(int tileType) {
//            switch (tileType) {
//                case 1: return Color.GREEN; // Ground
//                case 2: return Color.BLUE; // Platform
//                case 3: return Color.YELLOW; // Special tile
//                default: return Color.TRANSPARENT;
//            }
//        }
//    }
//
//}
