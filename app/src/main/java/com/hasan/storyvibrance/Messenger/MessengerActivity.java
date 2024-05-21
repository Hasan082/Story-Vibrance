package com.hasan.storyvibrance.Messenger;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasan.storyvibrance.Controller.MessageAdapter;
import com.hasan.storyvibrance.Model.Message;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityMessengerBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MessengerActivity extends AppCompatActivity {

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;
    ActivityMessengerBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_messenger);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewChat.setAdapter(messageAdapter);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load messages from Firestore
        loadMessages();

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message);
                    binding.editTextMessage.setText("");
                }
            }
        });

        //back to previous page
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });



    }

    private void loadMessages() {
        db.collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e("error", "Error fetching messages", e);
                        return;
                    }

                    messages.clear();
                    if (snapshot != null) {
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            String messageContent = document.getString("message");
                            Long timestamp = document.getLong("timestamp");

                            if (timestamp != null && messageContent != null) {
                                Message message = new Message(messageContent, timestamp);
                                messages.add(message);
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                        binding.recyclerViewChat.scrollToPosition(messages.size() - 1);
                    }
                });
    }




    private void sendMessage(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("messages")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Message sent successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
}