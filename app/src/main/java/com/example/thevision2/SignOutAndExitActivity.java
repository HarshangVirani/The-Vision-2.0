package com.example.thevision2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class SignOutAndExitActivity extends AppCompatActivity {

    private Button logOutBtn,exitBtn;
    private String stringText;
    private TextToSpeech tts;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out_and_exit);

        stringText = "Click Upper part of your screen for Logout and Down part of your screen for exit";
        textToSpeech();

        setReference();
        setButtons();
    }

    //Set References
    public void setReference() {
        logOutBtn = findViewById(R.id.log_out);
        exitBtn = findViewById(R.id.exit);
    }

    //Set Button
    private void setButtons(){
        //Log out
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(SignOutAndExitActivity.this,SignUpAndLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Exit
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.shutdown();
                finish();
                System.exit(0);
            }
        });
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

    @Override
    protected void onPause() {
        tts.shutdown();
        super.onPause();
    }
}