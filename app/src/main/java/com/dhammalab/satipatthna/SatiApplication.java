package com.dhammalab.satipatthna;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.crittercism.app.Crittercism;
import com.parse.Parse;
import com.dhammalab.satipatthna.domain.Meditations;
import com.dhammalab.satipatthna.repository.Settings;

/**
 * Created by Tretyakov on 15.07.2014.
 */
public class SatiApplication extends Application {

    private static SatiApplication application;

    private Settings settings;

    private MediaPlayer mediaPlayer;

    public static SatiApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Crittercism.initialize(getApplicationContext(), "53c54614466eda2cc200000d");
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_application_id))
                .server(getString(R.string.parse_server_url))
                .build()
        );
        // Parse.initialize(getApplicationContext(), getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        application = this;
        settings = new Settings(this);
        Meditations meditations = settings.getMeditations();
        if (meditations == null) {
            meditations = new Meditations();
            settings.setMeditations(meditations);
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.meditation_bell);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setOnCompletionListener(audioCompleteListener);
    }

    private MediaPlayer.OnCompletionListener audioCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.stop();
            mp.release();
            mediaPlayer = MediaPlayer.create(application, R.raw.meditation_bell);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(1, 1);
            mediaPlayer.setOnCompletionListener(this);
        }
    };

    public Settings getSettings() {
        return settings;
    }

    public void playBell() {
        mediaPlayer.start();
    }

    public void stopBell() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(application, R.raw.meditation_bell);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setOnCompletionListener(audioCompleteListener);
    }
}
