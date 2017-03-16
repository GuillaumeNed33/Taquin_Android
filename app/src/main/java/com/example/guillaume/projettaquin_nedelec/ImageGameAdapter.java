package com.example.guillaume.projettaquin_nedelec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Guillaume on 28/02/2017.
 */

public class ImageGameAdapter extends BaseAdapter {

    private Context mContext;
    private Bitmap img ;
    private Bitmap unlockImg ;
    private Bitmap hiddenImg ;
    private Bitmap[] mThumbIds  ;
    private int[] position = {-1 , -1 , -1 , -1 } ;
    private int width ;
    private int height ;
    private int rannul =  0 ; //id de la case a supprimé
    private ArrayList<Integer> listeInts = new ArrayList<Integer>(); //liste ordre grille
    private ArrayList<Integer> successInts = new ArrayList<Integer>(); //liste ordre grille réussie
    private int prevRannul=0;

    public ImageGameAdapter(Context c , int w , int h, int id ){

        mContext = c;
        this.width  = w ;
        this.height = h ;

        //  boucle pour générer des valeurs aléatoire
        for(int i=0;i< ( width * height ) ;i++)
        {
            this.listeInts.add(i);
            this.successInts.add(i);
        }
        shuffling();

        selectImage(c, id);
        initGrille();
    }

    public ImageGameAdapter(Context c , int w , int h, int id, ArrayList<Integer> order){

        mContext = c;
        this.width  = w ;
        this.height = h ;
        listeInts = order;

        selectImage(c, id);
        initGrille();
    }

    private void initGrille() {
        mThumbIds = new Bitmap[ width * height ]; //grille

        // calcule des dimensions des images
        int partWidth  = img.getWidth() / width ;
        int partHeight = img.getHeight() / height ;

        // hidden image
        Bitmap tmpBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.lock);
        unlockImg =  Bitmap.createScaledBitmap( tmpBitmap , partWidth, partHeight, true);

        // decoupage de la bitmap
        int r = 0 ;
        for (int i = 0 ; i < width ; i++){
            for ( int j = 0 ; j < height ; j++){
                Bitmap unBout = Bitmap.createBitmap(img,  partHeight   *  j   ,  partWidth  * i   , partWidth , partHeight );
                mThumbIds[listeInts.indexOf(r)] = unBout ;
                r++ ;
            }
        }
        r--;
        // mettre l'image cachée à null
        rannul = listeInts.indexOf(r);
        hiddenImg = mThumbIds[listeInts.indexOf(r)] ;
        mThumbIds[listeInts.indexOf(r)] = unlockImg ;
    }

    private void shuffling() {
        for(int i=0; i<10000; i++) { //Realise 10000 deplacements aléatoire avec la case vide
            int size = width * height;
            int pos = listeInts.indexOf(size-1); // on récupère la position de la case vide

            int[] possibility = new int[4];
            int positionUp = 0;
            int positionDown = 0;
            int positionLeft = 0;
            int positionRight = 0;

            // calcule des position left & right
            positionLeft = pos - 1;
            positionRight = pos + 1;
            if (positionLeft >= 0 && (pos % width) != 0)
                possibility[0] = positionLeft;
            else
                possibility[0] = -1;

            if (positionRight < (height * width) && ((pos + 1) % width) != 0)
                possibility[1] = positionRight;
            else
                possibility[1] = -1;

            // calcule des position up & down
            positionUp = pos - height;
            positionDown = pos + height;
            if (positionUp >= 0)
                possibility[2] = positionUp;
            else
                possibility[2] = -1;

            if (positionDown < (height * width))
                possibility[3] = positionDown;
            else
                possibility[3] = -1;

            int j=0;
            ArrayList<Integer>moveChoice = new ArrayList<>();
            moveChoice.clear();
            while (j<4) {
                if(possibility[j] != -1)
                {
                    moveChoice.add(possibility[j]);
                }
                j++;
            }
            Collections.shuffle(moveChoice);
            int random = (int) (Math.random() * moveChoice.size()); //possibilités de déplacement (4 au max : haut bas gauche droite)
            switchPosition(pos, moveChoice.get(random));
        }
    }

    private void switchPosition(int currentPosition, int i) {
        if (i != -1) {
            int tmp = listeInts.get(currentPosition);
            listeInts.set(currentPosition, listeInts.get(i));
            listeInts.set(i, tmp);
        }
    }

    private void selectImage(Context c, int id) {
        switch (id) {
            case 1:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac);
                break;
            case 2:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac1);
                break;
            case 3:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac3);
                break;
            case 4:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac_l);
                break;
            case 5:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac4);
                break;
            case 6:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac_r);
                break;
            case 7:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac_u);
                break;
            case 8:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac_s);
                break;
            case 9:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac_abstergo);
                break;
            default:
                img = BitmapFactory.decodeResource(c.getResources(), R.drawable.ac);
        }
        //image case vide
        unlockImg = BitmapFactory.decodeResource(c.getResources(), R.drawable.lock);
    }

    public boolean move( int pos){
        int  Indice = 0;
        boolean trouver = false;
        int positionUp = 0;
        int positionDown = 0;
        int positionLeft = 0;
        int positionRight = 0;

        // calcule des position left & right
        positionLeft = pos - 1;
        positionRight = pos + 1;
        if (positionLeft >= 0 && (pos % width) != 0)
            position[0] = positionLeft;
        else
            position[0] = -1;

        if (positionRight < (height * width) && ((pos + 1) % width) != 0)
            position[1] = positionRight;
        else
            position[1] = -1;

        // calcule des position up & down
        positionUp = pos - height;
        positionDown = pos + height;
        if (positionUp >= 0)
            position[2] = positionUp;
        else
            position[2] = -1;

        if (positionDown < (height * width))
            position[3] = positionDown;
        else
            position[3] = -1;


        for (int i = 0; i < position.length; i++) {
            if (position[i] == rannul) {
                trouver = true;
                Indice = position[i];
                break;
            }
        }
        if (trouver) {
            prevRannul = rannul;
            rannul = pos;
            //Changement des images
            mThumbIds[Indice] = mThumbIds[pos];
            mThumbIds[pos] = unlockImg;

            //Restructuration de la liste
            int tmp = listeInts.get(pos);
            listeInts.set(pos, listeInts.get(Indice));
            listeInts.set(Indice, tmp);

        }
        return trouver;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

            imageView = new ImageView(mContext);
            int size = metrics.widthPixels;

            imageView.setLayoutParams(new GridView.LayoutParams((size / width), (size / height)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mThumbIds[position]);

        return imageView;
    }


    public boolean finished()
    {
        return listeInts.equals(successInts);
    }

    public void displayPick()
    {
        mThumbIds[(width*height)-1] = hiddenImg ;
    }

    public Animation getAnimation(int position, boolean b) {
        int coordForAnimation_x = 0;
        int coordForAnimation_y = 0;

        if(b) {
            if(this.position[0]==prevRannul)
                coordForAnimation_x--;
            else if (this.position[1]==prevRannul)
                coordForAnimation_x++;
            else if (this.position[2]==prevRannul)
                coordForAnimation_y--;
            else if (this.position[3]==prevRannul)
                coordForAnimation_y++;
        }
        else {
            if(this.position[0]==prevRannul)
                coordForAnimation_x++;
            else if (this.position[1]==prevRannul)
                coordForAnimation_x--;
            else if (this.position[2]==prevRannul)
                coordForAnimation_y++;
            else if (this.position[3]==prevRannul)
                coordForAnimation_y--;
        }

        if (coordForAnimation_x != 0 || coordForAnimation_y != 0) {
            return new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                    TranslateAnimation.RELATIVE_TO_SELF, coordForAnimation_x, TranslateAnimation.RELATIVE_TO_SELF, 0,
                    TranslateAnimation.RELATIVE_TO_SELF, coordForAnimation_y);
        }
        else {
            return null;
        }
    }

    public int getEmptyPos() {
        return prevRannul;
    }

    public void resolve() {
        int partWidth  = img.getWidth() / width ;
        int partHeight = img.getHeight() / height ;
        int r=0;
        for (int i = 0 ; i < width ; i++){
            for ( int j = 0 ; j < height ; j++){
                Bitmap unBout = Bitmap.createBitmap(img,  partHeight   *  j   ,  partWidth  * i   , partWidth , partHeight );
                mThumbIds[successInts.get(r)] = unBout ;
                r++ ;
            }
        }
    }

    public ArrayList<Integer> getOrder() {
        return listeInts;
    }

}

