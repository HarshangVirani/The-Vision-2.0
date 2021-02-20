package com.example.thevision2.Study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thevision2.R;
import com.example.thevision2.Study.PdfFileDisplayActivity;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.Locale;

public class NotesListViewAdapter extends RecyclerView.Adapter<NotesListViewAdapter.NotesModel> {

    private Context context;
    private String[] upload;
    private String[] urlLoad;
    private TextToSpeech tts;
    private String stringText;
    private String subject;

    public NotesListViewAdapter(Context context, String[] upload, String[] urlLoad, String subject) {
        this.context = context;
        this.upload = upload;
        this.urlLoad = urlLoad;
        this.subject = subject;
    }

    @NonNull
    @Override
    public NotesModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view,parent,false);
        return new NotesModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesModel holder, int position) {
        //Pdf name
        String name = upload[position].toString();
        holder.pdfName.setText(name);

        //Pdf url
        String url = urlLoad[position].toString();

        Log.d("Adapter",subject);

        holder.cardView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                stringText = name;
                textToSpeech();
            }

            @Override
            public void onDoubleClick(View view) {
                Intent intent = new Intent(context, PdfFileDisplayActivity.class);
                intent.putExtra(PdfFileDisplayActivity.PDFNAME,name);
                intent.putExtra(PdfFileDisplayActivity.PDFURL,url);
                intent.putExtra(PdfFileDisplayActivity.PDFSUBJECT,subject);
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return upload.length;
    }

    private void textToSpeech() {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
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

    public static class NotesModel extends RecyclerView.ViewHolder {
        TextView pdfName;
        LinearLayout cardView;
        public NotesModel(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdf_name);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
