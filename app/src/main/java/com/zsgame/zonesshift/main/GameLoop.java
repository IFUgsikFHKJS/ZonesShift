package com.zsgame.zonesshift.main;

import com.zsgame.zonesshift.Game;

public class GameLoop implements Runnable{

    private Thread gameThread;
    private final Game game;
    private boolean isRunning = false;

    public GameLoop(Game game){
        this.game = game;
    }
    @Override
    public void run() {

        long lastFPSCheck = System.currentTimeMillis();

        long lastDelta = System.nanoTime();
        long nanoSec = 1_000_000_000;
        while (isRunning) {

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
        if (gameThread == null || !isRunning) {
            isRunning = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGameLoop() {
        isRunning = false;
        try {
            if (gameThread != null) {
                gameThread.join(); // Wait for the thread to finish
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
