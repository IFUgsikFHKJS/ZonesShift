package com.example.zonesshift.gamestates.multiplayer.onlinelvls.mapeditor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zonesshift.R;


public class MapEditorActivity extends AppCompatActivity {

    private MapEditorView mapEditorView;
    private Button btnBlock, btnRedzone, btnGravizone, btnRemove, btnPlayer, btnWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_map_editor);

        mapEditorView = findViewById(R.id.mapEditorView);
        btnBlock = findViewById(R.id.btnSolid);
        btnRedzone = findViewById(R.id.btnRedZone);
        btnGravizone = findViewById(R.id.btnGravizone);
        btnRemove = findViewById(R.id.btnRemove);
        btnPlayer = findViewById(R.id.btnPlayer);
        btnWin = findViewById(R.id.btnWin);

        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('1');
            }
        });

        btnRedzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('R');
            }
        });

        btnGravizone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('G');
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('0');
            }
        });

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('P');
            }
        });

        btnWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('W');
            }
        });
    }
}