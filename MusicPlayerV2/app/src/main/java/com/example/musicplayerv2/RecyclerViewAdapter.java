package com.example.musicplayerv2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv2.Model.AudioFile;
import com.example.musicplayerv2.Model.Playlist;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>{

    private Context rContext;
    private ArrayList<AudioFile> audioFiles;
    private int playlistIndex;

    RecyclerViewAdapter(Context rContext, int playlistIndex){
        this.playlistIndex = playlistIndex;
        this.audioFiles = MainActivity.playlists.get(playlistIndex).getSongs();
        this.rContext = rContext;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(rContext).inflate(R.layout.music_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.file_name.setText(audioFiles.get(position).getTitle());
        holder.artist_name.setText(audioFiles.get(position).getArtist());
        byte[] image = getAlbumArt(position);
        if(image != null){
            Glide.with(rContext).asBitmap().load(image).into(holder.album_art);
        }else{
            Glide.with(rContext).load(R.drawable.ic_baseline_music_note_24).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(rContext, MusicPlayerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("playlistIndex", playlistIndex);
                rContext.startActivity(intent);
            }
        });
        holder.option_menu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(rContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.music_menu_popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete_option:
                                deleteMusic(position, view);
                                break;
                            case R.id.share_option:
                                shareMusic(position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void shareMusic(int position) {
        String filePath = audioFiles.get(position).getPath();
        Uri uri = Uri.parse(filePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        rContext.startActivity(Intent.createChooser(share, "Share Music"));
    }

    private void deleteMusic(int position, View view){
        audioFiles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, audioFiles.size());
    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        TextView artist_name;
        ImageView album_art;
        ImageView option_menu;
        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            artist_name = itemView.findViewById(R.id.music_artist_name);
            album_art = itemView.findViewById(R.id.music_image);
            option_menu = itemView.findViewById(R.id.music_menu);
        }
    }

    private byte[] getAlbumArt(int position){
        return audioFiles.get(position).getArt();
    }
}
