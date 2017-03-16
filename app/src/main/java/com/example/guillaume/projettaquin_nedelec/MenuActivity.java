package com.example.guillaume.projettaquin_nedelec;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    public static final String GRID_CODE = "grid";
    public static final String PLAYER_NAME = "name";
    public static final String IMAGE_SELECT = "img";

    private int gridSize = 3;
    private int imageChoice = 1;
    private String playerName;
    private GridView mainMenu;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        name = (EditText) findViewById(R.id.playerNameedit);


        if(savedInstanceState != null){
            restoreData(savedInstanceState);
        }

        loadGridImage();
        setDynamicHeight(mainMenu);
    }

    private void restoreData(Bundle savedInstanceState) {
        gridSize = (int)savedInstanceState.get("size");
        name.setText(savedInstanceState.getString("name"));

        imageChoice = savedInstanceState.getInt("imgId");

        switch (gridSize) {
            case 3:
                checkRadio((RadioButton) findViewById(R.id.size3));
                break;
            case 4:
                checkRadio((RadioButton) findViewById(R.id.size4));
                break;
            case 5:
                checkRadio((RadioButton) findViewById(R.id.size5));
                break;
            case 6:
                checkRadio((RadioButton) findViewById(R.id.size6));
                break;
        }

    }

    private void loadGridImage() {
        mainMenu = (GridView) findViewById(R.id.menuSelect);
        mainMenu.setAdapter(new ImageAdapter(this, imageChoice-1));
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        mainMenu.setColumnWidth(metrics.widthPixels / 3 );

        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                for (int i = 0; i < mainMenu.getChildCount(); i++) {
                    mainMenu.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                imageChoice = position + 1;
                mainMenu.getChildAt(position).setBackgroundColor(Color.BLUE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("size", gridSize);
        outState.putInt("imgId", imageChoice);
        outState.putString("name", name.getText().toString());
    }

    public void checkRadio(View v) {
        RadioButton tmp;
        RadioGroup group = (RadioGroup) findViewById(R.id.radioButtons);
        group.clearCheck();
        ArrayList<RadioButton> disabled = new ArrayList<>();
        disabled.add((RadioButton) findViewById(R.id.size3));
        disabled.add((RadioButton) findViewById(R.id.size4));
        disabled.add((RadioButton) findViewById(R.id.size5));
        disabled.add((RadioButton) findViewById(R.id.size6));

        switch (v.getId()) {
            case R.id.size3:
                tmp = (RadioButton) findViewById(R.id.size3);
                gridSize = 3;
                break;
            case R.id.size4:
                tmp = (RadioButton) findViewById(R.id.size4);
                gridSize = 4;
                break;
            case R.id.size5:
                tmp = (RadioButton) findViewById(R.id.size5);
                gridSize = 5;
                break;
            case R.id.size6:
                tmp = (RadioButton) findViewById(R.id.size6);
                gridSize = 6;
                break;
            default:
                tmp = (RadioButton) findViewById(R.id.size3);
                gridSize = 3;
        }
        for (RadioButton b : disabled) {
            b.setChecked(false);
        }
        tmp.setChecked(true);
    }

    public void startGame(View v) {
        playerName = name.getText().toString();
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra(GRID_CODE, Integer.toString(gridSize));
        intent.putExtra(IMAGE_SELECT, Integer.toString(imageChoice));
        intent.putExtra(PLAYER_NAME, playerName);
        startActivity(intent);
    }

    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);

        float x = 1;
        int nb_colums = 3;
        rows = items/nb_colums;
        totalHeight = (listItem.getMeasuredHeight());

        totalHeight *=rows;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}
