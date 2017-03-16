package com.example.guillaume.projettaquin_nedelec;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Guillaume on 28/02/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int mId = 0;

    public ImageAdapter(Context c, int id) {
        mContext = c;
        mId = id;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((int)((metrics.widthPixels/3)-((metrics.widthPixels/3)*0.2)), (int)((metrics.widthPixels/3)-((metrics.widthPixels/3)*0.2))));
            imageView.setMaxWidth(250);
            imageView.setMaxHeight(250);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(10, 10, 10, 10);

            if(position == mId)
                imageView.setBackgroundColor(Color.BLUE);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ac,
            R.drawable.ac1,
            R.drawable.ac3,
            R.drawable.ac_l,
            R.drawable.ac4,
            R.drawable.ac_r,
            R.drawable.ac_u,
            R.drawable.ac_s,
            R.drawable.ac_abstergo
    };
}
