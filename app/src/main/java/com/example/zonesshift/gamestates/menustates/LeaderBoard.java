package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
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

import java.util.Map;

public class LeaderBoard extends BaseState implements GameStateInterface, BitmapMethods {

    private Map<String, String> topPlayers;
    private float itemWidth = (float) GAME_WIDTH / 3;
    private float itemHeight = (float) GAME_HEIGHT / 7;
    private Paint paint;


    private Typeface typeface;
    private Paint textPaint;

    public LeaderBoard(Game game, Map<String, String> topPlayers) {
        super(game);
        this.topPlayers = topPlayers;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        setPaintSettings();
    }

    private void setPaintSettings() {
        typeface = ResourcesCompat.getFont(GamePanel.getGameContext(), R.font.minecraft);
        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(MainActivity.getGameContext(), R.color.text_color));
        textPaint.setAlpha(150);
        textPaint.setTypeface(typeface);
        textPaint.setTextSize((float) GAME_WIDTH / 40);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        drawBackground(c);
        drawItems(c);
    }

    private void drawBackground(Canvas c) {
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.menu_background, options);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 144, 81);
        bitmap = getScaledBitmapButton(bitmap, GAME_WIDTH, GAME_HEIGHT);
        c.drawBitmap(bitmap, 0, 0, null);
    }

    private void drawItems(Canvas c) {
        for (int i = 0; i < topPlayers.size(); i++){
            RectF item = new RectF(itemWidth / 3 + (i > 4 ? itemWidth : 0),
                    itemHeight + itemHeight * (i % 5),
                    itemWidth / 3 + (i > 4 ? itemWidth : 0 + itemWidth),
                    2 * itemHeight + itemHeight * (i % 5));
            c.drawText(String.valueOf(topPlayers.keySet().toArray()[i]), item.left + 20, item.top + 20, textPaint);
            String time = topPlayers.get(topPlayers.keySet().toArray()[i]);
            c.drawText(time, item.right - paint.measureText(time) - 20  ,item.top + 20, textPaint);
        }
    }

    @Override
    public void touchEvents(MotionEvent event) {

    }
}
