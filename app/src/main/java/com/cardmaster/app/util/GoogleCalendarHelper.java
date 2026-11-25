package com.cardmaster.app.util;

import android.content.Context;
import android.content.Intent;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoogleCalendarHelper {

    public static final String CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar.events";
    private static final String BASE_EVENTS_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    public static class SyncResult {
        public enum Status {
            SUCCESS,
            NOT_SIGNED_IN,
            NEED_CONSENT,
            ERROR
        }

        public final Status status;
        public final int syncedCount;
        public final int deletedCount;
        public final String accountEmail;
        public final String errorMessage;
        public final Intent consentIntent;

        private SyncResult(Status status, int syncedCount, int deletedCount, String accountEmail, String errorMessage, Intent consentIntent) {
            this.status = status;
            this.syncedCount = syncedCount;
            this.deletedCount = deletedCount;
            this.accountEmail = accountEmail;
            this.errorMessage = errorMessage;
            this.consentIntent = consentIntent;
        }

        public static SyncResult success(int syncedCount, int deletedCount, String email) {
            return new SyncResult(Status.SUCCESS, syncedCount, deletedCount, email, null, null);
        }

        public static SyncResult notSignedIn() {
            return new SyncResult(Status.NOT_SIGNED_IN, 0, 0, null, "Chưa kết nối tài khoản Google", null);
        }

        public static SyncResult needConsent(Intent intent) {
            return new SyncResult(Status.NEED_CONSENT, 0, 0, null, null, intent);
        }

        public static SyncResult error(String message) {
            return new SyncResult(Status.ERROR, 0, 0, null, message, null);
        }
    }

    public static GoogleSignInClient getSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CALENDAR_SCOPE))
                .build();
        return GoogleSignIn.getClient(context, gso);
    }

    public static GoogleSignInAccount getConnectedAccount(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(CALENDAR_SCOPE))) {
            return account;
        }
        return null;
    }

    public static SyncResult syncUserPlans(Context context, GoogleSignInAccount googleAccount, long userId) {
        if (googleAccount == null || googleAccount.getAccount() == null) {
            return SyncResult.notSignedIn();
        }

        String scopeString = "oauth2:" + CALENDAR_SCOPE;
        String accessToken;
        try {
            accessToken = GoogleAuthUtil.getToken(context, googleAccount.getAccount(), scopeString);
        } catch (UserRecoverableAuthException e) {
            return SyncResult.needConsent(e.getIntent());
        } catch (IOException e) {
            return SyncResult.error("Lỗi kết nối mạng khi xác thực Google: " + e.getMessage());
        } catch (GoogleAuthException e) {
            return SyncResult.error("Lỗi xác thực tài khoản Google: " + e.getMessage());
        }

        AppDatabase db = AppDatabase.getInstance(context);
        List<StudyPlanEntity> pendingPlans = db.studyPlanDao().getPendingSyncPlans(userId);
        List<StudyPlanEntity> deletedPlans = db.studyPlanDao().getDeletedSyncPlans(userId);

        Map<Long, String> deckTitles = new HashMap<>();
        List<DeckEntity> decks = db.deckDao().getDecks(userId);
        if (decks != null) {
            for (DeckEntity deck : decks) {
                deckTitles.put(deck.getId(), deck.getTitle());
            }
        }

        OkHttpClient client = new OkHttpClient();
        int syncedCount = 0;
        int deletedCount = 0;

        try {
            // 1. Đồng bộ thêm mới / cập nhật các lịch đang chờ (chống trùng lặp bằng calendarEventId)
            if (pendingPlans != null) {
                for (StudyPlanEntity plan : pendingPlans) {
                    String deckTitle = deckTitles.getOrDefault(plan.getDeckId(), "Bộ thẻ ôn tập");
                    String payloadJson = buildEventJson(plan, deckTitle);

                    String existingEventId = plan.getCalendarEventId();
                    String finalEventId;

                    if (existingEventId != null && !existingEventId.trim().isEmpty()) {
                        // Lịch đã tồn tại trên Google Calendar -> gọi PUT để cập nhật, không tạo mới
                        finalEventId = updateCalendarEvent(client, accessToken, existingEventId, payloadJson);
                        if (finalEventId == null) {
                            // Nếu sự kiện đã bị xóa thủ công trên web Google Calendar -> tạo lại mới
                            finalEventId = createCalendarEvent(client, accessToken, payloadJson);
                        }
                    } else {
                        // Lịch chưa từng đồng bộ -> gọi POST tạo mới và lưu ID
                        finalEventId = createCalendarEvent(client, accessToken, payloadJson);
                    }

                    plan.setCalendarEventId(finalEventId);
                    plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                    plan.setLastSyncedAt(System.currentTimeMillis());
                    db.studyPlanDao().update(plan);
                    syncedCount++;
                }
            }

            // 2. Xóa các sự kiện trên Google Calendar tương ứng với lịch đã xóa trong app
            if (deletedPlans != null) {
                for (StudyPlanEntity plan : deletedPlans) {
                    String existingEventId = plan.getCalendarEventId();
                    if (existingEventId != null && !existingEventId.trim().isEmpty()) {
                        deleteCalendarEvent(client, accessToken, existingEventId);
                    }
                    db.studyPlanDao().delete(plan);
                    deletedCount++;
                }
            }

            return SyncResult.success(syncedCount, deletedCount, googleAccount.getEmail());

        } catch (TokenExpiredException expired) {
            try {
                GoogleAuthUtil.clearToken(context, accessToken);
            } catch (Exception ignored) {
            }
            return SyncResult.error("Phiên đăng nhập Google đã hết hạn, vui lòng bấm Đồng bộ lại.");
        } catch (IOException e) {
            return SyncResult.error("Đồng bộ thất bại: " + e.getMessage());
        }
    }

    private static String buildEventJson(StudyPlanEntity plan, String deckTitle) {
        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);
        isoFormat.setTimeZone(tz);

        long startMs = plan.getScheduledTime();
        long endMs = startMs + 45L * 60L * 1000L; // Mặc định mỗi phiên ôn tập kéo dài 45 phút

        JsonObject startObj = new JsonObject();
        startObj.addProperty("dateTime", isoFormat.format(new Date(startMs)));
        startObj.addProperty("timeZone", tz.getID());

        JsonObject endObj = new JsonObject();
        endObj.addProperty("dateTime", isoFormat.format(new Date(endMs)));
        endObj.addProperty("timeZone", tz.getID());

        JsonObject eventObj = new JsonObject();
        eventObj.addProperty("summary", "Ôn tập CardMaster: " + deckTitle);
        eventObj.addProperty("description", "Lịch học tự động đồng bộ từ ứng dụng CardMaster.");
        eventObj.add("start", startObj);
        eventObj.add("end", endObj);

        if (StudyPlanEntity.REPEAT_DAILY.equals(plan.getRepeatMode())) {
            JsonArray recurrence = new JsonArray();
            recurrence.add("RRULE:FREQ=DAILY");
            eventObj.add("recurrence", recurrence);
        } else if (StudyPlanEntity.REPEAT_WEEKLY.equals(plan.getRepeatMode())) {
            JsonArray recurrence = new JsonArray();
            recurrence.add("RRULE:FREQ=WEEKLY");
            eventObj.add("recurrence", recurrence);
        }

        return eventObj.toString();
    }

    private static String createCalendarEvent(OkHttpClient client, String token, String jsonBody) throws IOException, TokenExpiredException {
        Request request = new Request.Builder()
                .url(BASE_EVENTS_URL)
                .addHeader("Authorization", "Bearer " + token)
                .post(RequestBody.create(jsonBody, JSON_MEDIA_TYPE))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) throw new TokenExpiredException();
            String body = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + ": " + body);
            }
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            return obj.get("id").getAsString();
        }
    }

    private static String updateCalendarEvent(OkHttpClient client, String token, String eventId, String jsonBody) throws IOException, TokenExpiredException {
        Request request = new Request.Builder()
                .url(BASE_EVENTS_URL + "/" + eventId)
                .addHeader("Authorization", "Bearer " + token)
                .put(RequestBody.create(jsonBody, JSON_MEDIA_TYPE))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) throw new TokenExpiredException();
            if (response.code() == 404 || response.code() == 410) {
                return null; // Sự kiện không còn tồn tại trên Google Calendar
            }
            String body = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + ": " + body);
            }
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            return obj.get("id").getAsString();
        }
    }

    private static void deleteCalendarEvent(OkHttpClient client, String token, String eventId) throws IOException, TokenExpiredException {
        Request request = new Request.Builder()
                .url(BASE_EVENTS_URL + "/" + eventId)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) throw new TokenExpiredException();
            if (response.code() == 404 || response.code() == 410) {
                return; // Đã xóa sẵn trên Google Calendar
            }
            if (!response.isSuccessful()) {
                String body = response.body() != null ? response.body().string() : "";
                throw new IOException("HTTP " + response.code() + ": " + body);
            }
        }
    }

    private static class TokenExpiredException extends Exception {
    }
}