package com.example.guillaume.projettaquin_nedelec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Guillaume on 04/03/2017.
 */

public class ScoreAdapter extends ArrayAdapter<Score> {

    public ScoreAdapter(Context context, List<Score> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_row,parent, false);
        }

        ScoreViewHolder viewHolder = (ScoreViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ScoreViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.score = (TextView) convertView.findViewById(R.id.score);
            viewHolder.pos = (TextView) convertView.findViewById(R.id.pos);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Score score = getItem(position);
        viewHolder.name.setText(score.getName());
        viewHolder.score.setText(score.getTime());
        viewHolder.pos.setText(score.getPosition());

        return convertView;
    }

    private class ScoreViewHolder{
        public TextView name;
        public TextView score;
        public TextView pos;

    }
}