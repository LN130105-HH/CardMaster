package com.cardmaster.app.ui.main.aichat.model;

public class ChatMessage {
    public static final int TYPE_USER = 1;
    public static final int TYPE_AI = 2;

    private int type;
    private String text;
    private long timestamp;

    public ChatMessage(int type, String text, long timestamp) {
        this.type = type;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getType() { return type; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}
