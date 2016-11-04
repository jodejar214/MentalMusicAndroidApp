package ubicomp.pac.cornell.cs.edu.mentalmusic2;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SongCategorization extends AppCompatActivity {
    private TextView valence_text, arousal_text, song_info;
    private Button next, done;
    private Spinner valence, arousal;
    private Intent selectionDone;
    private String musicTitle, musicArtist, musicAlbum;
    private MediaMetadataRetriever mmr;
    private AssetFileDescriptor descriptor;
    private String[] musicFiles;
    private int track = 0;
    private HashMap<String, ArrayList<String>> playlists;
    private ArrayList<String> angry, annoyed, bored, calm, content, depressed, excited, happy, joyful, relaxed, sad, scared, stressed, tired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_categorization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        valence_text = (TextView) findViewById(R.id.valence_text);
        arousal_text = (TextView) findViewById(R.id.arousal_text);
        song_info = (TextView) findViewById(R.id.song_info);
        next = (Button) findViewById(R.id.next);
        done = (Button) findViewById(R.id.done);
        valence = (Spinner) findViewById(R.id.valence_spinner);
        arousal = (Spinner) findViewById(R.id.arousal_spinner);
        playlists = new HashMap<String, ArrayList<String>>();
        angry = new ArrayList<String>();
        annoyed = new ArrayList<String>();
        bored = new ArrayList<String>();
        calm = new ArrayList<String>();
        content = new ArrayList<String>();
        depressed = new ArrayList<String>();
        excited = new ArrayList<String>();
        happy = new ArrayList<String>();
        joyful = new ArrayList<String>();
        relaxed = new ArrayList<String>();
        sad = new ArrayList<String>();
        scared = new ArrayList<String>();
        stressed = new ArrayList<String>();
        tired = new ArrayList<String>();

        try{
            musicFiles = this.getAssets().list("songs");
        } catch (IOException e) {
            e.printStackTrace();
        }

        getInfo();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = valence.getSelectedItem().toString();
                String aro = arousal.getSelectedItem().toString();
                if(val.equals("Yes") && aro.equals("Yes")){
                    joyful.add(musicFiles[track]);
                    happy.add(musicFiles[track]);
                    excited.add(musicFiles[track]);
                }
                else if(val.equals("Yes") && aro.equals("No")){
                    content.add(musicFiles[track]);
                    calm.add(musicFiles[track]);
                    relaxed.add(musicFiles[track]);
                }
                else if(val.equals("No") && aro.equals("Yes")){
                    angry.add(musicFiles[track]);
                    annoyed.add(musicFiles[track]);
                    stressed.add(musicFiles[track]);
                    scared.add(musicFiles[track]);
                }
                else{
                    tired.add(musicFiles[track]);
                    bored.add(musicFiles[track]);
                    depressed.add(musicFiles[track]);
                    sad.add(musicFiles[track]);
                }

                track++;
                if(track == musicFiles.length){
                    playlists.put("Angry", angry);
                    playlists.put("Annoyed", annoyed);
                    playlists.put("Bored", bored);
                    playlists.put("Calm", calm);
                    playlists.put("Content", content);
                    playlists.put("Depressed", depressed);
                    playlists.put("Excited", excited);
                    playlists.put("Happy", happy);
                    playlists.put("Joyful", joyful);
                    playlists.put("Relaxed", relaxed);
                    playlists.put("Sad", sad);
                    playlists.put("Scared", scared);
                    playlists.put("Stressed", stressed);
                    playlists.put("Tired", tired);

                    selectionDone = new Intent(SongCategorization.this,MainActivity.class);
                    selectionDone.putExtra("Playlists",playlists);
                    selectionDone.putExtra("Option", "personal_playlists");
                    startActivity(selectionDone);
                }
                getInfo();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlists.put("Angry", angry);
                playlists.put("Annoyed", annoyed);
                playlists.put("Bored", bored);
                playlists.put("Calm", calm);
                playlists.put("Content", content);
                playlists.put("Depressed", depressed);
                playlists.put("Excited", excited);
                playlists.put("Happy", happy);
                playlists.put("Joyful", joyful);
                playlists.put("Relaxed", relaxed);
                playlists.put("Sad", sad);
                playlists.put("Scared", scared);
                playlists.put("Stressed", stressed);
                playlists.put("Tired", tired);

                selectionDone = new Intent(SongCategorization.this, MainActivity.class);
                selectionDone.putExtra("Playlists",playlists);
                selectionDone.putExtra("Option", "personal_playlists");
                startActivity(selectionDone);
            }
        });
    }

    private void getInfo(){
        try {
            descriptor = this.getAssets().openFd("songs/" + musicFiles[track]);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(descriptor.getFileDescriptor(), start, end);
            musicTitle = "Title: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicArtist = "Artist: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicAlbum = "Album: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            song_info.setText(musicTitle + "\n" + musicArtist + "\n" + musicAlbum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
