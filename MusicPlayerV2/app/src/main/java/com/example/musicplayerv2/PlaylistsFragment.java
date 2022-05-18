package com.example.musicplayerv2;

import static com.example.musicplayerv2.MainActivity.audioFiles;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayerv2.Model.AudioFile;
import com.example.musicplayerv2.Model.Playlist;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {
    RecyclerView recyclerView;
    PlaylistRecyclerViewAdapter playlistRecyclerViewAdapter;
    private ImageView saveButton;
    private TextInputLayout playlist_textbox;
    private TextInputEditText playlist_text;
    private Context pContext;

    public PlaylistsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pContext = this.getContext();
        FragmentManager fm = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        saveButton = view.findViewById(R.id.save_button);
        playlist_textbox = view.findViewById(R.id.playlist_input_box);
        playlist_text = view.findViewById(R.id.nameTextBox);
        recyclerView.setHasFixedSize(true);
        if(!(audioFiles.size() < 1)){
            playlistRecyclerViewAdapter = new PlaylistRecyclerViewAdapter(getContext(), audioFiles);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(playlistRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AudioFile> songs = playlistRecyclerViewAdapter.getAudioFileCopies();
                String title = playlist_text.getText().toString();
                playlist_text.setText("");
                Toast.makeText(pContext, "Playlist Created!", Toast.LENGTH_SHORT).show();
                MainActivity.playlists.add(new Playlist(songs, title));
                MainActivity.titles.add(title);
                SongsFragment f = (SongsFragment) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
                f.getAd().notifyDataSetChanged();
                int a = f.getAd().getCount();
                System.out.println("Item count:" + a);
            }
        });

        return view;
    }
}