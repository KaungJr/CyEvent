package com.example.CyEvents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CyEvents.ChatAdapter;
import com.example.CyEvents.ChatMessage;
import com.example.Sign_up_Page.R;
import okhttp3.*;

import java.util.ArrayList;

/**
 * UserChatActivity is an activity that allows users to send and receive messages via WebSocket in a chat interface.
 * It provides functionalities for sending text messages, replying to messages, sending images, and managing message status.
 * The activity also integrates with WebSocket to receive messages and update the UI accordingly.
 */
public class UserChatActivity extends AppCompatActivity implements ChatAdapter.OnMessageClickListener {

    /**
     * The WebSocket URL used for communication.
     */
    private static final String WEBSOCKET_URL = "ws://10.90.73.72:8080/chat/25";

    private OkHttpClient client;
    private WebSocket webSocket;
    private WebSocketListener webSocketListener;

    private Button sendBtn;
    private Button cancelReplyBtn;
    private Button sendImageBtn;
    private EditText msgEtx;
    private RecyclerView messageRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> messageHistory;
    private TextView replyContextText;
    private String replyingToMessage = null;
    private BroadcastReceiver webSocketReceiver;
    private String username = "Bobby";
    /**
     * Initializes the activity and sets up UI components and WebSocket connection.
     *
     * @param savedInstanceState The saved instance state for restoring the previous activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        // Initialize UI components
        sendBtn = findViewById(R.id.sendBtn);
        cancelReplyBtn = findViewById(R.id.cancelReplyBtn);
        sendImageBtn = findViewById(R.id.imageBtn);
        msgEtx = findViewById(R.id.msgEdt);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        replyContextText = findViewById(R.id.replyContextText);

        messageHistory = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageHistory, this);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(chatAdapter);

        // Set up WebSocket client and listener
        client = new OkHttpClient();
        webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                runOnUiThread(() -> {
                    // Prevent adding duplicate messages by checking if the message already exists in history
                    if (!isDuplicateMessage(text)) {
                        ChatMessage receivedMessage = new ChatMessage(username + ": " + text); // Prefix the message with the sender's name
                        messageHistory.add(receivedMessage);
                        chatAdapter.notifyItemInserted(messageHistory.size() - 1);
                        messageRecyclerView.scrollToPosition(messageHistory.size() - 1);
                    }
                });
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }
        };

        Request request = new Request.Builder().url(WEBSOCKET_URL).build();
        webSocket = client.newWebSocket(request, webSocketListener);

        // Set up button listeners
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();
            if (!message.isEmpty()) {
                sendMessageToWebSocket(message);
                ChatMessage chatMessage = new ChatMessage(username + ": " + message); // Prefix the message with the sender's name

                if (replyingToMessage != null) {
                    chatMessage = new ChatMessage(username + ": " + message, replyingToMessage);
                    replyingToMessage = null;
                    replyContextText.setText("");
                    cancelReplyBtn.setVisibility(View.GONE);
                }

                // Prevent adding duplicate messages
                if (!isDuplicateMessage(message)) {
                    messageHistory.add(chatMessage);
                    chatAdapter.notifyItemInserted(messageHistory.size() - 1);
                    messageRecyclerView.scrollToPosition(messageHistory.size() - 1);
                    updateMessageStatus(chatMessage);
                }

                msgEtx.setText("");
            }
        });


        sendImageBtn.setOnClickListener(v -> openImagePicker());

        cancelReplyBtn.setOnClickListener(v -> cancelReply());

        webSocketReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if (message != null && !isDuplicateMessage(message)) {
                    ChatMessage receivedMessage = new ChatMessage(message);
                    messageHistory.add(receivedMessage);
                    chatAdapter.notifyItemInserted(messageHistory.size() - 1);
                    messageRecyclerView.scrollToPosition(messageHistory.size() - 1);
                }
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(webSocketReceiver, new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(webSocketReceiver);
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
        }
    }

    /**
     * Checks if the given message is already present in the message history to prevent duplicates.
     *
     * @param message The message to check.
     * @return True if the message is a duplicate, false otherwise.
     */
    private boolean isDuplicateMessage(String message) {
        for (ChatMessage chatMessage : messageHistory) {
            // Check if the message itself is the same as an existing message in the history
            if (chatMessage.getMessage().equals(message)) {
                return true;
            }

            // Check if the message is a reply and the original message matches
            if (chatMessage.isReply() && chatMessage.getReplyText().equals(message)) {
                return true;
            }
        }
        return false;
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            sendImageMessage(imageUri);
        }
    }

    private void sendImageMessage(Uri imageUri) {
        String imageMessage = imageUri.toString();
        // Create an image message with the URI of the image
        ChatMessage imageMessageObject = new ChatMessage(imageMessage, true);
        if (!isDuplicateMessage(imageUri.toString())) {
            messageHistory.add(imageMessageObject);
            chatAdapter.notifyItemInserted(messageHistory.size() - 1);
            messageRecyclerView.scrollToPosition(messageHistory.size() - 1);
            updateMessageStatus(imageMessageObject);
        }
    }

    private void updateMessageStatus(ChatMessage message) {
        message.setStatus(ChatMessage.STATUS_SENT);
        chatAdapter.notifyItemChanged(messageHistory.indexOf(message));

        messageRecyclerView.postDelayed(() -> {
            message.setStatus(ChatMessage.STATUS_DELIVERED);
            chatAdapter.notifyItemChanged(messageHistory.indexOf(message));
        }, 2000);
    }

    @Override
    public void onMessageClick(String message, int position) {
        String[] options = {"Reply", "React", "Delete", "Forward"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    replyingToMessage = message;
                    replyContextText.setText("Replying to: " + message);
                    cancelReplyBtn.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    showEmojiReactionDialog(position);
                    break;
                case 2:
                    messageHistory.remove(position);
                    chatAdapter.notifyItemRemoved(position);
                    break;
                case 3:
                    forwardMessage(position);
                    break;
            }
        });
        builder.show();
    }

    @Override
    public void onDeleteMessage(int position) {
        messageHistory.remove(position);
        chatAdapter.notifyItemRemoved(position);
    }

    private void forwardMessage(int position) {
        ChatMessage originalMessage = messageHistory.get(position);
        ChatMessage forwardedMessage = new ChatMessage(originalMessage.getMessage());
        forwardedMessage.setForwarded(true);
        messageHistory.add(forwardedMessage);
        chatAdapter.notifyItemInserted(messageHistory.size() - 1);
    }

    private void showEmojiReactionDialog(int position) {
        String[] emojis = {"😊", "😂", "😢", "😡"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an emoji reaction");
        builder.setItems(emojis, (dialog, which) -> {
            String selectedEmoji = emojis[which];
            ChatMessage message = messageHistory.get(position);
            message.setReaction(selectedEmoji);
            chatAdapter.notifyItemChanged(position);
        });
        builder.show();
    }

    private void sendMessageToWebSocket(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    private void cancelReply() {
        replyingToMessage = null;
        replyContextText.setText("");
        cancelReplyBtn.setVisibility(View.GONE);
    }
}
