package com.example.zonesshift.gamestates.menustates;

import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.example.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

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
import com.example.zonesshift.authentication.UserInfo;
import com.example.zonesshift.gamestates.BaseState;
import com.example.zonesshift.helpers.interfaces.BitmapMethods;
import com.example.zonesshift.helpers.interfaces.GameStateInterface;
import com.example.zonesshift.main.GamePanel;
import com.example.zonesshift.main.MainActivity;
import com.example.zonesshift.ui.ButtonImages;
import com.example.zonesshift.ui.CustomButton;
import com.example.zonesshift.userresults.AddBestTime;
import com.example.zonesshift.userresults.GetBestTime;
//import com.example.zonesshift.userresults.GetTopPlayerOnMap;
import com.example.zonesshift.userresults.GetTopPlayerOnMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LevelScreen extends BaseState implements GameStateInterface, BitmapMethods {

    // level info
    private String name;
    private String author;
    private String bestTime;
    private Integer userId;
    private Map<String, String> topPlayerList;


    // text
    private Typeface typeface;
    private Paint paint;
    private Paint leaderBoardPaint;

    // buttons
    private CustomButton btnPlay;
    private CustomButton btnLvls;
    private CustomButton btnLeaderBoard;
    private int currentId;

    public LevelScreen(Game game, int id) {
        super(game);
        getMapInfo(id, mapData -> {
            name = (String) mapData.get("name");
            author = (String) mapData.get("author_name");

        }, e -> System.out.println("Ошибка: " + e.getMessage()));
        initButtons();

        currentId = id;


        getUserInfoAndThen(() -> {
            getTopPlayers();
            getBestTime();
        });
//        getTopPlayers();
        setPaintSettings();

        System.out.println(userId);

        if(userId != null){
            GetBestTime.getBestTimeForUser(userId, id,
                    bestTime -> {
                        System.out.println("Bt " + bestTime);
                        setBestTime(bestTime);
                    },
                    e -> {
                        System.out.println("Ошибка получения времени: " + e.getMessage());
                    }
            );
        }

        System.out.println( "Best time = " + bestTime);


    }

    private void getTopPlayers(){
        GetTopPlayerOnMap.getTopPlayers(String.valueOf(currentId),
                topPlayers -> {
                    topPlayerList = topPlayers;
                    leaderBoardPaint.setAlpha(255);


                },
                e -> System.out.println("Ошибка: " + e.getMessage())
        );
    }

    private void setPaintSettings() {
        typeface = ResourcesCompat.getFont(MainActivity.getGameContext(), R.font.minecraft);

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(MainActivity.getGameContext(), R.color.text_color));
        paint.setAlpha(150);
        paint.setTypeface(typeface);
        paint.setTextSize((float) GAME_WIDTH / 30);

        leaderBoardPaint = new Paint();
        leaderBoardPaint.setAlpha(200);
    }

    private void getUserInfoAndThen(Runnable callback) {
        UserInfo.getUserId(new UserInfo.UserIdCallback() {
            @Override
            public void onUserIdReceived(int userId) {
                System.out.println("User ID: " + userId);
                setUserId(userId);
                callback.run(); // Запускаем остальные операции после получения userId
            }

            @Override
            public void onError(String error) {
                System.out.println("Error: " + error);
            }
        });
    }

    private void getBestTime() {
        GetBestTime.getBestTimeForUser(userId, currentId,
                bestTime -> {
                    System.out.println("Best Time: " + bestTime);
                    setBestTime(bestTime);
//                     game.getGamePanel().invalidate(); // Перерисовываем экран после обновления bestTime
                },
                e -> {
                    System.out.println("Ошибка получения времени: " + e.getMessage());
                }
        );
    }

    private void initButtons() {
        btnPlay = new CustomButton((float) GAME_WIDTH / 2 - (float) ButtonImages.PLAY.getWidth() / 2,
                (float) GAME_HEIGHT / 2 - (float) ButtonImages.PLAY.getHeight() / 2,
                ButtonImages.PLAY.getWidth(), ButtonImages.PLAY.getHeight());
        btnLvls = new CustomButton(GAME_WIDTH / 26, GAME_WIDTH / 26, ButtonImages.PLAYING_TO_LVL.getWidth(), ButtonImages.PLAYING_TO_LVL.getHeight());
        btnLeaderBoard = new CustomButton( GAME_WIDTH - ButtonImages.LEADERBOARD.getWidth() - GAME_WIDTH / 26,
                 GAME_WIDTH / 26
                , ButtonImages.LEADERBOARD.getWidth(), ButtonImages.LEADERBOARD.getHeight());
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        drawBackground(c);
        drawButtons(c);
        drawBestTime(c);
    }

    private void drawBestTime(Canvas c) {
        if (bestTime != null){
            c.drawText(bestTime, ((float) GAME_WIDTH / 2) - paint.measureText(bestTime) / 2,
                btnPlay.getHitbox().bottom + (float) ButtonImages.PLAYING_TO_LVL.getHeight() , paint);
        }
    }

    private void drawBackground(Canvas c) {
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.menu_background, options);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 144, 81);
        bitmap = getScaledBitmapButton(bitmap, GAME_WIDTH, GAME_HEIGHT);
        c.drawBitmap(bitmap, 0, 0, null);
    }

    private void drawButtons(Canvas c) {
        c.drawBitmap(
                ButtonImages.PLAY.getBtnImg(btnPlay.isPushed()),
                btnPlay.getHitbox().left,
                btnPlay.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.PLAYING_TO_LVL.getBtnImg(btnLvls.isPushed()),
                btnLvls.getHitbox().left,
                btnLvls.getHitbox().top, null);
        c.drawBitmap(
                ButtonImages.LEADERBOARD.getBtnImg(btnLeaderBoard.isPushed()),
                btnLeaderBoard.getHitbox().left,
                btnLeaderBoard.getHitbox().top, leaderBoardPaint);
    }

    @Override
    public void touchEvents(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (btnLvls.isIn(event))
                btnLvls.setPushed(true);
            else if (btnPlay.isIn(event))
                btnPlay.setPushed(true);
            else if (btnLeaderBoard.isIn(event))
                btnLeaderBoard.setPushed(true);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (btnLvls.isIn(event))
                if (btnLvls.isPushed())
                    game.setCurrentGameState(Game.GameState.MENU);
            if (btnPlay.isIn(event))
                if (btnPlay.isPushed()){
                    game.getPlaying().restartCurrentMap();
                    game.setCurrentGameState(Game.GameState.PLAYING);
                }
            if (btnLeaderBoard.isIn(event))
                if (btnLeaderBoard.isPushed()){
                    if(topPlayerList != null)
                        game.setCurrentGameStateLeaderBoard(new LeaderBoard(game, topPlayerList));
                }

            btnLvls.setPushed(false);
            btnPlay.setPushed(false);
            btnLeaderBoard.setPushed(false);
        }

    }


    public void getMapInfo(int mapId, OnSuccessListener<Map<String, Object>> onSuccess, OnFailureListener onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Ищем карту по mapId
        db.collection("maps")
                .whereEqualTo("map_id", mapId) // Предполагаем, что mapId хранится в поле "map_id"
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot mapSnapshot = querySnapshot.getDocuments().get(0);
                        Map<String, Object> mapData = mapSnapshot.getData();

                        if (mapData == null) {
                            onFailure.onFailure(new Exception("Map data is null"));
                            return;
                        }

                        String authorId =  String.valueOf(mapData.get("author_id"));

                        if (authorId != null) {
                            // Ищем пользователя по userId
                            db.collection("users")
                                    .whereEqualTo("user_id", Integer.parseInt(authorId)) // Предполагаем, что user_id хранится внутри документа
                                    .limit(1)
                                    .get()
                                    .addOnSuccessListener(userQuerySnapshot -> {
                                        if (!userQuerySnapshot.isEmpty()) {
                                            DocumentSnapshot userSnapshot = userQuerySnapshot.getDocuments().get(0);
                                            String username = userSnapshot.getString("username");
                                            mapData.put("author_name", username != null ? username : "Unknown");
                                        } else {
                                            mapData.put("author_name", "Unknown");
                                        }
                                        onSuccess.onSuccess(mapData);
                                    })
                                    .addOnFailureListener(onFailure);
                        } else {
                            mapData.put("author_name", "Unknown");
                            onSuccess.onSuccess(mapData);
                        }
                    } else {
                        onFailure.onFailure(new Exception("Map not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }
}
