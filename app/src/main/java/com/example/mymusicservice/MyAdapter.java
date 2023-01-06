package com.example.mymusicservice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Object> {
    private final Context context;
    private final ArrayList<Song> songs;

    public MyAdapter(Context context, ArrayList<Song> songs) {
        super(context, R.layout.row);
        this.context = context;
        this.songs = songs;

        addAll(new Object[songs.size()]);
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row, parent, false);
        }

        Song song = songs.get(pos);

        // set the image for ImageView
        TextView titleView = view.findViewById(R.id.song_title);
        titleView.setText(song.getTitle());

        // set the text for TextView
        TextView descView = view.findViewById(R.id.song_desc);
        descView.setText(song.getDesc());

        return view;
    }
}

