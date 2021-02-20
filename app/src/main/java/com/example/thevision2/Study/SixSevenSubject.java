package com.example.thevision2.Study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.thevision2.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.Locale;

public class SixSevenSubject extends AppCompatActivity {

    public static final String STANDARDS = "Standard";
    private RelativeLayout englishSub,hindiSub,sstSub,scienceSub;
    private TextToSpeech tts;
    private String subject,stringText="";
    private Intent intent;
    private int standard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_seven_subject);
        changeStatusBarColor();
        stringText = "Subject";
        textToSpeech();
        setReference();
        setButton();
    }

    //change statusbar color
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.nevi_light_blue));
        }
    }

    private void setReference() {
        englishSub = findViewById(R.id.english);
        hindiSub = findViewById(R.id.hindi);
        sstSub = findViewById(R.id.sst);
        scienceSub = findViewById(R.id.science);
    }

    private void setButton(){
        englishSub.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "english";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                subject = "English";
                sendData(subject);
            }
        }));
        hindiSub.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "hindi";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                subject = "Hindi";
                sendData(subject);
            }
        }));
        sstSub.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "social science";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                subject = "Sst";
                sendData(subject);
            }
        }));
        scienceSub.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "science";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                subject="Science";
                sendData(subject);
            }
        }));
    }

    //Send data
    private void sendData(String subject){
        //Fetch data
        intent = getIntent();
        standard = intent.getIntExtra(STANDARDS,0);
        Log.d("SixSevenSubject",subject+""+standard);

        //Send data
        intent = new Intent(SixSevenSubject.this,ChapterAndNotes.class);
        intent.putExtra(ChapterAndNotes.SUBJECT,subject);
        intent.putExtra(ChapterAndNotes.STANDARD,standard);
        startActivity(intent);
    }

    //convert text into speech
    private void textToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                tts.setSpeechRate(0.8f);
                tts.setPitch(1.0f);
                tts.speak(stringText, TextToSpeech.QUEUE_FLUSH, null, null);

            }
        }, 500);
    }

    @Override
    public void onRestart() {
        textToSpeech();
        super.onRestart();
    }
}