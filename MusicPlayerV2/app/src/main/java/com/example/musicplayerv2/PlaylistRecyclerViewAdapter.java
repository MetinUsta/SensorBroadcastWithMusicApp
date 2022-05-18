package com.example.musicplayerv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv2.Model.AudioFile;

import java.util.ArrayList;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.PlaylistCustomViewHolder>{

    private Context rContext;
    private ArrayList<AudioFile> audioFiles;
    private ArrayList<AudioFile> audioFileCopies;

    PlaylistRecyclerViewAdapter(Context rContext, ArrayList<AudioFile> audioFiles){
        this.audioFiles = audioFiles;
        this.rContext = rContext;
    }

    @NonNull
    @Override
    public PlaylistCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(rContext).inflate(R.layout.playlist_music_item, parent, false);
        return new PlaylistCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistCustomViewHolder holder, int position) {
        holder.file_name.setText(audioFiles.get(position).getTitle());
        holder.artist_name.setText(audioFiles.get(position).getArtist());
        byte[] image = getAlbumArt(position);
        if(image != null){
            Glide.with(rContext).asBitmap().load(image).into(holder.album_art);
        }else{
            Glide.with(rContext).load(R.drawable.ic_baseline_music_note_24).into(holder.album_art);
        }
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(audioFiles.get(position).isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                audioFiles.get(position).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    public ArrayList<AudioFile> getAudioFileCopies(){
        audioFileCopies = new ArrayList<>();
        for(AudioFile audioFile : audioFiles){
            if(audioFile.isSelected()){
                audioFileCopies.add(audioFile);
            }
        }
        return audioFileCopies;
    }

    public class PlaylistCustomViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        TextView artist_name;
        ImageView album_art;
        CheckBox checkBox;
        public PlaylistCustomViewHolder(@NonNull View itemView){
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            artist_name = itemView.findViewById(R.id.music_artist_name);
            album_art = itemView.findViewById(R.id.music_image);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    private byte[] getAlbumArt(int position){
        return audioFiles.get(position).getArt();
    }
}
