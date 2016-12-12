package com.avinsharma.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Avin on 11-12-2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie>{


    public MovieAdapter(Context context, ArrayList<Movie> objects) {
        super(context,0, objects);
    }

    static class ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.movie_thumbnail_imageview);
            textView = (TextView) view.findViewById(R.id.movie_title_textview);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie currentMovie = getItem(position);

        Picasso.with(convertView.getContext()).load(currentMovie.getmImageUrl()).into(holder.imageView);
        holder.textView.setText(currentMovie.getmTitle());

        return convertView;
    }
}
