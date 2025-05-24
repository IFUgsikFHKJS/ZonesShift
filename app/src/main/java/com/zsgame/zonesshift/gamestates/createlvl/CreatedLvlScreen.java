package com.zsgame.zonesshift.gamestates.createlvl;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.service.quicksettings.Tile;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.authentication.UserInfo;
import com.zsgame.zonesshift.gamestates.BaseState;
import com.zsgame.zonesshift.gamestates.createlvl.mapeditor.MapEditorActivity;
import com.zsgame.zonesshift.gamestates.createlvl.mapeditor.MapEditorView;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.helpers.interfaces.Verifications;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.main.MainActivity;
import com.zsgame.zonesshift.ui.ButtonImages;
import com.zsgame.zonesshift.ui.CustomButton;
import com.zsgame.zonesshift.environments.mapmanagment.mapcreating.AddMap;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class CreatedLvlScreen extends BaseState implements GameStateInterface, BitmapMethods, Verifications {

    private String name;
    private boolean isVerified;

    private EditText editText;
    private CustomButton btnBack;
    private CustomButton btnPlay;
    private CustomButton btnDelete;
    private CustomButton btnVerify;
    private CustomButton btnShare;

    private Typeface typeface;
    private Paint paint;

    public CreatedLvlsList parent;
    private DeleteLvlScreen deleteScreen;
    private boolean inDeleteScreen = false;
    

    public CreatedLvlScreen(Game game, String name, boolean isVerified, CreatedLvlsList parent) {
        super(game);
        this.name = name;
        this.isVerified = isVerified;
        this.parent = parent;

        setPaintSettings();
        setEditTextSettings();
        initButtons();

    }

    private void initButtons() {

        float btnPlayX = (float) GAME_WIDTH / 2 - (float) ButtonImages.PLAY.getWidth() / 2;
        float btnPlayY = (float) GAME_HEIGHT / 2;

        btnBack = new CustomButton((float) GAME_WIDTH / 20, (float) GAME_HEIGHT / 10, ButtonImages.PLAYING_TO_LVL.getWidth(), ButtonImages.PLAYING_TO_LVL.getHeight());
        btnPlay = new CustomButton(btnPlayX, btnPlayY,
                ButtonImages.PLAY.getWidth(), ButtonImages.PLAY.getHeight());
        btnDelete = new CustomButton(btnPlayX + (float) (ButtonImages.PLAY.getWidth() * 1.5),
                btnPlayY + ((ButtonImages.PLAY.getHeight() - ButtonImages.DELETE.getHeight()) / 2),
                ButtonImages.DELETE.getWidth(),  ButtonImages.DELETE.getHeight());
        btnShare = new CustomButton(btnPlayX - (float) (ButtonImages.PLAY.getWidth() * 0.5) - ButtonImages.SHARE.getWidth(),
                btnPlayY + ((ButtonImages.PLAY.getHeight() - ButtonImages.DELETE.getHeight()) / 2),
                ButtonImages.DELETE.getWidth(),  ButtonImages.DELETE.getHeight());
        btnVerify = new CustomButton(GAME_WIDTH - ButtonImages.VERIFY.getWidth() - GAME_WIDTH / 20,
                GAME_HEIGHT / 10, ButtonImages.SHARE.getWidth(), ButtonImages.SHARE.getHeight());
        
    }

    private void setEditTextSettings(){
        Context context = GamePanel.getGameContext();

        if (editText != null && editText.getParent() != null) {
            ((ViewGroup) editText.getParent()).removeView(editText);
        }

        editText = new EditText(context);
        System.out.println(context);
        editText.setTypeface(typeface);
        editText.setHint(name);
        editText.setText(name);
        editText.setBackgroundColor(R.color.text_color);
        editText.setX(GAME_WIDTH / 2 - GAME_WIDTH / 5); // позиция по X
        editText.setY((float) GAME_HEIGHT / 8);
        editText.setTextSize(27);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);





        if (context instanceof Activity) {

            ((Activity) context).addContentView(editText,
                    new ViewGroup.LayoutParams((int) (GAME_WIDTH / 2.5), GAME_HEIGHT / 6));
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

    }

    @Override
    public void update(double delta) {
        isVerified = Boolean.TRUE.equals(loadVerifications().get(name));

    }

    @Override
    public void render(Canvas c) {

        if (inDeleteScreen)
            deleteScreen.render(c);
        else
            drawButtons(c);

    }

    private void drawButtons(Canvas c) {

        c.drawBitmap(
                ButtonImages.PLAYING_TO_LVL.getBtnImg(btnBack.isPushed()),
                btnBack.getHitbox().left,
                btnBack.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.PLAY.getBtnImg(btnPlay.isPushed()),
                btnPlay.getHitbox().left,
                btnPlay.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.DELETE.getBtnImg(btnDelete.isPushed()),
                btnDelete.getHitbox().left,
                btnDelete.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.SHARE.getBtnImg(btnShare.isPushed()),
                btnShare.getHitbox().left,
                btnShare.getHitbox().top, null);

        if (!isVerified){
            c.drawBitmap(
                    ButtonImages.VERIFY.getBtnImg(btnVerify.isPushed()),
                    btnVerify.getHitbox().left,
                    btnVerify.getHitbox().top, null);
        }



    }

    @Override
    public void touchEvents(MotionEvent event) {


        if (inDeleteScreen)
            deleteScreen.touchEvents(event);
        else {
            if (editText.hasFocus())
                changeLvlName();


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (btnBack.isIn(event))
                    btnBack.setPushed(true);
                else if (btnPlay.isIn(event))
                    btnPlay.setPushed(true);
                else if (btnDelete.isIn(event))
                    btnDelete.setPushed(true);
                else if (btnShare.isIn(event))
                    btnShare.setPushed(true);
                else if(btnVerify.isIn(event))
                    btnVerify.setPushed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (btnBack.isIn(event))
                    if (btnBack.isPushed()) {
                        removeEditText();
                        parent.setInLvlScreen(false);
                    }
                if (btnPlay.isIn(event))
                    if (btnPlay.isPushed()) {
                        testLvl();
                    }
                if (btnDelete.isIn(event))
                    if (btnDelete.isPushed()) {
                        removeEditText();
                        deleteScreen = new DeleteLvlScreen(game, name, this);
                        inDeleteScreen = true;
                    }
                if (btnShare.isIn(event))
                    if (btnShare.isPushed()) {
                        shareMap();
                    }
                if (!isVerified){
                    if (btnVerify.isIn(event))
                        if (btnVerify.isPushed()) {
                            Toast.makeText(GamePanel.getGameContext(), "You must complete the level to verify it.", Toast.LENGTH_SHORT).show();
                        }
                }

                btnBack.setPushed(false);
                btnPlay.setPushed(false);
                btnDelete.setPushed(false);
                btnShare.setPushed(false);
                btnVerify.setPushed(false);
            }
        }

    }

    private void shareMap() {

        if (!isVerified)
            Toast.makeText(GamePanel.getGameContext(), "You must verify the level to share it.", Toast.LENGTH_SHORT).show();
        else {
            UserInfo.getUserId(new UserInfo.UserIdCallback() {
                @Override
                public void onUserIdReceived(int userId) {
                    System.out.println("User ID: " + userId);
                    String map = MapEditorView.getMapString(name);
                    System.out.println(map);

                    AddMap.addMap(map, name, userId, "00:00:00","00:00:00","00:00:00");
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error: " + error);
                }
            });
        }


    }





    public void removeEditText() {
        ((ViewGroup) editText.getParent()).removeView(editText);
    }

    private void testLvl() {

        Map<String, Boolean> maps = loadVerifications();

        for (Map.Entry<String, Boolean> map : maps.entrySet()){
            if (name.equals(map.getKey())){


                String mapString = loadMapAsString(map.getKey());

                System.out.println(mapString);

                Intent intent = new Intent(GamePanel.getGameContext(), MapEditorActivity.class);
                intent.putExtra("name", map.getKey());
                intent.putExtra("map", mapString);

                GamePanel.getGameContext().startActivity(intent);

            }

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

    public Tile[][] cropMap(Tile[][] map) {
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

        if (bottom == -1) return new Tile[1][1]; // карта полностью пуста

        Tile[][] cropped = new Tile[bottom - top + 1][right - left + 1];
        for (int y = top; y <= bottom; y++) {
            for (int x = left; x <= right; x++) {
                cropped[y - top][x - left] = map[y][x];
            }
        }

        return cropped;
    }

    private void changeLvlName() {
        String newName = editText.getText().toString().trim();

        if (newName.length() >= 15) {
            Toast.makeText(GamePanel.getGameContext(), "Map name cannot be more than 15 letters long", Toast.LENGTH_SHORT).show();
            editText.setText(name);
        } else if (!newName.matches("[A-Za-z0-9 ]+")){
            Toast.makeText(GamePanel.getGameContext(), "Map name can only contain letters, numbers and spaces (A-Z, a-z, 0-9).", Toast.LENGTH_SHORT).show();
            editText.setText(name);
        } else {

            Map<String, Boolean> maps = loadVerifications();
            boolean mapWithSameName = false;

            for (Map.Entry<String, Boolean> map : maps.entrySet()){
                if (map.getKey().equals(newName)){
                    mapWithSameName = true;
                    break;
                }
            }

            if (!mapWithSameName){
                maps.remove(name);
                renameMap(newName);
                name = newName;
                maps.put(name, isVerified);
                saveVerifications(maps);
                editText.setText(name);
            } else {
                Toast.makeText(GamePanel.getGameContext(), "There is already a map with this name.", Toast.LENGTH_SHORT).show();
                editText.setText(name);
            }


        }


        editText.clearFocus();

    }

    private void renameMap(String newName) {

        File oldFile = new File(GamePanel.getGameContext().getFilesDir(), name + ".txt");
        System.out.println(name);
        System.out.println(newName);
        File newFile = new File(GamePanel.getGameContext().getFilesDir(), newName + ".txt");

        System.out.println(oldFile.exists());

        if (oldFile.exists()) {
            boolean renamed = oldFile.renameTo(newFile);
            if (renamed) {
                System.out.println("Файл переименован.");
            } else {
                System.out.println("Ошибка при переименовании.");
            }
        }

    }




    public void setInDeleteScreen(boolean inDeleteScreen) {
        this.inDeleteScreen = inDeleteScreen;
        setEditTextSettings();
    }
}
