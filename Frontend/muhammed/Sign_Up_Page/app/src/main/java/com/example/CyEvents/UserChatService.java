package com.example.CyEvents;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code UserChatService} class is an Android Service responsible for managing WebSocket connections
 * for real-time communication within the chat application. It handles WebSocket connection and disconnection
 * requests, sends and receives messages via WebSocket, and broadcasts messages to other components of the application.
 * It also integrates with local broadcasting for chat message handling.
 */
public class UserChatService extends Service {

    /**
     * A map that holds multiple WebSocketClient instances, each associated with a unique key.
     * This allows managing multiple WebSocket connections.
     */
    private final Map<String, WebSocketClient> webSockets = new HashMap<>();

    /**
     * Default constructor for {@code UserChatService}.
     * Initializes the service.
     */
    public UserChatService() {}

    /**
     * Called when the service is started. Handles the WebSocket connection and disconnection requests.
     * @param intent The Intent that started the service, containing the action and any necessary data.
     * @param flags Additional flags for the start command.
     * @param startId A unique identifier for this specific start request.
     * @return The return value defines how the system should continue running the service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("CONNECT".equals(action)) {
                String url = "ws://coms-3090-041.class.las.iastate.edu:8080/chat/25";
                String key = intent.getStringExtra("key");
                connectWebSocket(key, url);
            } else if ("DISCONNECT".equals(action)) {
                String key = intent.getStringExtra("key");
                disconnectWebSocket(key);
            }
        }
        return START_STICKY;
    }

    /**
     * Called when the service is created. Initializes the receiver for handling WebSocket messages.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(chatReceiver, new IntentFilter("SendWebSocketMessage"));
    }

    /**
     * Called when the service is destroyed. Unregisters the receiver to avoid memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver);
    }

    /**
     * Initiates a WebSocket connection using the provided key and URL.
     * @param key The key used to identify the WebSocket connection.
     * @param url The URL of the WebSocket server to connect to.
     */
    private void connectWebSocket(String key, String url) {
        URI uri = URI.create(url); // Create URI to connect to
        WebSocketClient webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                Log.d("UserChatService", "Connected to WebSocket");
            }

            @Override
            public void onMessage(String message) {
                // Broadcast the received WebSocket message to other components
                Intent messageIntent = new Intent("WebSocketMessageReceived");
                messageIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(UserChatService.this).sendBroadcast(messageIntent);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("UserChatService", "Disconnected from WebSocket");
            }

            @Override
            public void onError(Exception ex) {
                Log.e("UserChatService", "WebSocket Error: ", ex);
            }
        };
        webSockets.put(key, webSocketClient);
        webSocketClient.connect();
    }

    /**
     * Disconnects the WebSocket associated with the provided key.
     * @param key The key that identifies the WebSocket connection to be closed.
     */
    private void disconnectWebSocket(String key) {
        WebSocketClient webSocketClient = webSockets.get(key);
        if (webSocketClient != null) {
            webSocketClient.close();
            webSockets.remove(key);
        }
    }

    /**
     * A {@link BroadcastReceiver} that listens for incoming broadcast messages, which contain chat messages
     * to be sent through a WebSocket connection.
     */
    private final BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String key = intent.getStringExtra("key");

            WebSocketClient webSocketClient = webSockets.get(key);
            if (webSocketClient != null && message != null) {
                // Send message through the appropriate WebSocket connection
                webSocketClient.send(message);
            }
        }
    };

    /**
     * The method that provides binding to this service. In this case, the service does not provide binding
     * functionality.
     * @param intent The intent used to bind to the service.
     * @return {@code null} because this service does not provide binding.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
