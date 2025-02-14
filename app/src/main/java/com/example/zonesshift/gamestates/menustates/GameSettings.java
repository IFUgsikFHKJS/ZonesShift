package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;
import static com.example.zonesshift.ui.ButtonImages.HOME;
import static com.example.zonesshift.ui.ButtonImages.SETTINGS_SIGNOUT;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import com.example.zonesshift.Game;
import com.example.zonesshift.R;
import com.example.zonesshift.authentication.LoginActivity;
import com.example.zonesshift.authentication.UserInfo;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.CustomButton;
import com.google.firebase.auth.FirebaseAuth;

public class GameSettings extends BaseState implements GameStateInterface {

    private Typeface typeface;
    private Paint paint;
    private String username = "Loading...";
    private CustomButton btnSignOut;
    private CustomButton btnHome;


    public GameSettings(Game game) {
        super(game);

        typeface = ResourcesCompat.getFont(GamePanel.getGameContext(), R.font.minecraft);
        paint = new Paint();
        paint.setColor(R.color.text_color);
        paint.setAlpha(150);
        paint.setTypeface(typeface);
        paint.setTextSize((float) GAME_WIDTH / 30);

        btnSignOut = new CustomButton(GAME_WIDTH / 2 - SETTINGS_SIGNOUT.getWidth() / 2,
                GAME_HEIGHT / 2 - SETTINGS_SIGNOUT.getHeight() / 2,
                SETTINGS_SIGNOUT.getWidth(), SETTINGS_SIGNOUT.getHeight());

        btnHome = new CustomButton(GAME_WIDTH / 26, GAME_WIDTH / 26, HOME.getWidth(), HOME.getHeight());


        UserInfo.getUserName(new UserInfo.UserNameCallback() {
            @Override
            public void onUserNameReceived(String username) {
                setUsername(username);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error: " + error);
            }
        });
    }


    @Override
    public void update(double delta) {

    }

    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public void render(Canvas c) {
        String displayName = (username != null) ? username : "Loading...";
        c.drawText(displayName, (float) (GAME_WIDTH / 2) - paint.measureText(displayName) / 2, (float) GAME_HEIGHT / 6, paint);
        drawButtons(c);
    }

    private void drawButtons(Canvas c){
        c.drawBitmap(
                SETTINGS_SIGNOUT.getBtnImg(btnSignOut.isPushed()),
                btnSignOut.getHitbox().left,
                btnSignOut.getHitbox().top, null);

        c.drawBitmap(HOME.getBtnImg(btnHome.isPushed()),
                btnHome.getHitbox().left,
                btnHome.getHitbox().top, null);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (btnSignOut.isIn(event))
                btnSignOut.setPushed(true);

            if(btnHome.isIn(event))
                btnHome.setPushed(true);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (btnSignOut.isIn(event)) {
                if (btnSignOut.isPushed()) {
                    FirebaseAuth.getInstance().signOut();
                    Intent loginIntent = new Intent(GamePanel.getGameContext(), LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                    GamePanel.getGameContext().startActivity(loginIntent);
                }
            }

            if (btnHome.isIn(event))
                if (btnHome.isPushed())
                    game.getMenu().setCurrentMenuState(Menu.MenuState.START_MENU);

            btnSignOut.setPushed(false);
            btnHome.setPushed(false);
        }
    }

}
