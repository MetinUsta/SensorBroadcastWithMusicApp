package com.example.musicplayerv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class MovementStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("aa");
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int status = intent.getIntExtra("state", 0);
        if(status == 0){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            System.out.println("Status = " + status);
            MusicPlayerActivity.mediaPlayer.setVolume(0, 0);
        }else if(status == 1){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            System.out.println("Status = " + status);
            MusicPlayerActivity.mediaPlayer.setVolume(1, 1);
        }else if(status == 2){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            System.out.println("Status = " + status);
            MusicPlayerActivity.mediaPlayer.setVolume(0, 0);

        }
    }
}