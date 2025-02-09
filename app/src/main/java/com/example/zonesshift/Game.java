package com.example.zonesshift;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.gamestates.Menu;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.gamestates.menustates.StartMenu;
import com.example.zonesshift.main.GameLoop;
import com.example.zonesshift.main.MainActivity;

public class Game {

    private SurfaceHolder holder;
    private Menu menu;
    private Playing playing;
    private GameLoop gameLoop;

    private StartMenu startMenu;

    private GameState currentGameState = GameState.MENU;

    public Game(SurfaceHolder holder){
        this.holder = holder;
        gameLoop = new GameLoop(this);
        initGameStates();
        Tile.playing = playing;

        startMenu = new StartMenu(this);
    }

    private void initGameStates() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    public boolean touchEvent(MotionEvent event) {
        switch (currentGameState){
            case MENU -> menu.touchEvents(event);
            case PLAYING -> playing.touchEvents(event);
        }
        return true;
    }

    public void update(double delta){
        switch (currentGameState){
            case MENU -> menu.update(delta);
            case PLAYING -> playing.update(delta);
        }

    }

    public void render(){
        Canvas c = holder.lockCanvas();

        c.drawColor(MainActivity.getGameContext().getColor(R.color.background));


        switch (currentGameState){
            case MENU -> menu.render(c);
            case PLAYING -> playing.render(c);
        }


        holder.unlockCanvasAndPost(c);
    }

    public enum GameState{
        MENU, PLAYING;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState gameState){
        this.currentGameState = gameState;
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

}
