package com.kodiapps.iptv.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SmartPlayerActivity extends AppCompatActivity {

    RelativeLayout parentRelativeLayout;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    String kepper = null;
    public ImageView pushPlayBtn, nextBtn, previousBtn;

    ImageView imageView;
    RelativeLayout lowerRelativeLayout, upperLayout;
    Button voiceEnableBtn;
    TextView songNameTxt;
    String mode = "ON";

    MediaPlayer mediaPlayer;
    int pos;
    ArrayList<File> song;
    String songName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkVoidcecomandPermission();

        pushPlayBtn = findViewById(R.id.play_push_Btn);
        nextBtn = findViewById(R.id.next_Btn);
        previousBtn = findViewById(R.id.previousBtn);

        imageView = findViewById(R.id.logoID);
        voiceEnableBtn = findViewById(R.id.voiceEnabelBtn);
        lowerRelativeLayout = findViewById(R.id.lowerID);
        parentRelativeLayout = findViewById(R.id.parentRelativeID);
        songNameTxt = findViewById(R.id.songNameID);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        init();
        imageView.setBackgroundResource(R.drawable.logo);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matchFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchFound != null) {
                    kepper = matchFound.get(0);
                    Toast.makeText(SmartPlayerActivity.this, "Result = " + kepper, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        kepper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;

                }
                return false;
            }
        });


        voiceEnableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("ON")) {
                    mode = "OFF";
                    voiceEnableBtn.setText("Voice Enabled Mode - OFF");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    mode = "ON";
                    voiceEnableBtn.setText("Voice Enabled Mode - ON");
                    lowerRelativeLayout.setVisibility(View.GONE);

                }
            }
        });
    }

    private void init() {


        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pos = bundle.getInt("position");
        song = (ArrayList) bundle.getParcelableArrayList("song");
        songName = song.get(pos).getName();
        songNameTxt.setText(songName);
        songNameTxt.setSelected(true);

        Uri uri = Uri.parse(song.get(pos).toString());
        mediaPlayer = MediaPlayer.create(SmartPlayerActivity.this, uri);
        mediaPlayer.start();
    }

    public void checkVoidcecomandPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageManager()));
                startActivity(intent);
                finish();
            }
        }

    }
}