package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationEntity {
    public static final String TYPE_REMINDER = "REMINDER";
    public static final String TYPE_AI = "AI";
    public static final String TYPE_IMPORT = "IMPORT";
    public static final String TYPE_SYSTEM = "SYSTEM";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long userId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private long createdAt;

    public NotificationEntity(long userId, String title, String message, String type, boolean isRead, long createdAt) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
