package com.hasan.storyvibrance.Controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.Message;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getContent());
        holder.textViewTimestamp.setText(getFormattedTimestamp(message.getTimestamp()));
        Log.d("timestamp", "onBindViewHolder: " + getFormattedTimestamp(message.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.messageTextView);
            textViewTimestamp = itemView.findViewById(R.id.timestampTextView);
        }
    }

    private String getFormattedTimestamp(long timestamp) {
        // Convert timestamp to human-readable format (e.g., "12:34 PM")
        // Implement this method according to your desired timestamp format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
