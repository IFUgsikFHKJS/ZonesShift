package com.example.zonesshift.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zonesshift.authentication.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


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

        boolean fromProfile = getIntent().getBooleanExtra("fromProfile", false);
        System.out.println(fromProfile);
        System.out.println(FirebaseAuth.getInstance().getCurrentUser() == null);
        if (FirebaseAuth.getInstance().getCurrentUser() == null && !fromProfile) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }



        setContentView(new GamePanel(this));



    }

    public static Context getGameContext(){
        return gameContext;
    }

    int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

}