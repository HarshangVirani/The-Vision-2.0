package com.example.thevision2.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.thevision2.Messages.Adapter.MessageAdapter;
import com.example.thevision2.Messages.InterFace.RecyclerViewClickInterFace;
import com.example.thevision2.Messages.Model.SMSModel;
import com.example.thevision2.R;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PrimitiveIterator;

public class MessageActivity extends AppCompatActivity implements RecyclerViewClickInterFace {

    private int REQUEST_CODE = 1000;
    private String tag = "MainActivity";
    private List<SMSModel> smsList;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private Cursor cursor;
    private StackLayoutManager layoutManager;
    private TextToSpeech tts;
    private String stringText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        smsList = new ArrayList<SMSModel>();

        layoutManager = new StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP);
        layoutManager.setPagerMode(true);
        layoutManager.setPagerFlingVelocity(3000);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(this,smsList,this);
        recyclerView.setAdapter(adapter);

        changeStatusBarColor();
        checkPermission();
    }

    //change statusbar color
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.nevi_light_blue));
        }
    }

    private void checkPermission(){
        int contextCompat = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (contextCompat == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"PERMISSION_GRANTED",Toast.LENGTH_SHORT).show();
            setSMSMessage();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{(Manifest.permission.READ_SMS)},REQUEST_CODE);
            Log.d(tag,"Error in permission");
        }
    }

    //Fetch SMS from phone and send to the applicatin
    private void setSMSMessage() {
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver contentResolver = getContentResolver();
        cursor = contentResolver.query(uri, null,null,null,null);

        if (cursor != null){
            if (cursor.moveToNext()){
                int header = cursor.getColumnIndex("address");

                String dates = cursor.getString(cursor.getColumnIndex("date"));
                int messages = cursor.getColumnIndex("body");

                do {
                    Date date = new Date(Long.valueOf(dates));
                    smsList.add(new SMSModel(cursor.getString(header),date,cursor.getString(messages)));

                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    //InterFace for Message TITLE Reading
    @Override
    public void onItemClick(SMSModel smsModel) {
        //stringText == Message Title
        stringText = smsModel.getTitle();
        Toast.makeText(getApplicationContext(),stringText,Toast.LENGTH_SHORT).show();
        textToSpeech();
    }

    //InterFace for Message Body Reading
    @Override
    public void onDoubleItemClick(SMSModel smsModel) {
        //stringText == Message Body
        stringText = smsModel.getMessageBody();
        Toast.makeText(getApplicationContext(),stringText,Toast.LENGTH_SHORT).show();
        textToSpeech();
    }

    //InterFace for reading stop
    @Override
    public void onLongItemClick() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }

    //Text to Speech
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
    public void onBackPressed() {
        super.onBackPressed();
         onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onRestart() {
        textToSpeech();
        super.onRestart();
    }
}