package com.hasan.storyvibrance.Controller;

import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.Message;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    FirebaseFirestore db;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
        this.db = FirebaseFirestore.getInstance();
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
        String messageSender = message.getSenderUserId();
        Log.d("MessageAdapter", "Sender ID: " + messageSender);

        UserDataFetcher userDataFetcher = new UserDataFetcher(db);
        userDataFetcher.fetchUserData(messageSender, (name, profileImg) -> {
            if (name != null) {
                String splitName = name.split(" ")[0];
                String firstname = NameCapitalize.capitalize(splitName);
                holder.firstName.setText(firstname);
            }
            if (profileImg != null) {
                Picasso.get().load(profileImg).placeholder(R.drawable.edit_person).into(holder.senderImg);
            }else {
                Picasso.get().load(R.drawable.edit_person).into(holder.senderImg);
            }
        });
        Log.d("timestamp", "onBindViewHolder: " + getFormattedTimestamp(message.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView senderImg;

        TextView textViewMessage, firstName, textViewTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderImg = itemView.findViewById(R.id.senderImg);
            firstName = itemView.findViewById(R.id.firstName);
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
