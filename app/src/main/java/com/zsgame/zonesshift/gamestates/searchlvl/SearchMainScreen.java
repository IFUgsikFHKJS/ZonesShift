package com.zsgame.zonesshift.gamestates.searchlvl;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.zsgame.zonesshift.ui.ButtonImages.HOME;
import static com.zsgame.zonesshift.ui.ButtonImages.SEARCH;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.gamestates.BaseState;
import com.zsgame.zonesshift.gamestates.Menu;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.main.MainActivity;
import com.zsgame.zonesshift.ui.CustomButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchMainScreen extends BaseState implements GameStateInterface, BitmapMethods {

    private CustomButton btnHome;
    private CustomButton btnSearch;

    private EditText editText;

    private Typeface typeface;
    private Paint paint;

    private OnlineLvlsList lvlsList;
    private boolean inLvlsList = false;



    public SearchMainScreen(Game game) {
        super(game);

        setPaintSettings();
        setEditTextSettings();
        initButtons();

    }

    private void setEditTextSettings(){
        Context context = GamePanel.getGameContext();

        if (editText != null && editText.getParent() != null) {
            ((ViewGroup) editText.getParent()).removeView(editText);
        }

        editText = new EditText(context);
        editText.setTypeface(typeface);
        editText.setHint("Map Name or ID");
        editText.setBackgroundColor(R.color.text_color);
        editText.setX(GAME_WIDTH / 2 - GAME_WIDTH / 5); // позиция по X
        editText.setY((float) GAME_HEIGHT / 8);
        editText.setTextSize(27);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);





        if (context instanceof Activity) {
            System.out.println(editText);

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

    private void initButtons() {


        btnHome = new CustomButton(GAME_WIDTH / 26, GAME_HEIGHT / 13, HOME.getWidth(), HOME.getHeight());
        btnSearch = new CustomButton((float) (editText.getX() + GAME_WIDTH / 2.4), editText.getY(), SEARCH.getWidth(), SEARCH.getHeight());

    }

    @Override
    public void update(double delta) {

        if (inLvlsList)
            lvlsList.update(delta);

    }

    @Override
    public void render(Canvas c) {

        if (inLvlsList)
            lvlsList.render(c);
        else {
            drawButtons(c);
        }

    }

    private void drawButtons(Canvas c) {

        c.drawBitmap(HOME.getBtnImg(btnHome.isPushed()),
                btnHome.getHitbox().left,
                btnHome.getHitbox().top, null);
        c.drawBitmap(SEARCH.getBtnImg(btnSearch.isPushed()),
                btnSearch.getHitbox().left,
                btnSearch.getHitbox().top, null);

    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (inLvlsList)
            lvlsList.touchEvents(event);
        else{
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (btnHome.isIn(event))
                    btnHome.setPushed(true);
                else if (btnSearch.isIn(event))
                    btnSearch.setPushed(true);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {


                if (btnHome.isIn(event))
                    if (btnHome.isPushed()) {
                        removeEditText();
                        game.getMenu().setCurrentMenuState(Menu.MenuState.START_MENU);
                    }
                if (btnSearch.isIn(event))
                    if (btnSearch.isPushed()) {
                        searchMaps(editText.getText().toString().trim(), maps -> {
                            maps.removeIf(map -> ((long) map.get("map_id") < 100));
                            toLvlsList(maps);
//                            for (Map<String, Object> map : maps) {
//                                Log.d("Result", "Карта: " + map.get("name") + ", ID: " + map.get("map_id"));
//                                System.out.println(map.get("map_data"));
//
//                            }

                        });
                    }


                btnHome.setPushed(false);
                btnSearch.setPushed(false);
            }
        }
        }

    private void toLvlsList(List<Map<String, Object>> maps) {

        removeEditText();
        lvlsList = new OnlineLvlsList(game, this, maps);
        inLvlsList = true;
    }

    public void removeEditText() {
        if (editText != null && editText.getParent() != null)
            ((ViewGroup) editText.getParent()).removeView(editText);
    }


    public static void searchMaps(String input, OnMapSearchResult listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Map<String, Object>> resultList = new ArrayList<>();

        // Попробуем сначала как число (для поиска по map_id)
        try {
            int id = Integer.parseInt(input);
            db.collection("maps")
                    .whereEqualTo("map_id", id)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            resultList.add(doc.getData());
                        }
                        // После поиска по ID запускаем поиск по имени
                        searchByName(db, input, resultList, listener);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Ошибка при поиске по map_id", e);
                        listener.onSearchComplete(resultList);
                    });
        } catch (NumberFormatException e) {
            // Не число — значит ищем только по имени
            searchByName(db, input, resultList, listener);
        }
    }

    private static void searchByName(FirebaseFirestore db, String input, List<Map<String, Object>> resultList, OnMapSearchResult listener) {
        db.collection("maps")
                .orderBy("name")
                .startAt(input)
                .endAt(input + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = doc.getData();
                        if (!resultList.contains(data)) {
                            resultList.add(data);
                        }
                    }
                    listener.onSearchComplete(resultList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Ошибка при поиске по имени", e);
                    listener.onSearchComplete(resultList);
                });
    }

    public void setInLvlsList(boolean inLvlsList) {
        this.inLvlsList = inLvlsList;
        setEditTextSettings();
    }
}
