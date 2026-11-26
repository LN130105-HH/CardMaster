package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String firebaseUid;
    private String fullName;
    private String email;
    private String passwordHash;
    private String avatarUri;
    private String gender; // Nam / Nữ / Khác / Không muốn cung cấp
    private String dateOfBirth; // dd/MM/yyyy
    private long createdAt;

    public UserEntity() {}

    @Ignore
    public UserEntity(String fullName, String email, String passwordHash, String avatarUri, long createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUri = avatarUri;
        this.createdAt = createdAt;
    }

    @Ignore
    public UserEntity(String firebaseUid, String fullName, String email, String avatarUri, String gender, String dateOfBirth, long createdAt) {
        this.firebaseUid = firebaseUid;
        this.fullName = fullName;
        this.email = email;
        this.avatarUri = avatarUri;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getAvatarUri() { return avatarUri; }
    public void setAvatarUri(String avatarUri) { this.avatarUri = avatarUri; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
