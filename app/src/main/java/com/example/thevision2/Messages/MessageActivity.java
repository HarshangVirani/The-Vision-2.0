package com.example.thevision2.Messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.example.thevision2.R;

public class MessageActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 1;
    private String tag = "MessageActivity";
    private Uri uri;
    private Cursor cursor;
//    private ArrayList<MessageModel> messageModels;
    private RecyclerView recyclerView;
  //  private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        checkPermission();
        setReference();
    }

    private void checkPermission(){
        int contextCompat = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (contextCompat == PackageManager.PERMISSION_GRANTED){
           //setMessages();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{(Manifest.permission.READ_SMS)},REQUEST_CODE);
            Log.d(tag,"Error in permission");
        }
    }

    public void setReference(){
        recyclerView = findViewById(R.id.recyclerView);
    }


}