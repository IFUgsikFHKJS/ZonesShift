package com.example.zonesshift.gamestates.searchlvl;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.HOME;
import static com.example.zonesshift.ui.ButtonImages.LVL_ITEM;
import static com.example.zonesshift.ui.ButtonImages.NEXT_LVL;
import static com.example.zonesshift.ui.ButtonImages.PREV_LVL;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.zonesshift.Game;
import com.example.zonesshift.R;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.CustomButton;

import java.util.List;
import java.util.Map;

public class OnlineLvlsList extends BaseState implements GameStateInterface, BitmapMethods {

    private final SearchMainScreen parent;
    private CustomButton btnHome;
    private List<Map<String, Object>> maps;

    private int maxPage;
    private int page = 0;

    private Typeface typeface;
    private Paint paint;
    private float textHeight;

    private CustomButton[] btnLvl;
    private CustomButton btnNext;
    private CustomButton btnPrev;

    private OnlineLvlScreen lvlScreen;
    private boolean inLvlScreen = false;



    public OnlineLvlsList(Game game, SearchMainScreen parent, List<Map<String, Object>> maps) {
        super(game);
        this.parent = parent;
        this.maps = maps;


        maxPage = maps.size() / 4;

        setPaintSettings();
        innitButtons();
    }

    private void innitButtons() {

        btnHome = new CustomButton(GAME_WIDTH / 26, GAME_HEIGHT / 13, HOME.getWidth(), HOME.getHeight());
        btnNext = new CustomButton( GAME_WIDTH - GAME_WIDTH / 20 - NEXT_LVL.getWidth(), GAME_HEIGHT / 2 - NEXT_LVL.getHeight() / 2, NEXT_LVL.getWidth(), NEXT_LVL.getHeight() );
        btnPrev = new CustomButton( GAME_WIDTH / 20, GAME_HEIGHT / 2 - PREV_LVL.getHeight() / 2, PREV_LVL.getWidth(), PREV_LVL.getHeight() );
        initLvlItems();

    }

    private void initLvlItems() {
        btnLvl = new CustomButton[maps.size()];

        float x = (float) GAME_WIDTH / 2 - (float) GAME_WIDTH / 5;
        float defaultY = (float) GAME_HEIGHT / 8;

        for (int i = 0; i < maps.size(); i++) {
            float itemY = defaultY + (float) (i % 3 * GAME_HEIGHT) / 4;

            btnLvl[i] = new CustomButton(x, itemY, LVL_ITEM.getWidth(), LVL_ITEM.getHeight());

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
        if (inLvlScreen)
            lvlScreen.update(delta);
    }

    @Override
    public void render(Canvas c) {

        if (inLvlScreen)
            lvlScreen.render(c);
        else
            drawButtons(c);

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

        drawLvls(c);

    }

    private void drawLvls(Canvas c) {
        if (btnLvl == null || maps == null) return;

        // Рисуем кнопки
        for (int i = 0; i < btnLvl.length; i++) {
            if (i >= page * 3 && i < page * 3 + 3) {
                c.drawBitmap(
                        LVL_ITEM.getBtnImg(btnLvl[i].isPushed()),
                        btnLvl[i].getHitbox().left,
                        btnLvl[i].getHitbox().top,
                        null
                );
            }
        }

        // Рисуем названия карт
        for (int i = page * 3; i < Math.min(page * 3 + 3, maps.size()); i++) {
            Map<String, Object> map = maps.get(i);
            String mapName = (String) map.get("name");

            float x = btnLvl[i].getHitbox().left;
            float y = btnLvl[i].getHitbox().top;

            float textX = x + ((GAME_WIDTH / 2.5f) - paint.measureText(mapName)) / 2;
            float textY = y + ((float) GAME_HEIGHT / 5 - textHeight);

            c.drawText(mapName, textX, textY, paint);
        }
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (inLvlScreen)
            lvlScreen.touchEvents(event);
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
                else if (btnNext.isIn(event))
                    btnNext.setPushed(true);
                else if (btnPrev.isIn(event))
                    btnPrev.setPushed(true);


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
                        parent.setInLvlsList(false);

                if (btnNext.isIn(event))
                    if (btnNext.isPushed())
                        nextPage();
                if (btnPrev.isIn(event))
                    if (btnPrev.isPushed())
                        prevPage();


                for (int i = 0; i < btnLvl.length; i++) {
                    if (i >= page * 3 && i < page * 3 + 3)
                        btnLvl[i].setPushed(false);
                }

                btnHome.setPushed(false);
            }
        }
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
        if (i >= 0 && i < maps.size()) {
            Map<String, Object> map = maps.get(i);
            String name = (String) map.get("name");
            int authorID = ((Long) map.get("author_id")).intValue();
            int id = ((Long) map.get("map_id")).intValue();
            String mapData = (String) map.get("map_data");



            lvlScreen = new OnlineLvlScreen(game,name, authorID, id, mapData, this);
            inLvlScreen = true;
        }
    }



    public void setInLvlScreen(boolean inLvlScreen) {
        this.inLvlScreen = inLvlScreen;
    }
}
