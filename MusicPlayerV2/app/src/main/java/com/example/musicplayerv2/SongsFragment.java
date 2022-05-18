package com.example.musicplayerv2;

import static com.example.musicplayerv2.MainActivity.audioFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.musicplayerv2.Model.AudioFile;
import com.example.musicplayerv2.Model.Playlist;

import java.util.ArrayList;

public class SongsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<AudioFile> currPlaylist;
    private ArrayAdapter ad;
    public SongsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayList<String> playlist_titles = new ArrayList<>();
        for (Playlist playlist : MainActivity.playlists) {
            playlist_titles.add(playlist.getTitle());
        }
        ad = new ArrayAdapter(this.getContext(), R.layout.my_spinner_item, MainActivity.titles);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        recyclerView.setHasFixedSize(true);
        if (!(audioFiles.size() < 1)) {
            currPlaylist = audioFiles;
            recyclerViewAdapter = new RecyclerViewAdapter(getContext(), 0);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        ad.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view2, int i, long l) {
                String playlist_name = adapterView.getItemAtPosition(i).toString();
                int j = 0;
                while(j < MainActivity.playlists.size()){
                    if(MainActivity.playlists.get(j).getTitle().equals(playlist_name)){
                        currPlaylist = MainActivity.playlists.get(j).getSongs();
                        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), j);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                    j+=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    public ArrayAdapter getAd() {
        return ad;
    }
}