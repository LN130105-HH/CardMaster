package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "decks")
public class DeckEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long subjectId;
    private long userId;
    private String title;
    private String description;
    private long createdAt;
    private long updatedAt;

    public DeckEntity(long subjectId, long userId, String title, String description, long createdAt, long updatedAt) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getSubjectId() { return subjectId; }
    public void setSubjectId(long subjectId) { this.subjectId = subjectId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
