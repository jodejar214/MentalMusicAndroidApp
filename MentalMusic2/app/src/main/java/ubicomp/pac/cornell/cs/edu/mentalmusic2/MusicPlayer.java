package ubicomp.pac.cornell.cs.edu.mentalmusic2;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer extends AppCompatActivity {

    private Intent data, changeEmotion, sndSurvey;
    private String emotion_data, pulse_data, pos_data;
    private TextView emotion, pulse, musicTitle, musicArtist, musicAlbum;
    private Button play, pause, prev, next, change;
    private String[] musicFiles;
    private AssetFileDescriptor descriptor;
    private MediaPlayer mPlayer;
    private MediaMetadataRetriever mmr;
    private Timer timer;
    private int trackNum = 0;
    private HashMap<String, ArrayList<String>> playlists;
    private String opt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = getIntent();
        emotion_data = data.getStringExtra("Init_Emotion");
        pulse_data = data.getStringExtra("Init_Pulse");
        pos_data = data.getStringExtra("Emotion_Want");
        onBackPressed();
        emotion = (TextView) findViewById(R.id.emotions);
        emotion.setText("Emotion: " + emotion_data);
        pulse = (TextView) findViewById(R.id.pulse);
        pulse.setText("Heart Rate: " + pulse_data);
        musicTitle = (TextView) findViewById(R.id.musicTitle);
        musicArtist = (TextView) findViewById(R.id.musicArtist);
        musicAlbum = (TextView) findViewById(R.id.musicAlbum);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        change = (Button) findViewById(R.id.change);
        opt = data.getStringExtra("Option");
        playlists = (HashMap<String, ArrayList<String>>) data.getSerializableExtra("Playlists");

        try{
            if(opt.equals("default_playlists")){
                musicFiles = this.getAssets().list("default_playlists/" + emotion_data);
            }
            else {
                ArrayList<String> pdata = new ArrayList<String>(playlists.get(emotion_data));
                musicFiles = new String[pdata.size()];
                for(int i = 0; i < pdata.size(); i++){
                    musicFiles[i] = pdata.get(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        playTrack();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                changeEmotion = new Intent(MusicPlayer.this, MainActivity.class);
                startActivity(changeEmotion);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackNum = trackNum-1;
                if(trackNum < 0){
                    trackNum = musicFiles.length - 1;
                }
                mmr.release();
                mPlayer.release();
                playTrack();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackNum = trackNum+1;
                if(trackNum >= musicFiles.length){
                    trackNum = 0;
                }
                mmr.release();
                mPlayer.release();
                playTrack();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                trackNum = trackNum+1;
                if(trackNum >= musicFiles.length){
                    trackNum = 0;
                }
                mmr.release();
                mp.reset();
                playTrack();
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sndSurvey = new Intent(MusicPlayer.this,Survey2.class);
                sndSurvey.putExtra("Init_Emotion", emotion_data);
                sndSurvey.putExtra("Init_Pulse", pulse_data);
                sndSurvey.putExtra("Emotion_Want", pos_data);
                sndSurvey.putExtra("Option", opt);
                startActivity(sndSurvey);
            }
        }, 60000);
    }

    private void playTrack(){
        try {
            if(opt == "default_playlists"){
                descriptor = this.getAssets().openFd("default_playlists/" + emotion_data + "/" + musicFiles[trackNum]);
            }
            else {
                descriptor = this.getAssets().openFd("songs/" + musicFiles[trackNum]);
            }
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mPlayer.prepare();
            mPlayer.start();

            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(descriptor.getFileDescriptor(), start, end);
            musicTitle.setText("Title: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            musicArtist.setText("Artist: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            musicAlbum.setText("Album: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
