package com.example.zonesshift.gamestates.createlvl;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.HOME;
import static com.example.zonesshift.ui.ButtonImages.LVL_ITEM;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.zonesshift.Game;
import com.example.zonesshift.R;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.gamestates.createlvl.mapeditor.MapEditorActivity;
import com.example.zonesshift.gamestates.createlvl.mapeditor.TileSimplified;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.CustomButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreatedLvlsList extends BaseState implements GameStateInterface, BitmapMethods {

    private Map<String, Boolean> maps;
    private int page;

    private Typeface typeface;
    private Paint paint;
    private float textHeight;

    private CustomButton btnHome;
    private CustomButton[] btnLvl;


    public CreatedLvlsList(Game game) {
        super(game);

        setPaintSettings();


        maps = loadVerifications();
        page = maps.size() / 4;
        btnLvl  = new CustomButton[maps.size()];

        initButtons();


        System.out.println(maps);
    }

    private void initButtons() {

        btnHome = new CustomButton(GAME_WIDTH / 26, GAME_WIDTH / 26, HOME.getWidth(), HOME.getHeight());

        initLvlItems();

    }

    private void initLvlItems() {

        float x = (float) GAME_WIDTH / 2 - (float) GAME_WIDTH / 5;
        float defaultY = (float) GAME_HEIGHT / 8;

        int index = 0;
        for (Map.Entry<String, Boolean> map : maps.entrySet()){
            if (index >= page * 3 && index < page * 3 + 3){
                float itemY = defaultY +  (float) (index % 3 * GAME_HEIGHT) / 4;

                btnLvl[index] = new CustomButton(x, itemY, LVL_ITEM.getWidth(), LVL_ITEM.getHeight());




            }

            index++;
        }

    }

    private void setPaintSettings() {
        typeface = ResourcesCompat.getFont(GamePanel.getGameContext(), R.font.minecraft);

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(MainActivity.getGameContext(), R.color.text_color));
        paint.setAlpha(200);
        paint.setTypeface(typeface);
        paint.setTextSize((float) GAME_WIDTH / 30);

        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = fm.bottom - fm.top + fm.leading;

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {

//        c.drawText("LOLOLO", 50, 50, paint);

        drawLvls(c);
        drawButtons(c);



    }

    private void drawButtons(Canvas c) {

        c.drawBitmap(HOME.getBtnImg(btnHome.isPushed()),
                btnHome.getHitbox().left,
                btnHome.getHitbox().top, null);

        drawLvls(c);

    }

    private void drawLvls(Canvas c) {

        for (CustomButton customButton : btnLvl) {
            c.drawBitmap(
                    LVL_ITEM.getBtnImg(customButton.isPushed()),
                    customButton.getHitbox().left,
                    customButton.getHitbox().top, null);
        }

        int index = 0;
        for (Map.Entry<String, Boolean> map : maps.entrySet()){
            if (index >= page * 3 && index < page * 3 + 3){

                float x = btnLvl[index].getHitbox().left;
                float itemY = btnLvl[index].getHitbox().top;


                float textX = (float) (x + ((GAME_WIDTH / 2.5) - paint.measureText(map.getKey())) / 2);
                float textY = itemY + (((float) GAME_HEIGHT / 5) - textHeight);
                c.drawText(map.getKey(), textX, textY, paint);

            }

            index++;
        }



    }

    private void drawItem(Canvas c, float x, float y) {
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.item_lvl, options);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 42, 18);
        bitmap = getScaledBitmapButton(bitmap, (int) (GAME_WIDTH / 2.5), GAME_HEIGHT / 5);
        c.drawBitmap(bitmap, x, y, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            for (CustomButton customButton : btnLvl) {
                if (customButton.isIn(event)) {
                    customButton.setPushed(true);
                    break;
                }
            }


            if(btnHome.isIn(event))
                btnHome.setPushed(true);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            for (int i = 0; i < btnLvl.length; i++){
                if (btnLvl[i].isIn(event))
                    if (btnLvl[i].isPushed()) {

                        selectLvl(i);

                        btnLvl[i].setPushed(false);
                        break;
                    }
            }


            if (btnHome.isIn(event))
                if (btnHome.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.START_MENU);

            for (CustomButton button : btnLvl){
                button.setPushed(false);
            }

            btnHome.setPushed(false);
        }

    }

    private void selectLvl(int i) {

        int index = 0;

        for (Map.Entry<String, Boolean> map : maps.entrySet()){
            if (index == i){

                String mapString = loadMapAsString(map.getKey());

                System.out.println(mapString);

                Intent intent = new Intent(GamePanel.getGameContext(), MapEditorActivity.class);
                intent.putExtra("name", map.getKey());
                intent.putExtra("map", mapString);

                GamePanel.getGameContext().startActivity(intent);

            }

            index++;
        }

    }


    public String loadMapAsString(String mapName) {
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

    private boolean saveMapAsString(String mapName, String mapData) {
        File file = new File(GamePanel.getGameContext().getFilesDir(), mapName + ".txt");

        if (file.exists()) return false;

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(mapData.getBytes());
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map<String, Boolean> loadVerifications() {
        File file = new File(GamePanel.getGameContext().getFilesDir(), "verifications.json");

        if (!file.exists()) return new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            JSONObject obj = new JSONObject(json.toString());
            Map<String, Boolean> result = new HashMap<>();
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                result.put(key, obj.getBoolean(key));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveVerifications(Map<String, Boolean> map) {
        JSONObject obj = new JSONObject(map);
        File file = new File(GamePanel.getGameContext().getFilesDir(), "verifications.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
