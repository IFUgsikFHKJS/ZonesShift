package com.example.zonesshift;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.gamestates.createlvl.CreatedLvlsList;
import com.example.zonesshift.gamestates.menustates.LeaderBoard;
import com.example.zonesshift.gamestates.menustates.LevelScreen;
import com.example.zonesshift.main.GameLoop;
import com.example.zonesshift.main.MainActivity;

public class Game {

    private SurfaceHolder holder;
    private Menu menu;
    private Playing playing;
    private GameLoop gameLoop;
    private LevelScreen levelScreen;
    private LeaderBoard leaderBoard;

    private int temp = 0;


    private GameState currentGameState = GameState.MENU;

    public Game(SurfaceHolder holder){
        this.holder = holder;
        gameLoop = new GameLoop(this);
        initGameStates();
        Tile.playing = playing;

    }

    private void initGameStates() {
        menu = new Menu(this);
        playing = new Playing(this);
        if(Playing.getMapManager().getCurrentMapId() == -1){
            currentGameState = GameState.PLAYING;
        }
    }

    public boolean touchEvent(MotionEvent event) {
        switch (currentGameState){
            case MENU -> menu.touchEvents(event);
            case PLAYING -> playing.touchEvents(event);
            case LEVELSCREEN -> levelScreen.touchEvents(event);
            case LEADERBOARD -> leaderBoard.touchEvents(event);
        }
        return true;
    }

    public void update(double delta){
        switch (currentGameState){
            case MENU -> menu.update(delta);
            case PLAYING -> playing.update(delta);
            case LEVELSCREEN -> levelScreen.update(delta);
        }

    }

    public void render() {
        Canvas c = holder.lockCanvas();
        if (c != null) {
            c.drawColor(MainActivity.getGameContext().getColor(R.color.background));

            switch (currentGameState) {
                case MENU -> menu.render(c);
                case PLAYING -> playing.render(c);
                case LEVELSCREEN -> levelScreen.render(c);
                case LEADERBOARD -> leaderBoard.render(c);
            }

            holder.unlockCanvasAndPost(c);
        }
    }

    public enum GameState{
        MENU, PLAYING, LEVELSCREEN, LEADERBOARD;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState gameState){
        this.currentGameState = gameState;
    }

    public void setCurrentGameStateLvlScreen(LevelScreen lvlScreen){
        this.levelScreen = lvlScreen;
        this.currentGameState = GameState.LEVELSCREEN;
    }

    public void setCurrentGameStateLeaderBoard(LeaderBoard leaderBoard){
        this.leaderBoard = leaderBoard;
        this.currentGameState = GameState.LEADERBOARD;
    }


    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void startGameLoop() {
        gameLoop.startGameLoop();
    }

    public void stopGameLoop() {
        gameLoop.stopGameLoop();
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTemp() {
        return temp;
    }
}
