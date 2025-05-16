package com.example.zonesshift.gamestates.createlvl;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.ADD;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.zonesshift.Game;
import com.example.zonesshift.R;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.helpers.interfaces.Verifications;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class AddLvlScreen extends BaseState implements GameStateInterface, BitmapMethods, Verifications {

    private CreatedLvlsList parent;

    private CustomButton btnBack;
    private CustomButton btnAdd;


    private EditText editText;
    private Typeface typeface;
    private Paint paint;

    public AddLvlScreen(Game game, CreatedLvlsList parent) {
        super(game);
        this.parent = parent;

        setPaintSettings();
        setEditTextSettings();
        initButtons();

    }

    private void setEditTextSettings(){
        Context context = GamePanel.getGameContext();

        editText = new EditText(context);
        editText.setTypeface(typeface);
        editText.setHint("New Map");
        editText.setText("New Map");
        editText.setBackgroundColor(R.color.text_color);
        editText.setX(GAME_WIDTH / 2 - GAME_WIDTH / 5); // позиция по X
        editText.setY((float) GAME_HEIGHT / 8);
        editText.setTextSize(27);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);




        if (context instanceof Activity) {

            System.out.println("ADDED EDITTEXT");
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


    }

    private void initButtons() {

        btnBack = new CustomButton((float) GAME_WIDTH / 20, (float) GAME_HEIGHT / 10, ButtonImages.PLAYING_TO_LVL.getWidth(), ButtonImages.PLAYING_TO_LVL.getHeight());
        btnAdd = new CustomButton(GAME_WIDTH / 2 - ADD.getWidth() / 2,
                GAME_HEIGHT / 2 - ADD.getHeight() / 2, ADD.getWidth(), ADD.getHeight());


    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {

        drawButtons(c);

    }

    private void drawButtons(Canvas c) {
        c.drawBitmap(
                ButtonImages.PLAYING_TO_LVL.getBtnImg(btnBack.isPushed()),
                btnBack.getHitbox().left,
                btnBack.getHitbox().top, null);
        c.drawBitmap(ADD.getBtnImg(btnAdd.isPushed()),
                btnAdd.getHitbox().left,
                btnAdd.getHitbox().top, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (btnBack.isIn(event))
                btnBack.setPushed(true);
            else if(btnAdd.isIn(event))
                btnAdd.setPushed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (btnBack.isIn(event))
                if (btnBack.isPushed()) {
                    ((ViewGroup) editText.getParent()).removeView(editText);
                    parent.setInAddLvl(false);
                }

            if (btnAdd.isIn(event))
                if (btnAdd.isPushed()) {
                    String name = editText.getText().toString().trim();
                    if (checkLvlName(name)){
                        if(createMap(GamePanel.getGameContext(), name)){
                            ((ViewGroup) editText.getParent()).removeView(editText);
                            parent.setInAddLvl(false);
                        } else {
                            Toast.makeText(GamePanel.getGameContext(), "Failed to create map.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            btnBack.setPushed(false);
            btnAdd.setPushed(false);
        }

    }

    private boolean createMap(Context context, String mapName) {
        String fileName = mapName + ".txt";
        File file = new File(context.getFilesDir(), fileName);

        if (file.exists())
            return false;

        String mapData = """
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................
                ....................""";

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(mapData.getBytes());

            Map<String, Boolean> maps = loadVerifications();
            maps.put(mapName, false);
            saveVerifications(maps);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean checkLvlName(String name) {

        if (name.length() >= 15) {
            Toast.makeText(GamePanel.getGameContext(), "Map name cannot be more than 15 letters long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!name.matches("[A-Za-z0-9 ]+")){
            Toast.makeText(GamePanel.getGameContext(), "Map name can only contain letters, numbers and spaces (A-Z, a-z, 0-9).", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            Map<String, Boolean> maps = loadVerifications();
            boolean mapWithSameName = false;

            for (Map.Entry<String, Boolean> map : maps.entrySet()){
                if (map.getKey().equals(name)){
                    mapWithSameName = true;
                    break;
                }
            }

            if (!mapWithSameName){
                return true;
            } else {
                Toast.makeText(GamePanel.getGameContext(), "There is already a map with this name.", Toast.LENGTH_SHORT).show();
                return false;
            }


        }


    }

}
