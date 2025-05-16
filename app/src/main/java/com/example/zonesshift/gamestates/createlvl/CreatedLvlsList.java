package com.example.zonesshift.gamestates.createlvl;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.ADD;
import static com.example.zonesshift.ui.ButtonImages.HOME;
import static com.example.zonesshift.ui.ButtonImages.LVL_ITEM;
import static com.example.zonesshift.ui.ButtonImages.NEXT_LVL;
import static com.example.zonesshift.ui.ButtonImages.PREV_LVL;

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
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.helpers.interfaces.Verifications;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.CustomButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class CreatedLvlsList extends BaseState implements GameStateInterface, BitmapMethods, Verifications {

    private Map<String, Boolean> maps;
    private int page = 0;
    private int maxPage;

    private Typeface typeface;
    private Paint paint;
    private float textHeight;

    private CreatedLvlScreen lvlScreen;
    private boolean inLvlScreen = false;
    private AddLvlScreen addLvl;
    private boolean inAddLvl = false;

    private CustomButton btnHome;
    private CustomButton[] btnLvl;

    private CustomButton btnNext;
    private CustomButton btnPrev;
    private CustomButton btnAdd;


    public CreatedLvlsList(Game game) {
        super(game);

        setPaintSettings();


        maps = loadVerifications();
        maxPage = maps.size() / 4;
        btnLvl  = new CustomButton[maps.size()];

        initButtons();


    }

    private void initButtons() {

        btnHome = new CustomButton(GAME_WIDTH / 26, GAME_HEIGHT / 13, HOME.getWidth(), HOME.getHeight());

        btnNext = new CustomButton( GAME_WIDTH - GAME_WIDTH / 20 - NEXT_LVL.getWidth(), GAME_HEIGHT / 2 - NEXT_LVL.getHeight() / 2, NEXT_LVL.getWidth(), NEXT_LVL.getHeight() );
        btnPrev = new CustomButton( GAME_WIDTH / 20, GAME_HEIGHT / 2 - PREV_LVL.getHeight() / 2, PREV_LVL.getWidth(), PREV_LVL.getHeight() );
        btnAdd = new CustomButton(GAME_WIDTH - GAME_WIDTH / 20 - ADD.getWidth(), GAME_HEIGHT - GAME_HEIGHT / 13 - ADD.getHeight(), ADD.getWidth(), ADD.getHeight());

        initLvlItems();

    }

    private void initLvlItems() {

        maps = loadVerifications();
        btnLvl  = new CustomButton[maps.size()];


        float x = (float) GAME_WIDTH / 2 - (float) GAME_WIDTH / 5;
        float defaultY = (float) GAME_HEIGHT / 8;

        int index = 0;
        for (Map.Entry<String, Boolean> map : maps.entrySet()){

                float itemY = defaultY +  (float) (index % 3 * GAME_HEIGHT) / 4;

                btnLvl[index] = new CustomButton(x, itemY, LVL_ITEM.getWidth(), LVL_ITEM.getHeight());


            index++;
        }

    }

    private void setPaintSettings() {
        typeface = ResourcesCompat.getFont(MainActivity.getGameContext(), R.font.minecraft);

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

        if (inLvlScreen)
            lvlScreen.update(delta);

    }

    @Override
    public void render(Canvas c) {

//        c.drawText("LOLOLO", 50, 50, paint);

        if (inLvlScreen)
            lvlScreen.render(c);
        else if(inAddLvl)
            addLvl.render(c);
        else {
            drawLvls(c);
            drawButtons(c);
        }




    }

    private void drawButtons(Canvas c) {

        c.drawBitmap(HOME.getBtnImg(btnHome.isPushed()),
                btnHome.getHitbox().left,
                btnHome.getHitbox().top, null);

        if (maxPage != 0){
            c.drawBitmap(NEXT_LVL.getBtnImg(btnNext.isPushed()),
                    btnNext.getHitbox().left,
                    btnNext.getHitbox().top, null);

            c.drawBitmap(PREV_LVL.getBtnImg(btnPrev.isPushed()),
                    btnPrev.getHitbox().left,
                    btnPrev.getHitbox().top, null);
        }



        c.drawBitmap(ADD.getBtnImg(btnAdd.isPushed()),
                btnAdd.getHitbox().left,
                btnAdd.getHitbox().top, null);

        drawLvls(c);

    }

    private void drawLvls(Canvas c) {

        for (int i = 0; i < btnLvl.length; i++) {
            if (i >= page * 3 && i < page * 3 + 3) {
                c.drawBitmap(
                        LVL_ITEM.getBtnImg(btnLvl[i].isPushed()),
                        btnLvl[i].getHitbox().left,
                        btnLvl[i].getHitbox().top, null);
            }
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

        if (inLvlScreen)
            lvlScreen.touchEvents(event);
        else if (inAddLvl)
            addLvl.touchEvents(event);
        else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                for (int i = 0; i < btnLvl.length; i++) {
                    if (i >= page * 3 && i < page * 3 + 3) {
                        if (btnLvl[i].isIn(event)) {
                            btnLvl[i].setPushed(true);
                            break;
                        }
                    }

                }


                if (btnHome.isIn(event))
                    btnHome.setPushed(true);

                else if (btnNext.isIn(event)) {
                    btnNext.setPushed(true);
                } else if (btnPrev.isIn(event)) {
                    btnPrev.setPushed(true);
                } else if (btnAdd.isIn(event)) {
                    btnAdd.setPushed(true);
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                for (int i = 0; i < btnLvl.length; i++) {
                    if (i >= page * 3 && i < page * 3 + 3) {
                        if (btnLvl[i].isIn(event))
                            if (btnLvl[i].isPushed()) {

                                selectLvl(i);

                                btnLvl[i].setPushed(false);
                                break;
                            }
                    }

                }


                if (btnHome.isIn(event))
                    if (btnHome.isPushed())
                        game.getMenu().setCurrentMenuState(Menu.MenuState.START_MENU);
                if (btnNext.isIn(event))
                    if (btnNext.isPushed())
                        nextPage();
                if (btnPrev.isIn(event))
                    if (btnPrev.isPushed())
                        prevPage();
                if (btnAdd.isIn(event))
                    if (btnAdd.isPushed())
                        createMapScreen();

                for (int i = 0; i < btnLvl.length; i++) {
                    if (i >= page * 3 && i < page * 3 + 3)
                        btnLvl[i].setPushed(false);
                }

                btnHome.setPushed(false);
                btnNext.setPushed(false);
                btnPrev.setPushed(false);
                btnAdd.setPushed(false);
            }
        }


    }

    private void createMapScreen() {

        addLvl = new AddLvlScreen(game, this);
        inAddLvl = true;

    }

    private void nextPage(){
        if (page != maxPage)
            page++;
        else
            page = 0;
    }

    private void prevPage(){
        if (page != 0)
            page--;
        else
            page = maxPage;
    }

    private void selectLvl(int i) {

        int index = 0;

        for (Map.Entry<String, Boolean> map : maps.entrySet()){
            if (index == i){

                lvlScreen = new CreatedLvlScreen(game, map.getKey(), map.getValue(), this);
                inLvlScreen = true;


            }

            index++;
        }

    }




    private boolean saveMapAsString(String mapName, String mapData) {
        File file = new File(MainActivity.getGameContext().getFilesDir(), mapName + ".txt");

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





    public void setInLvlScreen(boolean inLvlScreen){
        this.inLvlScreen = inLvlScreen;
        maps = loadVerifications();
        maxPage = maps.size() / 4;
        initLvlItems();
    }

    public void setInAddLvl(boolean inAddLvl){
        this.inAddLvl = inAddLvl;
        maps = loadVerifications();
        maxPage = maps.size() / 4;
        initLvlItems();
    }



}
