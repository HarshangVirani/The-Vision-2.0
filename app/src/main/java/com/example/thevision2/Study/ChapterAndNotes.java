package com.example.thevision2.Study;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.thevision2.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.Locale;

public class ChapterAndNotes extends AppCompatActivity {

    public static final String SUBJECT = "Subject";
    public static final String STANDARD = "Standard";
    private Button chapterBtn,notesBtn;
    private TextToSpeech tts;
    private String stringText="";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_and_notes);

        stringText = "Click Upper part of your screen for Chapters and Down part of your screen for Notes";
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


    private void setReference(){
        chapterBtn = findViewById(R.id.chapter_btn);
        notesBtn = findViewById(R.id.notes_btn);
    }

    private void setButton() {
        intent = getIntent();
        int standard = intent.getIntExtra(STANDARD,0);
        String subject = intent.getStringExtra(SUBJECT);
        //Chapters
        chapterBtn.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "Chapters";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                setChapters(standard,subject);
            }
        }));

        //Notes
        notesBtn.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = "Notes";
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                if(isConnected()) {
                    setNotes(standard, subject);
                }else {
                    stringText = "Please turn on Internet";
                    textToSpeech();
                }
            }
        }));
    }

    //Internet Connection check
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

    //Set Chapters
    private void setChapters(int standard, String subject){
        Log.d("ChapterAndNotes", subject +""+ standard);
        Toast.makeText(getApplicationContext(), subject +""+ standard,Toast.LENGTH_SHORT).show();
        setActionData(standard, subject);
    }

    //Send Data to other activity
    private void setActionData(int standard, String subject){
       // intent = new Intent(ChapterAndNotes.this,ChapterActivity.class);
        //intent.putExtra(ChapterActivity.STANDARD,standard);
        //intent.putExtra(ChapterActivity.SUBJECT,subject);
        //startActivity(intent);
    }

    //Set notes and Send Data to other activity
    private void setNotes(int standard, String subject) {
        intent = new Intent(ChapterAndNotes.this,FileList.class);
        intent.putExtra(FileList.STANDARD,standard);
        intent.putExtra(FileList.SUBJECT,subject);
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