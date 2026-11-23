package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class SubjectEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long userId;
    private String name;
    private String description;
    private long createdAt;

    public SubjectEntity(long userId, String name, String description, long createdAt) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
