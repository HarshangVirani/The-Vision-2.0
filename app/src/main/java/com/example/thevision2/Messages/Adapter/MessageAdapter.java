package com.example.thevision2.Messages.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thevision2.Messages.InterFace.RecyclerViewClickInterFace;
import com.example.thevision2.Messages.Model.SMSModel;
import com.example.thevision2.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<SMSModel> smsList;
    private RecyclerViewClickInterFace recyclerViewClickInterFace;

    public MessageAdapter(Context context, List<SMSModel> smsList, RecyclerViewClickInterFace recyclerViewClickInterFace) {
        this.smsList = smsList;
      //  this.recyclerViewClickInterFace = recyclerViewClickInterFace;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.message_cardview,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        //Set title
        String titles = smsList.get(position).getTitle();
        holder.title.setText(titles);
        //Set dates
        String dates = smsList.get(position).getDate().toString();
        holder.date.setText(dates);
        //Set message body
        String messages = smsList.get(position).getMessageBody();
        holder.message.setText(messages);
        //Set card view click listener(Single & Double)
        holder.cardView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        recyclerViewClickInterFace.onItemClick(smsList.get(position));
                    }

                    @Override
                    public void onDoubleClick(View view) {
                        recyclerViewClickInterFace.onDoubleItemClick(smsList.get(position));
                    }
                }));
        //Set card view long click listener
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerViewClickInterFace.onLongItemClick();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView message;
        CardView cardView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.bodyMessage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
