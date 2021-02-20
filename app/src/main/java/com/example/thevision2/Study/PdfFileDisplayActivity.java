package com.example.thevision2.Study;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thevision2.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Scanner;

public class PdfFileDisplayActivity extends AppCompatActivity {

    public static final String PDFNAME = "pdfname";
    public static final String PDFURL = "pdfurl";
    public static final String PDFSUBJECT = "subject";
    private PDFView pdfView;
    private ProgressDialog progressDialog;
    private String tag = "PdfFileDisplayActivity";
    private TextToSpeech tts;
    private String stringText;
    private Intent intent;
    private String PDF_LINK;
    private String subject;
    private String PDF_name;
    private String FINAL_PDF_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_file_display);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);

        stringText = "Notes open";
        textToSpeech();
        changeStatusBarColor();
        setReference();
        fetchData();

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
        pdfView = findViewById(R.id.pdfView);
    }

    private void fetchData(){
        progressDialog.show();
        stringText = "Loading files";
        textToSpeech();
        intent = getIntent();
        PDF_LINK = intent.getStringExtra(PDFURL);
        subject = intent.getStringExtra(PDFSUBJECT);
        PDF_name = intent.getStringExtra(PDFNAME);
        FINAL_PDF_NAME = PDF_name+".pdf";
        dowmloadFiles(FINAL_PDF_NAME);
    }

    private void dowmloadFiles(String final_pdf_name) {
        new AsyncTask<Void,Integer,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids) {
              return dowmloadPdf();
            }

            private Boolean dowmloadPdf() {
                try {
                    File file = getFileStreamPath(final_pdf_name);
                    if (file.exists()){
                        return true;
                    }
                    try {
                        FileOutputStream fileOutputStream = openFileOutput(final_pdf_name,Context.MODE_PRIVATE);
                        URL u = new URL(PDF_LINK);
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();
                        InputStream inputStream = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentLength];
                        long totle = 0;
                        int count;
                        while ((count = inputStream.read(data))!= -1){
                            totle += count;
                            fileOutputStream.write(data,0,count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    openPdf(final_pdf_name);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(PdfFileDisplayActivity.this,"Unable to download this file", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void openPdf(String final_pdf_name) {
        try {
            progressDialog.dismiss();
            File file = getFileStreamPath(final_pdf_name);
            Log.e(tag,"filePath:"+file.getAbsolutePath());
            System.out.print("Filepath:"+file.getAbsolutePath());
            pdfView.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableAntialiasing(true).spacing(0).load();
            readPdfFile(file);
        }catch (Exception e){e.printStackTrace();}

    }

    private void readPdfFile(File file) {
        try {
            PdfReader pdfReader = new PdfReader(file.getAbsolutePath());

            stringText = PdfTextExtractor.getTextFromPage(pdfReader,1);
            textToSpeech();
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onPause() {
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
