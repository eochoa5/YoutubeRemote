package com.example.edwin.youtuberemote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ControllerActivity extends AppCompatActivity {
    private String key;
    private String videoUrl;
    public static final String PREFERENCES = "Preferences";
    SharedPreferences sharedPreferences;
    private boolean isPlaying = false;
    private ImageButton playBtn;

    private Socket socket;
    {
        try{
            socket = IO.socket("http://192.168.1.172:3000");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        socket.connect();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView titleTv = (TextView) findViewById(R.id.textView2);
        Button startBtn = (Button) findViewById(R.id.Button);
        final EditText keyInput = (EditText)findViewById(R.id.editText);


        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.contains("key")) {
            String theKey = sharedPreferences.getString("key", null);
            keyInput.setText(theKey);
            key = keyInput.getText().toString();

        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            final String title = extras.getString("vidTitle");
            videoUrl = extras.getString("vidURL");

            titleTv.setText("Playing: "+title);

        }


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Button) v).getText().equals("START")) {
                    if(keyInput.getText().length()>10) {
                        key = keyInput.getText().toString();
                        editor.putString("key", key);
                        editor.commit();
                        startVideo();
                        keyInput.setEnabled(false);
                        ((Button) v).setText("Edit");

                    }
                }
                else{
                    keyInput.setEnabled(true);
                    ((Button) v).setText("START");

                }
            }
        });

        playBtn = (ImageButton) findViewById(R.id.imageButton);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying) {
                    socket.emit("play", key);
                    ((ImageButton) v).setImageResource(R.drawable.ic_pause);
                    isPlaying = true;
                }
                else{
                    socket.emit("pause", key);
                    ((ImageButton) v).setImageResource(R.drawable.ic_play);
                    isPlaying = false;
                }
            }
        });


        ImageButton muteBtn = (ImageButton)findViewById(R.id.imageButton1) ;

        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    socket.emit("mute", key);

            }
        });


    }

    private void startVideo(){
        JSONObject recAndUrl = new JSONObject();
        try{
            recAndUrl.put("url",videoUrl);
            recAndUrl.put("to",key);
            socket.emit("startVideo", recAndUrl);
            isPlaying = true;
            playBtn.setImageResource(R.drawable.ic_pause);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

}
