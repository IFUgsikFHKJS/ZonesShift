package com.example.zonesshift;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.zonesshift.environments.Tile;
import com.example.zonesshift.gamestates.Playing;
import com.example.zonesshift.main.GameLoop;

public class Game {

    private SurfaceHolder holder;
    private Menu menu;
    private Playing playing;
    private GameLoop gameLoop;

    public Game(SurfaceHolder holder){
        this.holder = holder;
        gameLoop = new GameLoop(this);
        playing = new Playing(this);
        Tile.playing = playing;
    }

    public boolean touchEvent(MotionEvent event) {
        playing.touchEvents(event);
        return true;
    }

    public void update(double delta){
        playing.update(delta);
    }

    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        playing.render(c);

        holder.unlockCanvasAndPost(c);
    }

    public void startGameLoop() {
        gameLoop.startGameLoop();
    }

}
