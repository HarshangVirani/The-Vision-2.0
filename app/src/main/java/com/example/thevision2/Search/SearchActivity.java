package com.example.thevision2.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thevision2.R;

import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private LinearLayout searchBtn,textRecogBtn;
    private TextToSpeech tts;
    private String stringText;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        stringText = "Click Upper part of your screen for search and Down part of your screen for text recognition";
        textToSpeech();

        changeStatusBarColor();
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
        searchBtn = findViewById(R.id.search_layout);
        textRecogBtn = findViewById(R.id.text_reco_layout);
    }

    private void setButton() {
        //search btn
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){
                    intent = new Intent(SearchActivity.this,SearchMainActivity.class);
                    startActivity(intent);
                }else {
                    stringText = "Please turn on internet";
                    textToSpeech();
                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //text recognition btn
        textRecogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SearchActivity.this, TextRecognition.class);
                startActivity(intent);
            }
        });
    }

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

    //Check Internet Connection
    private boolean isConnected(){
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo !=null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Connection Exception",e.getMessage());
        }
        return connected;
    }

    @Override
    protected void onPause() {
        if (tts != null){
            tts.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onRestart() {
        textToSpeech();
        super.onRestart();
    }

}