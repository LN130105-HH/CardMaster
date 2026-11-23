package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String avatarUri;
    private long createdAt;

    public UserEntity(String fullName, String email, String passwordHash, String avatarUri, long createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUri = avatarUri;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getAvatarUri() { return avatarUri; }
    public void setAvatarUri(String avatarUri) { this.avatarUri = avatarUri; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
