package com.zsgame.zonesshift.main;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.zsgame.zonesshift.Game;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private Game game;
    private static Context context;

    public GamePanel(Context context) {
        super(context);
        GamePanel.context = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        game = new Game(holder);
    }

    public static Context getGameContext(){
        return context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.touchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        game.startGameLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        game.stopGameLoop();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public static void setContext(Context context) {
        GamePanel.context = context;
    }

    public void setHolder(){
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        game = MainActivity.getGame();
    }


}
