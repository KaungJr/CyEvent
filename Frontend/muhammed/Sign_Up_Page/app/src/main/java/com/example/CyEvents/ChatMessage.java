package com.example.CyEvents;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {

    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_DELIVERED = 2;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    private String message;
    private String replyText;
    private boolean isReply;
    private String reaction;
    private int status;
    private String timestamp;
    private boolean isForwarded;
    private String imageUrl;
    private int messageType;
    private String username; // Added username field

    /**
     * Constructor for text messages.
     *
     * @param message The text message content.
     */
    public ChatMessage(String message) {
        this.message = message;
        this.isReply = false;
        this.reaction = "";
        this.status = STATUS_SENDING;
        this.timestamp = getCurrentTimestamp();
        this.isForwarded = false;
        this.messageType = TYPE_TEXT;
    }

    /**
     * Constructor for reply messages.
     *
     * @param replyText The reply text content.
     * @param originalMessage The original message being replied to.
     */
    public ChatMessage(String replyText, String originalMessage) {
        this.replyText = replyText;
        this.message = originalMessage;
        this.isReply = true;
        this.reaction = "";
        this.timestamp = getCurrentTimestamp();
        this.isForwarded = false;
        this.messageType = TYPE_TEXT;
    }

    /**
     * Constructor for image messages.
     *
     * @param imageUrl The URL of the image.
     * @param isReply Indicates if the message is a reply.
     */
    public ChatMessage(String imageUrl, boolean isReply) {
        this.imageUrl = imageUrl;
        this.isReply = isReply;
        this.messageType = TYPE_IMAGE;
        this.timestamp = getCurrentTimestamp();
        this.reaction = "";
        this.isForwarded = false;
    }

    /**
     * New constructor for a message, reply, and username.
     *
     * @param message The text message content.
     * @param replyText The reply text content (optional).
     * @param username The username of the sender.
     */
    public ChatMessage(String message, String replyText, String username) {
        this.message = message;
        this.replyText = replyText;
        this.username = username;
        this.isReply = replyText != null && !replyText.isEmpty();
        this.reaction = "";
        this.status = STATUS_SENDING;
        this.timestamp = getCurrentTimestamp();
        this.isForwarded = false;
        this.messageType = TYPE_TEXT;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getReplyText() {
        return replyText;
    }

    public boolean isReply() {
        return isReply;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getOriginalMessage() {
        return message;
    }

    public boolean isForwarded() {
        return isForwarded;
    }

    public void setForwarded(boolean forwarded) {
        isForwarded = forwarded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public boolean isImageMessage() {
        return messageType == TYPE_IMAGE;
    }

    public String getImageUri() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }
}
