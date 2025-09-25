package com.example.androidexample;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class ChatActivity2 extends AppCompatActivity {

    private Button sendBtn;
    private Button emojiBtn;
    private EditText msgEtx;
    private ListView messageListView;
    private ArrayAdapter<String> messageAdapter;
    private ArrayList<String> messageHistory;

    private String[] emojis = {"😊", "😂", "👍", "❤️", "😢", "🔥", "🎉"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);


        sendBtn = (Button) findViewById(R.id.sendBtn2);
        emojiBtn = (Button) findViewById(R.id.emojiBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt2);
        messageListView = (ListView) findViewById(R.id.messageListView);


        messageHistory = new ArrayList<>();
        messageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageHistory);
        messageListView.setAdapter(messageAdapter);


        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();

            // Broadcast the message and add to history
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat2");
            intent.putExtra("message", message);

            addMessageToHistory("Me: " + message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


            msgEtx.setText("");
        });


        emojiBtn.setOnClickListener(v -> showEmojiDialog());
    }


    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            if ("chat2".equals(key)){
                String message = intent.getStringExtra("message");
                addMessageToHistory("Friend: " + message);
            }
        }
    };

    private void addMessageToHistory(String message) {

        messageHistory.add(message);
        messageAdapter.notifyDataSetChanged();
    }


    private void showEmojiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Emoji");

        builder.setItems(emojis, (dialog, which) -> {

            String currentText = msgEtx.getText().toString();
            msgEtx.setText(currentText + emojis[which]);
            msgEtx.setSelection(msgEtx.getText().length());
        });

        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}

