package com.example.thevision2.Study;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thevision2.R;
import com.example.thevision2.Study.Adapter.NotesListViewAdapter;
import com.example.thevision2.Study.Model.NotesListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileList extends AppCompatActivity {

    public static final String SUBJECT = "Subject";
    public static final String STANDARD = "Standard";
    private RecyclerView listView;
    private Intent intent;
    private int standard;
    private String subject;
    private TextView standardDisplay;
    private TextView subjectDisplay;
    private String tag = "FileListActivity";
    private DatabaseReference databaseReference;
    private List<NotesListModel> uploadPDFs;
    private RelativeLayout noFileTextDisplay;
    private TextToSpeech tts;
    private String stringText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        stringText = "Notes List";
        textToSpeech();
        changeStatusBarColor();
        setReference();
        fetchData();

        uploadPDFs = new ArrayList<>();
        viewAllFiles();
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
        listView = findViewById(R.id.list_view);
        standardDisplay = findViewById(R.id.standard_text_display);
        subjectDisplay = findViewById(R.id.subject_text_display);
        noFileTextDisplay = findViewById(R.id.no_file_layout);
    }

    //Fetch data from other activity and set STANDARD and SUBJECT title
    private void fetchData(){
            //Fetch data
            intent = getIntent();
            standard = intent.getIntExtra(STANDARD,0);
            subject = intent.getStringExtra(SUBJECT);

            //Set data
            standardDisplay.setText("STD:"+standard);
            subjectDisplay.setText("SUB:"+subject);
           // Toast.makeText(getApplicationContext(), standard + " " + subject, Toast.LENGTH_SHORT).show();
    }


    //View PDF files in list view
    private void viewAllFiles(){

        //Set progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        stringText = "Please wait";
        textToSpeech();

        //Set standard files
        String pathStandardReference = null;
        String finalPathReference;

        //Standard set class
        if (standard == 5){
            pathStandardReference = "Class-5";
        }else if (standard == 6){
            pathStandardReference = "Class-6";
        }else if (standard == 7){
            pathStandardReference = "Class-7";
        }else {
            Toast.makeText(getApplicationContext(),"No File available",Toast.LENGTH_SHORT).show();
        }

        finalPathReference = pathStandardReference+subject;
        Log.d(tag,finalPathReference);

        //Fetch data from database
        databaseReference = FirebaseDatabase.getInstance().getReference(finalPathReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                   NotesListModel uploadModel = postSnapshot.getValue(com.example.thevision2.Study.Model.NotesListModel.class);
                    uploadPDFs.add(uploadModel);
                    System.out.println(uploadPDFs);
                }

                String[] upload = new String[uploadPDFs.size()];
                String[] urlLoad = new String[uploadPDFs.size()];

                for (int i = 0;i<upload.length;i++){
                    upload[i] = uploadPDFs.get(i).getPdfNames();
                    urlLoad[i] = uploadPDFs.get(i).getUrl();
                }


                if (uploadPDFs.isEmpty()){
                    progressDialog.dismiss();
                    noFileTextDisplay.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Zero files",Toast.LENGTH_SHORT).show();
                    stringText = "No files found";
                    textToSpeech();

                }else {
                    listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    NotesListViewAdapter adapter = new NotesListViewAdapter(getApplicationContext(),upload,urlLoad,subject);
                    listView.setAdapter(adapter);
                    stringText = "Continue";
                    textToSpeech();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String s = error.toException().toString();
                Log.d(tag,s);
                progressDialog.dismiss();
            }
        });
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