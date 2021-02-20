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
import android.widget.Toast;

import com.example.thevision2.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.Locale;

public class StudyActivity extends AppCompatActivity {

    private RelativeLayout fiveStandard,sixStandard,sevenStandard,otherStandard;
    private String stringText = "";
    private TextToSpeech tts;
    private Intent intent;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        changeStatusBarColor();

        stringText = "Standard";
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
        fiveStandard = findViewById(R.id.five);
        sixStandard = findViewById(R.id.six);
        sevenStandard = findViewById(R.id.seven);
        otherStandard = findViewById(R.id.other);
    }

    private void setButton(){
        fiveStandard.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "5th Standard";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                value = 5;
                sendData(value);
            }
        }));
        sixStandard.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "6th Standard";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                value =6;
                sendData(value);
            }
        }));
        sevenStandard.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "7th Standard";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                value = 7;
                sendData(value);
            }
        }));
        otherStandard.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "Other Standard";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                stringText = "Locked";
                textToSpeech();
            }
        }));
    }

    //Send Data to Other Activity
    private void sendData(int value){
       // Toast.makeText(getApplicationContext(),"Send Data method call",Toast.LENGTH_SHORT).show();
        if (value == 5){
        intent = new Intent(StudyActivity.this,Subject.class);
        intent.putExtra(Subject.STANDARD, value);
        startActivity(intent);
        }else if ((value == 6) || (value==7)){
            intent = new Intent(StudyActivity.this,SixSevenSubject.class);
            intent.putExtra(SixSevenSubject.STANDARDS,value);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"Please select standard",Toast.LENGTH_SHORT).show();
        }
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