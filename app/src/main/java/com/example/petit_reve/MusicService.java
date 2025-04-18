package com.example.petit_reve;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialisez le MediaPlayer avec une musique par défaut
        mediaPlayer = MediaPlayer.create(this, R.raw.musique_fond);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Vérifiez si une musique spécifique est demandée
        if (intent != null && intent.hasExtra("MUSIC_FILE")) {
            int musicResId = intent.getIntExtra("MUSIC_FILE", R.raw.musique_fond);
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying() && mediaPlayer.getAudioSessionId() == musicResId) {
                    // Si la musique demandée est déjà en cours, ne rien faire
                    return START_STICKY;
                }
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, musicResId);
            mediaPlayer.setLooping(true);
        }

        // Démarrez la musique si elle n'est pas déjà en cours
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}