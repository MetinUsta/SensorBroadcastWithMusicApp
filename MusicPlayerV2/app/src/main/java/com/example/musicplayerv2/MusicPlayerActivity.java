package com.example.musicplayerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayerv2.Model.AudioFile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    int playlistIndex = 0;
    static ArrayList<AudioFile> musicFiles = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Thread playThread, prevThread, nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initViews();
        getIntentMethod();
        song_name.setText(musicFiles.get(position).getTitle());
        artist_name.setText(musicFiles.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    duration_played.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position - 1) < 0 ? (musicFiles.size() - 1) : (position - 1);
            uri = Uri.parse(musicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(musicFiles.get(position).getTitle());
            artist_name.setText(musicFiles.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration());

            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position +1 ) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(musicFiles.get(position).getTitle());
            artist_name.setText(musicFiles.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration());

            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            System.out.println("playing");
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position +1 ) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(musicFiles.get(position).getTitle());
            artist_name.setText(musicFiles.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration());

            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            System.out.println("not playing");
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position +1 ) % musicFiles.size());
            uri = Uri.parse(musicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(musicFiles.get(position).getTitle());
            artist_name.setText(musicFiles.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration());

            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.start();
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration());

            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }else{
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private String formattedTime(int currentPosition) {
        String minutes = String.format("%02d", currentPosition / 1000 / 60);
        String seconds = String.format("%02d", (int)((currentPosition / 1000)%60));
        return minutes + ":" + seconds;
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        playlistIndex = getIntent().getIntExtra("playlistIndex", 0);
        musicFiles = MainActivity.playlists.get(playlistIndex).getSongs();
        if(musicFiles != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(musicFiles.get(position).getPath());
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration());
        metaData(uri);

    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPassed);
        duration_total = findViewById(R.id.durationTotal);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.next_button);
        prevBtn = findViewById(R.id.prev_button);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBar);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(musicFiles.get(position).getDuration());
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = musicFiles.get(position).getArt();
        if(art != null){
            Glide.with(this).asBitmap().load(art).into(cover_art);
        }else{
            Glide.with(this).load(R.drawable.ic_baseline_music_note_24).into(cover_art);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextBtnClicked();
    }
}