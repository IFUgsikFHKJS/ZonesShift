package com.zsgame.zonesshift.gamestates.createlvl;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.gamestates.BaseState;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.helpers.interfaces.Verifications;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.main.MainActivity;
import com.zsgame.zonesshift.ui.ButtonImages;
import com.zsgame.zonesshift.ui.CustomButton;

import java.io.File;
import java.util.Map;

public class DeleteLvlScreen extends BaseState implements GameStateInterface, BitmapMethods, Verifications {

    private CreatedLvlScreen parent;
    private String name;

    private Typeface typeface;
    private Paint paint;

    private String text = "Are you sure you want to delete the map?";

    private CustomButton btnCancel;
    private CustomButton btnConfirm;


    public DeleteLvlScreen(Game game, String name,CreatedLvlScreen parent) {
        super(game);
        this.name = name;
        this.parent = parent;

        initButtons();
        setPaintSettings();
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

        btnCancel = new CustomButton((float) GAME_WIDTH / 2 + ButtonImages.CANCEL.getWidth() / 2, (float) GAME_HEIGHT / 2, ButtonImages.CANCEL.getWidth(), ButtonImages.CANCEL.getHeight());
        btnConfirm = new CustomButton((float) ((float) GAME_WIDTH / 2 - ButtonImages.CONFIRM.getWidth() * 1.5),(float) GAME_HEIGHT / 2, ButtonImages.CONFIRM.getWidth(), ButtonImages.CONFIRM.getHeight());


    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {

        drawButtons(c);
        drawText(c);

    }

    private void drawText(Canvas c) {

        c.drawText(text, GAME_WIDTH / 2 - paint.measureText(text) / 2, GAME_HEIGHT / 3, paint);

    }

    private void drawButtons(Canvas c) {

        c.drawBitmap(
                ButtonImages.CANCEL.getBtnImg(btnCancel.isPushed()),
                btnCancel.getHitbox().left,
                btnCancel.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.CONFIRM.getBtnImg(btnConfirm.isPushed()),
                btnConfirm.getHitbox().left,
                btnConfirm.getHitbox().top, null);

    }

    @Override
    public void touchEvents(MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (btnCancel.isIn(event))
                    btnCancel.setPushed(true);
                else if(btnConfirm.isIn(event))
                    btnConfirm.setPushed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (btnCancel.isIn(event))
                    if (btnCancel.isPushed()) {
                        parent.setInDeleteScreen(false);
                    }

                if (btnConfirm.isIn(event))
                    if (btnConfirm.isPushed()) {
                        deleteLvl();
                        parent.setInDeleteScreen(false);
                        parent.removeEditText();
                        parent.parent.setInLvlScreen(false);
                    }

                btnCancel.setPushed(false);
                btnConfirm.setPushed(false);
            }

    }

    private boolean deleteLvl() {
        File file = new File(GamePanel.getGameContext().getFilesDir(), name + ".txt");

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Map<String, Boolean> maps = loadVerifications();
                maps.remove(name);
                saveVerifications(maps);
                Toast.makeText(GamePanel.getGameContext(), "Map deleted successfully.", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(GamePanel.getGameContext(), "Could not delete map.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        Toast.makeText(GamePanel.getGameContext(), "Could not delete map.", Toast.LENGTH_SHORT).show();
        return false;

    }
}
