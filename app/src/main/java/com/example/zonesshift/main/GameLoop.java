package com.example.zonesshift.main;

import com.example.zonesshift.Game;

public class GameLoop implements Runnable{

    private final Thread gameThread;
    private final Game game;

    public GameLoop(Game game){
        this.game = game;
        gameThread = new Thread(this);
    }
    @Override
    public void run() {

        long lastFPSCheck = System.currentTimeMillis();

        long lastDelta = System.nanoTime();
        long nanoSec = 1_000_000_000;
        while (true) {

            long nowDelta = System.nanoTime();
            double timeSinceLastDelta = nowDelta - lastDelta;
            double delta = timeSinceLastDelta / nanoSec;

            game.update(delta);
            game.render();
            lastDelta = nowDelta;

            long now = System.currentTimeMillis();
            if(now - lastFPSCheck >= 1000){
                lastFPSCheck += 1000;
            }
        }
    }

    public void startGameLoop() {
        gameThread.start();
    }
}
