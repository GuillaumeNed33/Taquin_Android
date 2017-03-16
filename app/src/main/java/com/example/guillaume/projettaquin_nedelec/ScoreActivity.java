package com.example.guillaume.projettaquin_nedelec;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import static android.telephony.PhoneNumberUtils.compare;
import static com.example.guillaume.projettaquin_nedelec.R.id.score;

public class ScoreActivity extends ActionBarActivity {

    private ListView mListView3;
    private ListView mListView4;
    private ListView mListView5;
    private ListView mListView6;
    private TabHost mTabHost;
    private String playerName;
    private String scorePlayer;
    private int gridSize = 3;
    private SharedPreferences prefs;
    private static final String HIGH_SCORE = "high score";

    private final static String SCORE3X3_1 = "sc3_1";
    private final static String SCORE3X3_2 = "sc3_2";
    private final static String SCORE3X3_3 = "sc3_3";
    private final static String SCORE3X3_4 = "sc3_4";
    private final static String SCORE3X3_5 = "sc3_5";
    private  static ArrayList<String> score3 = new ArrayList<>();

    private final static String SCORE4X4_1 = "sc4_1";
    private final static String SCORE4X4_2 = "sc4_2";
    private final static String SCORE4X4_3 = "sc4_3";
    private final static String SCORE4X4_4 = "sc4_4";
    private final static String SCORE4X4_5 = "sc4_5";
    private  static ArrayList<String> score4 = new ArrayList<>();

    private final static String SCORE5X5_1 = "sc5_1";
    private final static String SCORE5X5_2 = "sc5_2";
    private final static String SCORE5X5_3 = "sc5_3";
    private final static String SCORE5X5_4 = "sc5_4";
    private final static String SCORE5X5_5 = "sc5_5";
    private  static ArrayList<String> score5 = new ArrayList<>();

    private final static String SCORE6X6_1 = "sc6_1";
    private final static String SCORE6X6_2 = "sc6_2";
    private final static String SCORE6X6_3 = "sc6_3";
    private final static String SCORE6X6_4 = "sc6_4";
    private final static String SCORE6X6_5 = "sc6_5";
    private  static ArrayList<String> score6 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        receiveData();
        loadScoreTab();

        mListView3 = (ListView) findViewById(R.id.listfor3);
        mListView4 = (ListView) findViewById(R.id.listfor4);
        mListView5 = (ListView) findViewById(R.id.listfor5);
        mListView6 = (ListView) findViewById(R.id.listfor6);
        afficherScores();

    }

    private void receiveData() {
        Intent intent = getIntent();
        scorePlayer = intent.getStringExtra(GameActivity.SCORE);
        playerName = intent.getStringExtra(GameActivity.PLAYER_NAME);
        gridSize = Integer.parseInt(intent.getStringExtra(GameActivity.GRID_SIZE));
        loadScoresData();

        if(!scorePlayer.equals("null")) {
            if (inBestScores()) {
                new AlertDialog.Builder(this).setTitle(R.string.app_name)
                        .setMessage(playerName + ", " + getString(R.string.highScore) + Integer.toString(gridSize) + "x" + Integer.toString(gridSize) + " !")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
                new AlertDialog.Builder(this).setTitle(R.string.app_name)
                        .setMessage(playerName + ", " + getString(R.string.highScoreFail))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }

    private void loadScoreTab() {
        mTabHost = (TabHost)findViewById(R.id.tabhost);
        mTabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = mTabHost.newTabSpec("tab3");
        spec.setContent(R.id.tab1);
        spec.setIndicator("3x3");
        mTabHost.addTab(spec);

        //Tab 2
        spec = mTabHost.newTabSpec("tab4");
        spec.setContent(R.id.tab2);
        spec.setIndicator("4x4");
        mTabHost.addTab(spec);

        //Tab 3
        spec = mTabHost.newTabSpec("tab5");
        spec.setContent(R.id.tab3);
        spec.setIndicator("5x5");
        mTabHost.addTab(spec);

        //Tab 4
        spec = mTabHost.newTabSpec("tab6");
        spec.setContent(R.id.tab4);
        spec.setIndicator("6x6");
        mTabHost.addTab(spec);
    }

    public boolean inBestScores() {
        prefs = getSharedPreferences(HIGH_SCORE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean done = false;

        if (scorePlayer != null) {
            int i = 0;
            String otherScore = null;
            switch (gridSize) {
                case 3:
                    otherScore = prefs.getString(score3.get(0), " , ,9999:59:59");
                    while (!isAvailable(scorePlayer, otherScore) && i < 5) {
                        i++;
                        if(i!=5)
                            otherScore = prefs.getString(score3.get(i)," , ,9999:59:59");
                    }
                    if(isAvailable(scorePlayer, otherScore)) {
                        ArrayList<String>save = new ArrayList<>();
                        int j = i;
                        boolean noMoreScore = false;
                        while (j<5 && !noMoreScore) {
                            otherScore = prefs.getString(score3.get(j),"null");
                            if(otherScore.equals("null"))
                                noMoreScore = true;
                            else {
                                String changeOrder = Integer.toString(j+2) + ", " + otherScore.split(",")[1] + "," +otherScore.split(",")[2];
                                save.add(new String(changeOrder));
                                j++;
                            }
                        }

                        String data = Integer.toString(i+1) + "," + playerName + "," + scorePlayer;
                        editor.putString(score3.get(i), data);

                        i++;
                        for(String s : save) {
                            if (!s.equals("null") && i<5) {
                                editor.putString(score3.get(i), s);
                                i++;
                            }
                        }
                        editor.commit();
                        done = true;
                    }
                    break;
                case 4:
                    otherScore = prefs.getString(score4.get(0), " , ,9999:59:59");
                    while (!isAvailable(scorePlayer, otherScore) && i <= 5) {
                        otherScore = prefs.getString(score4.get(i)," , ,9999:59:59");
                        i++;
                    }

                    if(isAvailable(scorePlayer, otherScore)) {
                        if(i==1)
                            i=0;
                        String data = Integer.toString(i+1) + "," + playerName + "," + scorePlayer;
                        editor.putString(score4.get(i), data);
                        editor.commit();
                        done = true;
                    }
                    break;
                case 5:
                    otherScore = prefs.getString(score5.get(0), " , ,9999:59:59");
                    while (!isAvailable(scorePlayer, otherScore) && i <= 5) {
                        otherScore = prefs.getString(score5.get(i)," , ,9999:59:59");
                        i++;
                    }

                    if(isAvailable(scorePlayer, otherScore)) {
                        if(i==1)
                            i=0;

                        String data = Integer.toString(i+1) + "," + playerName + "," + scorePlayer;
                        editor.putString(score5.get(i), data);
                        editor.commit();
                        done = true;
                    }
                    break;
                case 6:
                    otherScore = prefs.getString(score6.get(0), " , ,9999:59:59");
                    while (!isAvailable(scorePlayer, otherScore) && i <= 5) {
                        otherScore = prefs.getString(score6.get(i)," , ,9999:59:59");
                        i++;
                    }

                    if(isAvailable(scorePlayer, otherScore)) {
                        if(i==1)
                            i=0;

                        String data = Integer.toString(i+1) + "," + playerName + "," + scorePlayer;
                        editor.putString(score6.get(i), data);
                        editor.commit();
                        done = true;
                    }
                    break;
            }
        }
        return done;
    }

    private void loadScoresData() {
        score3.add(SCORE3X3_1);
        score3.add(SCORE3X3_2);
        score3.add(SCORE3X3_3);
        score3.add(SCORE3X3_4);
        score3.add(SCORE3X3_5);

        score4.add(SCORE4X4_1);
        score4.add(SCORE4X4_2);
        score4.add(SCORE4X4_3);
        score4.add(SCORE4X4_4);
        score4.add(SCORE4X4_5);

        score5.add(SCORE5X5_1);
        score5.add(SCORE5X5_2);
        score5.add(SCORE5X5_3);
        score5.add(SCORE5X5_4);
        score5.add(SCORE5X5_5);

        score6.add(SCORE6X6_1);
        score6.add(SCORE6X6_2);
        score6.add(SCORE6X6_3);
        score6.add(SCORE6X6_4);
        score6.add(SCORE6X6_5);
    }

    public boolean isAvailable(String score, String data) {
        int yourHr = 0;
        int yourMin =0;
        int yourSec =0;
        int hr = 0;
        int min =0;
        int sec= 0;

        //decoupage de notre score
        yourHr = Integer.parseInt(score.split(":")[0]);
        yourMin = Integer.parseInt(score.split(":")[1]);
        yourSec = Integer.parseInt(score.split(":")[2]);

        //decoupage meilleur score
        String best_score = data.split(",")[2];
        hr = Integer.parseInt(best_score.split(":")[0]);
        min = Integer.parseInt(best_score.split(":")[1]);
        sec = Integer.parseInt(best_score.split(":")[2]);

        boolean reponse;
        if(yourHr > hr)
            reponse = false;
        else if (yourHr < hr)
            reponse = true;
        else {
            if(yourMin > min)
                reponse = false;
            else if (yourMin < min)
                reponse = true;
            else {
                if(yourSec >= sec)
                    reponse = false;
                else
                    reponse= true;
            }
        }
        return reponse;
    }

    private void afficherScores(){
        List<Score> score3x3 = genererScore(3);
        List<Score> score4x4 = genererScore(4);
        List<Score> score5x5 = genererScore(5);
        List<Score> score6x6 = genererScore(6);

        ScoreAdapter adapter = new ScoreAdapter(ScoreActivity.this, score3x3);
        ScoreAdapter adapter2 = new ScoreAdapter(ScoreActivity.this, score4x4);
        ScoreAdapter adapter3 = new ScoreAdapter(ScoreActivity.this, score5x5);
        ScoreAdapter adapter4 = new ScoreAdapter(ScoreActivity.this, score6x6);

        mListView3.setAdapter(adapter);
        mListView4.setAdapter(adapter2);
        mListView5.setAdapter(adapter3);
        mListView6.setAdapter(adapter4);
    }

    private List<Score> genererScore(int id) {
        List<Score> scores = new ArrayList<Score>();
        prefs = getSharedPreferences(HIGH_SCORE, Context.MODE_PRIVATE);

        switch (id) {
            case 3:
                for (int i = 0; i < 5; i++) {
                    String data = prefs.getString(score3.get(i), Integer.toString(i+1) +", , ");
                    String position = data.split(",")[0];
                    String name = data.split(",")[1];
                    String scor = data.split(",")[2];
                    scores.add(new Score(position, name, scor));
                }
                break;
            case 4:
                for (int i = 0; i < 5; i++) {
                    String data = prefs.getString(score4.get(i), Integer.toString(i+1) +", , ");
                    String position = data.split(",")[0];
                    String name = data.split(",")[1];
                    String scor = data.split(",")[2];
                    scores.add(new Score(position, name, scor));
                }
                break;
            case 5:
                for (int i = 0; i < 5; i++) {
                    String data = prefs.getString(score5.get(i), Integer.toString(i+1) +", , ");
                    String position = data.split(",")[0];
                    String name = data.split(",")[1];
                    String scor = data.split(",")[2];
                    scores.add(new Score(position, name, scor));
                }
                break;
            case 6:
                for (int i = 0; i < 5; i++) {
                    String data = prefs.getString(score6.get(i), Integer.toString(i+1) +", , ");
                    String position = data.split(",")[0];
                    String name = data.split(",")[1];
                    String scor = data.split(",")[2];
                    scores.add(new Score(position, name, scor));
                }
                break;
        }
        return scores;
    }

    public void backToMenu(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
