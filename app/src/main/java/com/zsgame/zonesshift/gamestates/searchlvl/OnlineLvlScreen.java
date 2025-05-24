package com.zsgame.zonesshift.gamestates.searchlvl;

import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_HEIGHT;
import static com.zsgame.zonesshift.helpers.GameConstants.GameSize.GAME_WIDTH;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.authentication.UserInfo;
import com.zsgame.zonesshift.gamestates.BaseState;
import com.zsgame.zonesshift.gamestates.Playing;
import com.zsgame.zonesshift.gamestates.menustates.LeaderBoard;
import com.zsgame.zonesshift.helpers.interfaces.BitmapMethods;
import com.zsgame.zonesshift.helpers.interfaces.GameStateInterface;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.main.MainActivity;
import com.zsgame.zonesshift.ui.ButtonImages;
import com.zsgame.zonesshift.ui.CustomButton;
import com.zsgame.zonesshift.environments.mapmanagment.Map;
import com.zsgame.zonesshift.environments.mapmanagment.MapLoader;
import com.zsgame.zonesshift.userresults.GetBestTime;
import com.zsgame.zonesshift.userresults.GetTopPlayerOnMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class OnlineLvlScreen extends BaseState implements GameStateInterface, BitmapMethods {

    private String name;
    private int authorID;
    private String authorName;
    private int id;
    private String mapData;

    private int userId;
    private String bestTime;

    private java.util.Map<String, String> topPlayerList;

    private CustomButton btnBack;
    private CustomButton btnPlay;
    private CustomButton btnLeaderBoard;



    private OnlineLvlsList parent;

    private Typeface typeface;
    private Paint paint;
    private Paint leaderBoardPaint;
    private float textHeight;


    public OnlineLvlScreen(Game game, String name, int authorID, int id, String mapData, OnlineLvlsList parent) {
        super(game);
        Playing.getMapManager().setCurrentMapId(id);
        this.parent = parent;
        this.name = name;
        this.authorID = authorID;
        this.id = id;
        this.mapData = mapData;

        getMapInfo(id, mapData1 -> {
            authorName = (String) mapData1.get("author_name");

        }, e -> System.out.println("Ошибка: " + e.getMessage()));

        getUserInfoAndThen(() -> {
            getTopPlayers();
            getBestTime();

        });



        setPaintSettings();
        innitButtons();
    }

    private void getTopPlayers(){
        GetTopPlayerOnMap.getTopPlayers(String.valueOf(id),
                topPlayers -> {
                    topPlayerList = topPlayers;
                    leaderBoardPaint.setAlpha(255);


                },
                e -> System.out.println("Ошибка: " + e.getMessage())
        );
    }

    private void setPaintSettings() {
        typeface = ResourcesCompat.getFont(GamePanel.getGameContext(), R.font.minecraft);

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(MainActivity.getGameContext(), R.color.text_color));
        paint.setAlpha(200);
        paint.setTypeface(typeface);
        paint.setTextSize((float) GAME_WIDTH / 30);

        leaderBoardPaint = new Paint();
        leaderBoardPaint.setAlpha(200);

        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = fm.bottom - fm.top + fm.leading;
    }

    private void innitButtons() {
        float btnPlayX = (float) GAME_WIDTH / 2 - (float) ButtonImages.PLAY.getWidth() / 2;
        float btnPlayY = (float) GAME_HEIGHT / 2 - ButtonImages.PLAY.getHeight() / 2;

        btnBack = new CustomButton((float) GAME_WIDTH / 20, (float) GAME_HEIGHT / 10, ButtonImages.PLAYING_TO_LVL.getWidth(), ButtonImages.PLAYING_TO_LVL.getHeight());
        btnPlay = new CustomButton(btnPlayX, btnPlayY,
                ButtonImages.PLAY.getWidth(), ButtonImages.PLAY.getHeight());
        btnLeaderBoard = new CustomButton( GAME_WIDTH - ButtonImages.LEADERBOARD.getWidth() - GAME_WIDTH / 26,
                GAME_WIDTH / 26
                , ButtonImages.LEADERBOARD.getWidth(), ButtonImages.LEADERBOARD.getHeight());
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void render(Canvas c) {

        drawButtons(c);
        drawName(c);
        drawBestTime(c);
    }

    private void drawBestTime(Canvas c) {
        if (bestTime != null){
            c.drawText(bestTime, ((float) GAME_WIDTH / 2) - paint.measureText(bestTime) / 2,
                    btnPlay.getHitbox().bottom + (float) ButtonImages.PLAYING_TO_LVL.getHeight() , paint);
        }
    }

    private void drawName(Canvas c) {
        float x = GAME_WIDTH / 2 - paint.measureText(name) / 2;
        c.drawText(name, x, GAME_HEIGHT / 5, paint);
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
                ButtonImages.LEADERBOARD.getBtnImg(btnLeaderBoard.isPushed()),
                btnLeaderBoard.getHitbox().left,
                btnLeaderBoard.getHitbox().top, leaderBoardPaint);

    }

    @Override
    public void touchEvents(MotionEvent event) {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (btnBack.isIn(event))
                    btnBack.setPushed(true);
                else if (btnPlay.isIn(event))
                    btnPlay.setPushed(true);
                else if (btnLeaderBoard.isIn(event))
                    btnLeaderBoard.setPushed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (btnBack.isIn(event))
                    if (btnBack.isPushed()) {
                        parent.setInLvlScreen(false);
                    }
                if (btnPlay.isIn(event))
                    if (btnPlay.isPushed()) {
                        Map map = new Map(MapLoader.loadMapFromString(mapData));

                        game.getPlaying().setCurrentOnlineMap(map, id);
                        game.setCurrentGameState(Game.GameState.PLAYING);
                    }
                if (btnLeaderBoard.isIn(event))
                    if (btnLeaderBoard.isPushed()){
                        if(topPlayerList != null)
                            game.setCurrentGameStateLeaderBoard(new LeaderBoard(game, topPlayerList));
                    }

                btnBack.setPushed(false);
                btnPlay.setPushed(false);
                btnLeaderBoard.setPushed(false);
            }
        }

    public void getMapInfo(int mapId, OnSuccessListener<java.util.Map<String, Object>> onSuccess, OnFailureListener onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Ищем карту по mapId
        db.collection("maps")
                .whereEqualTo("map_id", mapId) // Предполагаем, что mapId хранится в поле "map_id"
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot mapSnapshot = querySnapshot.getDocuments().get(0);
                        java.util.Map<String, Object> mapData = mapSnapshot.getData();

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
        GetBestTime.getBestTimeForUser(userId, id,
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }
}
