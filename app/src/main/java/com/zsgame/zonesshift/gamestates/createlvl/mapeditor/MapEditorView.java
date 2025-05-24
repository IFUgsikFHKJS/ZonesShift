package com.zsgame.zonesshift.gamestates.createlvl.mapeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.zsgame.zonesshift.environments.mapmanagment.MapLoader;
import com.zsgame.zonesshift.environments.Tile;
import com.zsgame.zonesshift.environments.mapmanagment.Map;
import com.zsgame.zonesshift.helpers.interfaces.Verifications;
import com.zsgame.zonesshift.main.GamePanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MapEditorView extends View implements Verifications {

    private String mapName;
    private TileSimplified[][] map = new TileSimplified[20][20];
    private int mapWidth = 20;
    private int mapHeight = 20;
    private int mapMaxSize = 20;

    private char selectedZoneType = '1';
    private boolean hasPlayer = false;
    private float cellSize = 100f;

    private long touchDownTime;
    private static final long TAP_TIMEOUT_MS = 150; // 150 мс для определения "тапа"

    private Context context;


    private Paint paint = new Paint();

    // Масштабирование
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.0f;

    // Панорамирование
    private float offsetX = 0;
    private float offsetY = 0;
    private float lastTouchX;
    private float lastTouchY;
    private boolean isPanning = false;

    public MapEditorView(Context context, AttributeSet attrs, String mapName) {
        super(context, attrs);
        this.context = context;
        this.map = MapLoader.loadSimplifiedMapFromString(loadMapAsString(mapName));
        this.mapName = mapName;

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 1.7f));
                invalidate();
                return true;
            }
        });

        updateAllTextures();
    }

    private void updateAllTextures() {

        for (int y = 0; y <  map.length; y++){
            for (int x = 0; x < map[y].length; x++){
                if (map[y][x] != null){
                    map[y][x].setTextureId(map[y][x].determineTexture(map, y, x, map[y][x].getType()));
                    map[y][x].updateBitmap();
                }
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        long duration;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                touchDownTime = System.currentTimeMillis();
                isPanning = true;
                break;

            case MotionEvent.ACTION_MOVE:
                duration = System.currentTimeMillis() - touchDownTime;

                if (duration < TAP_TIMEOUT_MS){
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                }

                if (scaleDetector.isInProgress()) {
                    isPanning = false;
                } else if (isPanning && duration >= TAP_TIMEOUT_MS) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;

                    offsetX += dx;
                    offsetY += dy;

                    lastTouchX = event.getX();
                    lastTouchY = event.getY();

                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                duration = System.currentTimeMillis() - touchDownTime;

                if (duration < TAP_TIMEOUT_MS && !scaleDetector.isInProgress()) {
                    handleTileTouch(event.getX(), event.getY());
                }

                isPanning = false;
                break;

            case MotionEvent.ACTION_CANCEL:
                isPanning = false;
                break;
        }

        return true;
    }



    private void handleTileTouch(float screenX, float screenY) {
        float adjustedX = (screenX - offsetX) / scaleFactor;
        float adjustedY = (screenY - offsetY) / scaleFactor;

        int tileX = (int) (adjustedX / cellSize);
        int tileY = (int) (adjustedY / cellSize);

        if (tileX < 0 || tileY < 0) return;

        int newWidth = mapWidth;
        int newHeight = mapHeight;

        if (tileX >= mapWidth && tileX < mapMaxSize) newWidth = tileX + 1;
        if (tileY >= mapHeight && tileY < mapMaxSize) newHeight = tileY + 1;

        if (newWidth != mapWidth || newHeight != mapHeight) {
            resizeMap(newWidth, newHeight);
        }

        TileSimplified tile = new TileSimplified(selectedZoneType);

        if (tileX < mapMaxSize && tileY < mapMaxSize){
            if (selectedZoneType == '0')
                tile = null;
            else {
                tile.setTextureId(tile.determineTexture(map, tileY, tileX, selectedZoneType));
                tile.updateBitmap();
            }

            if (selectedZoneType == 'P'){
                if (!hasPlayer){
                    map[tileY][tileX] = tile;
                    hasPlayer = true;
                }
            } else {
                if (map[tileY][tileX] != null && map[tileY][tileX].getType() == 'P')
                    hasPlayer = false;
                map[tileY][tileX] = tile;
            }

            updateTextures(tileX, tileY);
//            TileSimplified[][] crop = cropMap(map);
//            for (TileSimplified[] y : crop){
//                for (TileSimplified x : y){
//                    if (x == null)
//                        System.out.print(".");
//                    else
//                        System.out.print(x.getType());
//                }
//                System.out.println();
//            }
        }
        


        invalidate();
    }

    private void updateTextures(int x, int y) {

        boolean up = y > 0 && map[y - 1][x] != null;
        boolean down = y < map.length - 1 && map[y + 1][x] != null;
        boolean left = x > 0 && map[y][x - 1] != null;
        boolean right = x < map[0].length - 1 && map[y][x + 1] != null;

        if (up){
            map[y - 1][x].setTextureId(map[y - 1][x].determineTexture(map, y-1, x, map[y - 1][x].getType()));
            map[y - 1][x].updateBitmap();
        }
        if (down){
            map[y + 1][x].setTextureId(map[y + 1][x].determineTexture(map, y+1, x, map[y + 1][x].getType()));
            map[y + 1][x].updateBitmap();
        }
        if (left){
            map[y][x - 1].setTextureId(map[y][x - 1].determineTexture(map, y, x-1, map[y][x - 1].getType()));
            map[y][x - 1].updateBitmap();
        }
        if (right){
            map[y][x + 1].setTextureId(map[y][x + 1].determineTexture(map, y, x+1, map[y][x + 1].getType()));
            map[y][x + 1].updateBitmap();
        }



    }

    public void resizeMap(int newWidth, int newHeight) {
        TileSimplified[][] newMap = new TileSimplified[newHeight][newWidth];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                newMap[y][x] = map[y][x];
            }
        }

        map = newMap;
        mapWidth = newWidth;
        mapHeight = newHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.scale(scaleFactor, scaleFactor);

        Bitmap player = null;
        float playerL = 0, playerT = 0, playerR = 0, playerB = 0;

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                float left = x * cellSize;
                float top = y * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;

                TileSimplified tile = map[y][x];

                paint.setColor(Color.LTGRAY);
                canvas.drawRect(left, top, right, bottom, paint);

                if (tile == null || tile.getBitmap() == null) {

                } else {
                    if (tile.getType() == 'P'){
                        player = tile.getBitmap();
                        playerL = left; playerR = right; playerB = bottom + bottom - top; playerT = top;

                    }
                    else
                        canvas.drawBitmap(tile.getBitmap(), null, new RectF(left, top, right, bottom), null);
                }

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas.drawRect(left, top, right , bottom, paint);
                paint.setStyle(Paint.Style.FILL);
            }
        }

        if (player != null) {
            float playerWidth = playerR - playerL;
            float playerHeight = playerB - playerT;
            canvas.drawBitmap(player, null, new RectF(playerL - playerWidth / 5, playerT, playerR + playerWidth / 5, playerB), null);
        }


        canvas.restore();
    }

    public void setSelectedBlockType(char type) {
        selectedZoneType =  type;
    }

    public void resetView() {
        scaleFactor = 1f;
        offsetX = 0;
        offsetY = 0;
        invalidate();
    }

    public static TileSimplified[][] cropMap(TileSimplified[][] map) {
        int top = map.length, bottom = -1, left = map[0].length, right = -1;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] != null) {
                    if (y < top) top = y;
                    if (y > bottom) bottom = y;
                    if (x < left) left = x;
                    if (x > right) right = x;
                }
            }
        }

        if (bottom == -1) return new TileSimplified[1][1]; // карта полностью пуста

        TileSimplified[][] cropped = new TileSimplified[bottom - top + 1][right - left + 1];
        for (int y = top; y <= bottom; y++) {
            for (int x = left; x <= right; x++) {
                cropped[y - top][x - left] = map[y][x];
            }
        }

        return cropped;
    }

    private static String mapToString(TileSimplified[][] map) {
        StringBuilder sb = new StringBuilder();
        for (TileSimplified[] row : map) {
            for (TileSimplified tile : row) {
                if (tile == null)
                    sb.append('.');
                else
                    sb.append(tile.getType());
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public static String getMapString(String name){
        TileSimplified[][] mapSimplified =  MapLoader.loadSimplifiedMapFromString(loadMapAsString(name));
        return mapToString(cropMap(mapSimplified));
    }

    public Map getMap(boolean isCropped) {

//        String mp = mapToString(map);
//        saveMapToFile(GamePanel.getGameContext(), "Testmap3", mp);

        Tile[][] newMap;

        if (isCropped){
            String mapString = mapToString(cropMap(map));
            newMap = MapLoader.loadMapFromString(mapString);
        } else {
            newMap = MapLoader.loadMapFromString(mapToString(map));
        }

        if (!loadMapAsString(mapName).equals(mapToString(map)) ){
            java.util.Map<String, Boolean> maps = loadVerifications();
            maps.put(mapName, false);
            saveVerifications(maps);
        }
        saveMapToFile(context, mapName, mapToString(map));



        return new Map(newMap);



//        Intent i = new Intent(context, MainActivity.class);
//        context.startActivity(i);



    }



    public boolean saveMapToFile(Context context, String mapName, String mapData) {
        String fileName = mapName + ".txt";
        File file = new File(context.getFilesDir(), fileName);


        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(mapData.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loadMapAsString(String mapName) {
        File file = new File(GamePanel.getGameContext().getFilesDir(), mapName + ".txt");

        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
