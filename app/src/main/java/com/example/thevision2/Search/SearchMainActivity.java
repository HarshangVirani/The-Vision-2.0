package com.example.thevision2.Search;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thevision2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class SearchMainActivity extends AppCompatActivity {

    private RelativeLayout micBtn;
    private Button stopBtn;
    private TextView searchText,searchTitle;
    private TextToSpeech tts;
    private String stringText;
    private String email,description;
    private String tag="SearchMainActivity";
    private int flag = 0;
    private FirebaseUser user;
    private Elements paragraph;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        stringText = "Click bottom part of your screen for mic ";
        textToSpeech();

        setReference();
        setButton();
    }

    @Override
    protected void onStart() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    private void setReference() {
        micBtn = findViewById(R.id.mic_layout);
        searchText = findViewById(R.id.search_text);
        searchTitle = findViewById(R.id.search_text_title);
        progressBar = findViewById(R.id.progress_bar);
        stopBtn = findViewById(R.id.stop_btn);
    }

    private void setButton(){
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.stop();
                tts.shutdown();
            }
        });
    }

    private void data() {
        if (flag == 0){
            stringText = "Speak";
            textToSpeech();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    speechToText();
                }
            },3000);
        }
    }

    //Text to speech
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

    //Speech to text
    private void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        Log.d(tag, "Inside speech here");
        try {
            startActivityForResult(intent, 1000);
        } catch (ActivityNotFoundException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchTitle.setText(result.get(0));
                    description = result.get(0);
                    readWebpage();
                }
                break;
            }
        }
    }

    //Read data from webpage
    public void readWebpage() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentDisplay task = new DocumentDisplay();
        task.execute("http://en.m.wikipedia.org/wiki/Special:search?search=" + searchTitle.getText().toString());
    }

    //Download document
    private class DocumentDisplay extends AsyncTask<String, Void, Void>{
        String s;
        TextToSpeech tts;
        @Override
        protected Void doInBackground(String... urls) {
            Document doc = null;
            try {
                doc = (Document) Jsoup.connect(urls[0]).get();
                paragraph = doc.select("p");
            } catch (IOException e) {
                e.printStackTrace();
            }
            s = paragraph.text();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.GONE);
            searchText.setText(s);
            stringText = searchText.getText().toString();
            Toast.makeText(getApplicationContext(),stringText,Toast.LENGTH_SHORT).show();
            textToSpeech();
        }
    }


    @Override
    public void onPause() {
        if (tts != null){
            tts.shutdown();
        }
        super.onPause();
    }

}