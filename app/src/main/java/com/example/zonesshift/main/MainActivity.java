package com.example.zonesshift.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static Context gameContext;
    public static int GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH_RES, GAME_HEIGHT_RES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        GAME_HEIGHT = dm.heightPixels;
        GAME_WIDTH = dm.widthPixels;

        int gcd = gcd(GAME_WIDTH, GAME_HEIGHT);
        GAME_WIDTH_RES = GAME_WIDTH / gcd;
        GAME_HEIGHT_RES = GAME_HEIGHT / gcd;

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(new GamePanel(this));



        System.out.println((float) GAME_WIDTH / GAME_HEIGHT + " " + GAME_WIDTH + " " + GAME_HEIGHT + " " + GAME_WIDTH_RES + " " + GAME_HEIGHT_RES);
    }

    public static Context getGameContext(){
        return gameContext;
    }

    int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

}