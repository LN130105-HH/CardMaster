package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_plans")
public class StudyPlanEntity {
    public static final String SYNC_SYNCED = "SYNCED";
    public static final String SYNC_PENDING = "PENDING_SYNC";
    public static final String SYNC_CONFLICT = "CONFLICT";
    public static final String SYNC_DELETED = "DELETED";

    public static final String REPEAT_ONCE = "ONCE";
    public static final String REPEAT_DAILY = "DAILY";
    public static final String REPEAT_WEEKLY = "WEEKLY";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long deckId;
    private long userId;
    private long scheduledTime;
    private String repeatMode; // ONCE, DAILY, WEEKLY
    private boolean isRemind;
    private String calendarEventId;
    private long lastSyncedAt;
    private String syncStatus; // SYNCED, PENDING_SYNC, CONFLICT, DELETED
    private long lastModifiedAt;
    private long createdAt;

    public StudyPlanEntity(long deckId, long userId, long scheduledTime, String repeatMode, boolean isRemind, String calendarEventId, long lastSyncedAt, String syncStatus, long createdAt) {
        this.deckId = deckId;
        this.userId = userId;
        this.scheduledTime = scheduledTime;
        this.repeatMode = repeatMode;
        this.isRemind = isRemind;
        this.calendarEventId = calendarEventId;
        this.lastSyncedAt = lastSyncedAt;
        this.syncStatus = syncStatus;
        this.lastModifiedAt = createdAt;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getDeckId() { return deckId; }
    public void setDeckId(long deckId) { this.deckId = deckId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getRepeatMode() { return repeatMode; }
    public void setRepeatMode(String repeatMode) { this.repeatMode = repeatMode; }

    public boolean isRemind() { return isRemind; }
    public void setRemind(boolean remind) { isRemind = remind; }

    public String getCalendarEventId() { return calendarEventId; }
    public void setCalendarEventId(String calendarEventId) { this.calendarEventId = calendarEventId; }

    public long getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(long lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }

    public long getLastModifiedAt() { return lastModifiedAt; }
    public void setLastModifiedAt(long lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
