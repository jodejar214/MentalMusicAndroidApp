package ubicomp.pac.cornell.cs.edu.mentalmusic2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class InitialChoose extends AppCompatActivity {
    private TextView welcome, instruct;
    private Button create, play;
    private Intent choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        welcome = (TextView) findViewById(R.id.welcome);
        instruct = (TextView) findViewById(R.id.instruct);
        create = (Button) findViewById(R.id.create);
        play = (Button) findViewById(R.id.play);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = new Intent(InitialChoose.this,SongCategorization.class);
                startActivity(choose);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose = new Intent(InitialChoose.this,MainActivity.class);
                choose.putExtra("Option", "default_playlists");
                startActivity(choose);
            }
        });

    }

}
