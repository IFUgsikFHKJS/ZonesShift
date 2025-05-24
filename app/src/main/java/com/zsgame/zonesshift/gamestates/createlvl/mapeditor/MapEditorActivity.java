package com.zsgame.zonesshift.gamestates.createlvl.mapeditor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.zsgame.zonesshift.Game;
import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.gamestates.Playing;
import com.zsgame.zonesshift.helpers.interfaces.Verifications;
import com.zsgame.zonesshift.main.GamePanel;
import com.zsgame.zonesshift.environments.mapmanagment.Map;
import com.zsgame.zonesshift.main.MainActivity;


public class MapEditorActivity extends AppCompatActivity implements Verifications {

    private MapEditorView mapEditorView;
    private ImageButton btnBlock, btnRedzone, btnGravizone, btnRemove, btnPlayer, btnWin, btnTest, btnBack;
    private ImageButton[] buttons;
    private boolean contentView;
    

    private void clearButtonsSelected(ImageButton selectButton){
        for (ImageButton btn : buttons){
            btn.setSelected(false);
        }
        selectButton.setSelected(true);
    }

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
        contentView = false;

        initLayout();

//        mapEditorView = findViewById(R.id.mapEditorView);

    }

    @Override
    protected void onStop(){
        super.onStop();


        GamePanel.setContext(MainActivity.getGameContext());
        MainActivity.getGamePanel().setHolder();
    }

    public void testLvl(Map map){
        GamePanel gamePanel = new GamePanel(this);
        Playing.getMapManager().setCurrentOnlineMap(map,-1);
        gamePanel.getGame().setCurrentGameState(Game.GameState.PLAYING);
        gamePanel.getGame().setTemp(10);
        setContentView(gamePanel);
        contentView = true;
    }

    public void returnToEditor(boolean win){
        runOnUiThread(() -> {
            setContentView(R.layout.activity_map_editor);
            MainActivity.getGamePanel().setGame(MainActivity.getGame());
            contentView = false;
            if (win){
                java.util.Map<String, Boolean> maps = loadVerifications();
                maps.put(getIntent().getStringExtra("name"), true);
                saveVerifications(maps);
            }
            initLayout();
        });
    }

    private void initLayout(){
        btnBlock = findViewById(R.id.btnSolid);
        btnRedzone = findViewById(R.id.btnRedZone);
        btnGravizone = findViewById(R.id.btnGravizone);
        btnRemove = findViewById(R.id.btnRemove);
        btnPlayer = findViewById(R.id.btnPlayer);
        btnWin = findViewById(R.id.btnWin);
        buttons = new ImageButton[]{btnBlock, btnRedzone, btnGravizone, btnRemove, btnPlayer, btnWin};
        btnBlock.setSelected(true);

//        String mapString = getIntent().getStringExtra("map");
//        TileSimplified[][] map = MapLoader.loadSimplifiedMapFromString(mapString);
        String mapName = getIntent().getStringExtra("name");



        // Создаём вручную MapEditorView
        mapEditorView = new MapEditorView(this, null, mapName);

        // Устанавливаем параметры
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        // Добавляем его в root
        FrameLayout root = findViewById(R.id.mapEditorRoot);
        root.addView(mapEditorView, 0, params);


        btnTest = findViewById(R.id.btnTest);
        btnBack = findViewById(R.id.btnBack);

        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('1');
                clearButtonsSelected((ImageButton) v);
            }
        });

        btnRedzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('R');
                clearButtonsSelected((ImageButton) v);

            }
        });

        btnGravizone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('G');
                clearButtonsSelected((ImageButton) v);

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('0');
                clearButtonsSelected((ImageButton) v);

            }
        });

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('P');
                clearButtonsSelected((ImageButton) v);

            }
        });

        btnWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.setSelectedBlockType('W');
                clearButtonsSelected((ImageButton) v);

            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLvl(mapEditorView.getMap(true));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorView.getMap(false);
                finish();
            }
        });
    }

    public boolean isContentView() {
        return contentView;
    }
}