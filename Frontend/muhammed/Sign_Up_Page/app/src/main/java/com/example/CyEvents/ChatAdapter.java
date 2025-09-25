package com.example.CyEvents;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Sign_up_Page.R;
import java.util.List;

/**
 * ChatAdapter is a RecyclerView adapter for displaying a list of chat messages.
 * It handles different types of messages including text, image, and replies.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants for different view types
    private static final int VIEW_TYPE_MESSAGE = 0;
    private static final int VIEW_TYPE_REPLY = 1;
    private static final int VIEW_TYPE_IMAGE = 2; // New type for image messages

    private List<ChatMessage> messages; // List of chat messages to display
    private OnMessageClickListener listener; // Listener for message interactions

    public ChatAdapter(List<ChatMessage> messages, OnMessageClickListener listener) {
        this.messages = messages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_reply, parent, false);
            return new ReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);

        if (holder instanceof MessageViewHolder) {
            MessageViewHolder messageHolder = (MessageViewHolder) holder;
            messageHolder.messageText.setText(chatMessage.getMessage());
            messageHolder.timestampText.setText(chatMessage.getTimestamp());

            // Remove the status display
            // messageHolder.statusText.setText(statusText);  // Removed this line

            if (!chatMessage.getReaction().isEmpty()) {
                messageHolder.reactionText.setVisibility(View.VISIBLE);
                messageHolder.reactionText.setText(chatMessage.getReaction());
            } else {
                messageHolder.reactionText.setVisibility(View.GONE);
            }

            messageHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMessageClick(chatMessage.getMessage(), position);
                }
            });

        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageHolder = (ImageViewHolder) holder;
            imageHolder.timestampText.setText(chatMessage.getTimestamp());
            imageHolder.imageView.setImageURI(Uri.parse(chatMessage.getImageUri()));

            imageHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMessageClick(chatMessage.getImageUri(), position);
                }
            });

        } else if (holder instanceof ReplyViewHolder) {
            ReplyViewHolder replyHolder = (ReplyViewHolder) holder;
            replyHolder.originalMessageText.setText(chatMessage.getOriginalMessage());
            replyHolder.replyText.setText(chatMessage.getReplyText());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.isImageMessage()) {
            return VIEW_TYPE_IMAGE;
        } else if (message.isReply()) {
            return VIEW_TYPE_REPLY;
        }
        return VIEW_TYPE_MESSAGE;
    }

    public interface OnMessageClickListener {
        void onMessageClick(String message, int position);
        void onDeleteMessage(int position);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, reactionText, timestampText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            reactionText = itemView.findViewById(R.id.reactionText);
            timestampText = itemView.findViewById(R.id.timestampText);
            // statusText = itemView.findViewById(R.id.statusText); // Removed this line
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView timestampText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.messageImage);
            timestampText = itemView.findViewById(R.id.timestampText);
        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyText, originalMessageText;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyText = itemView.findViewById(R.id.replyText);
            originalMessageText = itemView.findViewById(R.id.originalMessageText);
        }
    }
}
