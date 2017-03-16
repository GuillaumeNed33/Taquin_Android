package com.example.guillaume.projettaquin_nedelec;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final int EXIT_CODE = 1;
    public static final String PLAYER_NAME = "name";
    public static final String SCORE = "score";
    public static final String GRID_SIZE = "grid";

    private int size;
    private int imgId;
    private String name;
    private GridView gridView;
    private ImageGameAdapter adapter;
    private Clock time;
    private String score;
    private float hr,min,sec, saveHr=0, saveMin=0,saveSec=0;
    private TextView chrono;

    private static MediaPlayer music = null;
    private static MediaPlayer achievement = null;

    @Override
    protected void onStop() {
        super.onStop();
        stopSong();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide(); //<< this

        chrono = (TextView)findViewById(R.id.chrono);

        if(savedInstanceState != null){
            restoreData(savedInstanceState);
        }
        else {
            recupData();
            selectSounds();

            music.start();
            music.setLooping(true);
            music.setVolume(0.5f,0.5f);

            startChrono();
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.infoToPlayer)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            chrono.setVisibility(View.VISIBLE);
                            time.restart();
                        }
                    })
                    .show();
            alert.setCanceledOnTouchOutside(false);
            loadGrid(false, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Integer>test = adapter.getOrder();
        outState.putIntegerArrayList("grid",test );
        outState.putFloat("hr", hr);
        outState.putFloat("min", min);
        outState.putFloat("sec", sec);

        outState.putInt("size", size);
        outState.putInt("imgId", imgId);
        outState.putString("name", name);

        outState.putInt("music", music.getCurrentPosition());
    }

    private void restoreData(Bundle savedInstanceState) {
        name = savedInstanceState.getString("name");
        size = savedInstanceState.getInt("size");
        imgId = savedInstanceState.getInt("imgId");

        saveHr = savedInstanceState.getFloat("hr");
        saveMin = savedInstanceState.getFloat("min");
        saveSec = savedInstanceState.getFloat("sec");

        time = new Clock();
        chrono.setVisibility(View.VISIBLE);
        score = new String(Integer.toString((int)hr) + ":" + Integer.toString((int)min) + ":" + Integer.toString((int)sec));
        chrono.setText(score);
        lauchChrono();
        selectSounds();

        music.seekTo(savedInstanceState.getInt("music"));
        music.start();
        loadGrid(true, (ArrayList<Integer>)savedInstanceState.get("grid"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        time.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        time.start();
    }

    private void startChrono() {
        time = new Clock();
        hr=0;
        min=0;
        sec=0;

        lauchChrono();
    }

    private void lauchChrono() {
        Thread myThread = new Thread()
        {
            public void run()
            {
                while (true) {
                    float tmp = time.milliseconds()/1000;
                    sec = (tmp + saveSec)%60;
                    min = ((tmp/60) + saveMin)%60;
                    hr = ((tmp/60) +saveHr)/60;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            score = new String(Integer.toString((int)hr) + ":" + Integer.toString((int)min) + ":" + Integer.toString((int)sec));
                            chrono.setText(score);
                        }
                    } );
                    try {
                        sleep(50);
                    }
                    catch (Exception e) {
                        stop();
                    }
                }
            }
        };
        myThread.start();
    }

    private void loadGrid(boolean restore, ArrayList<Integer> order) {
        gridView = (GridView) findViewById(R.id.gridview);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();

        if (size == 3 || size == 4 || size == 5 || size == 6) {
            if (!restore) {
                if (size == 3) {
                    adapter = new ImageGameAdapter(this, 3, 3, imgId);
                } else if (size == 4) {
                    adapter = new ImageGameAdapter(this, 4, 4, imgId);
                } else if (size == 5) {
                    adapter = new ImageGameAdapter(this, 5, 5, imgId);
                } else if (size == 6) {
                    adapter = new ImageGameAdapter(this, 6, 6, imgId);
                }
            }
            else {
                if (size == 3) {
                    adapter = new ImageGameAdapter(this, 3, 3, imgId, order);
                } else if (size == 4) {
                    adapter = new ImageGameAdapter(this, 4, 4, imgId, order);
                } else if (size == 5) {
                    adapter = new ImageGameAdapter(this, 5, 5, imgId, order);
                } else if (size == 6) {
                    adapter = new ImageGameAdapter(this, 6, 6, imgId, order);
                }
            }
            if(metrics.widthPixels <= metrics.heightPixels) {
                gridView.setColumnWidth((metrics.widthPixels / size) - 10);
                gridView.setNumColumns(size);
            }
            if(metrics.widthPixels > metrics.heightPixels) {
                gridView.setColumnWidth((metrics.heightPixels / size) - 10);
                gridView.setNumColumns(size);
            }
            gridView.setAdapter(adapter);
        }
        gridView.setOnItemClickListener(this);
    }

    private void recupData() {
        Intent intent = getIntent();
        size = Integer.parseInt(intent.getStringExtra(MenuActivity.GRID_CODE));
        imgId = Integer.parseInt(intent.getStringExtra(MenuActivity.IMAGE_SELECT));
        name = intent.getStringExtra(MenuActivity.PLAYER_NAME);
    }

    public void endGame() {
        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
        if(name.equals("") || name == null)
        {
            name = getString(R.string.defaultName);
        }
        intent.putExtra(PLAYER_NAME, name);
        intent.putExtra(GRID_SIZE, Integer.toString(size));
        intent.putExtra(SCORE, score);
        stopSong();
        startActivityForResult(intent, EXIT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        moving(position);
        if (adapter.finished()) {
            adapter.displayPick();
            gridView.setAdapter(adapter);
            music.stop();
            achievement.start();
            time.stop();
            AlertDialog alert = new AlertDialog.Builder(this).setTitle(R.string.app_name)
                    .setMessage(getString(R.string.victory) + name + " ! \n" + getString(R.string.victorySupp) + score + ".")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            endGame();
                        }
                    })
                    .show();
            alert.setCanceledOnTouchOutside(false);
        }
    }

    public void moving(int position) {
        if(adapter.move(position)) {
            Animation animation = adapter.getAnimation(position, true); //animation de l'image vers la case vide
            Animation animation2 = adapter.getAnimation(position, false); //animation de la case vide vers l'image
            if (animation != null) {
                ImageView viewCase = (ImageView) gridView.getChildAt(position); // On récupètre l'image sur laquelle faire l'animation
                animation.setDuration(500);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        gridView.invalidateViews();
                        gridView.setAdapter(adapter);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                viewCase.startAnimation(animation);
            }

            if (animation2 != null) {
                animation.setZAdjustment(Animation.ZORDER_BOTTOM);
                ImageView viewEmptyCase = (ImageView) gridView.getChildAt(adapter.getEmptyPos()); // On récupètre l'image sur laquelle faire l'animation
                animation2.setDuration(500);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        gridView.invalidateViews();
                        gridView.setAdapter(adapter);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                viewEmptyCase.startAnimation(animation2);
            }
        }
    }

    public void cancel(View v) {
        AlertDialog alert = new AlertDialog.Builder(this).setTitle(R.string.app_name)
                .setMessage(getString(R.string.quit))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopSong();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        alert.setCanceledOnTouchOutside(false);
    }

    public void solution(View v) {
        adapter.resolve();
        time.stop();
        adapter.displayPick();
        music.stop();
        achievement.start();
        gridView.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showSoluceDialog();
            }
        }, 2000);
    }

    public void showSoluceDialog() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(getString(R.string.soluce))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        score = "null";
                        endGame();
                    }
                })
                .show();
        alert.setCanceledOnTouchOutside(false);
    }

    private void selectSounds() {
        switch (imgId) {
            case 1:
                music = MediaPlayer.create(this, R.raw.ac);
                break;
            case 2:
                music = MediaPlayer.create(this, R.raw.ac1);
                break;
            case 3:
                music = MediaPlayer.create(this, R.raw.ac3);
                break;
            case 4:
                music = MediaPlayer.create(this, R.raw.ac_l);
                break;
            case 5:
                music = MediaPlayer.create(this, R.raw.ac4);
                break;
            case 6:
                music = MediaPlayer.create(this, R.raw.ac_r);
                break;
            case 7:
                music = MediaPlayer.create(this, R.raw.ac_u);
                break;
            case 8:
                music = MediaPlayer.create(this, R.raw.ac_s);
                break;
            case 9:
                music = MediaPlayer.create(this, R.raw.ac_abstergo);
                break;
        }
        achievement = MediaPlayer.create(this, R.raw.achieve);
    }

    public static void stopSong() {
        if(music.isPlaying())
            music.stop();
        if (achievement.isPlaying())
            achievement.stop();
    }
}
