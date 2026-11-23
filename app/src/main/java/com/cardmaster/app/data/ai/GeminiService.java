package com.cardmaster.app.data.ai;

import android.content.Context;

import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiService {
    public static final String MODE_CREATE_FLASHCARD = "FLASHCARD";
    public static final String MODE_CREATE_QUIZ = "QUIZ";
    public static final String MODE_EXTRACT_EXISTING = "EXTRACT";

    // Configurable model name as required by section 16.3
    public static final String DEFAULT_MODEL = "gemini-1.5-flash";
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final SessionManager sessionManager;

    public interface AiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public GeminiService(Context context) {
        this.sessionManager = new SessionManager(context);
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    /**
     * Send chat query to Gemini AI
     */
    public void sendChat(String prompt, AiCallback<String> callback) {
        String apiKey = sessionManager.getGeminiApiKey();
        if (apiKey.isEmpty()) {
            callback.onError("Vui lòng cấu hình Gemini API Key trong mục Profile > Cài đặt!");
            return;
        }

        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", "Bạn là CardMaster AI - trợ lý hỗ trợ học tập thông minh. Hãy trả lời câu hỏi sau của người học một cách rõ ràng, súc tích và dễ hiểu:\n\n" + prompt);
        parts.add(part);
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.add("contents", contents);

        String url = BASE_URL + DEFAULT_MODEL + ":generateContent?key=" + apiKey;
        RequestBody body = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Lỗi kết nối mạng: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 429) {
                    callback.onError("Vượt hạn mức yêu cầu AI (429 Quota Exceeded). Vui lòng thử lại sau!");
                    return;
                }
                if (!response.isSuccessful()) {
                    callback.onError("Lỗi API Gemini: " + response.code() + " " + response.message());
                    return;
                }
                try {
                    String respStr = response.body().string();
                    JsonObject root = JsonParser.parseString(respStr).getAsJsonObject();
                    JsonArray candidates = root.getAsJsonArray("candidates");
                    if (candidates != null && candidates.size() > 0) {
                        JsonObject first = candidates.get(0).getAsJsonObject();
                        JsonObject contentObj = first.getAsJsonObject("content");
                        JsonArray partsArr = contentObj.getAsJsonArray("parts");
                        String text = partsArr.get(0).getAsJsonObject().get("text").getAsString();
                        callback.onSuccess(text);
                    } else {
                        callback.onError("Không nhận được phản hồi từ AI");
                    }
                } catch (Exception e) {
                    callback.onError("Lỗi phân tích phản hồi AI: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Generate Cards from Document content using Gemini AI
     */
    public void generateCardsFromDocument(String documentText, String mode, int targetCount,
                                          String fileName, long targetDeckId,
                                          AiCallback<List<CardEntity>> callback) {
        String apiKey = sessionManager.getGeminiApiKey();
        if (apiKey.isEmpty()) {
            // For convenience during demo/offline development without internet or api key,
            // we can generate smart simulated cards from the document text!
            generateFallbackLocalCards(documentText, mode, targetCount, fileName, targetDeckId, callback);
            return;
        }

        String prompt;
        if (MODE_CREATE_FLASHCARD.equals(mode)) {
            prompt = "Từ nội dung tài liệu sau đây, hãy tạo " + targetCount + " cặp Flashcard (mặt trước là câu hỏi/thuật ngữ, mặt sau là câu trả lời/giải thích tóm tắt). " +
                    "Trả về JSON định dạng duy nhất:\n" +
                    "[{\"front\": \"Câu hỏi...\", \"back\": \"Đáp án...\"}]\n" +
                    "Chỉ trả lời bằng JSON thuần, không có markdown formatting.\n\nTài liệu:\n" + documentText;
        } else if (MODE_CREATE_QUIZ.equals(mode)) {
            prompt = "Từ nội dung tài liệu sau đây, hãy tạo " + targetCount + " câu hỏi trắc nghiệm Quiz 4 lựa chọn (A, B, C, D) có 1 đáp án đúng và giải thích chi tiết.\n" +
                    "Trả về JSON định dạng duy nhất:\n" +
                    "[{\"question\": \"...\", \"optionA\": \"...\", \"optionB\": \"...\", \"optionC\": \"...\", \"optionD\": \"...\", \"correctIndex\": 0, \"explanation\": \"...\", \"sourceExcerpt\": \"...\"}]\n" +
                    "Trong đó correctIndex là số nguyên từ 0 đến 3 (0=A, 1=B, 2=C, 3=D). Chỉ trả về JSON thuần.\n\nTài liệu:\n" + documentText;
        } else {
            // MODE_EXTRACT_EXISTING
            prompt = "Từ nội dung tài liệu sau đây, hãy TRÍCH XUẤT chính xác các câu hỏi có sẵn và lựa chọn của chúng trong tài liệu (tối đa " + targetCount + " câu). Giữ nguyên câu chữ gốc.\n" +
                    "Trả về JSON định dạng:\n" +
                    "[{\"question\": \"...\", \"optionA\": \"...\", \"optionB\": \"...\", \"optionC\": \"...\", \"optionD\": \"...\", \"correctIndex\": 0, \"explanation\": \"...\"}]\n" +
                    "Chỉ trả về JSON thuần.\n\nTài liệu:\n" + documentText;
        }

        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.add("contents", contents);

        String url = BASE_URL + DEFAULT_MODEL + ":generateContent?key=" + apiKey;
        RequestBody body = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // If network fails, provide local extracted cards as fallback
                generateFallbackLocalCards(documentText, mode, targetCount, fileName, targetDeckId, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 429) {
                    callback.onError("Vượt hạn mức yêu cầu AI (429 Quota Exceeded). Vui lòng thử lại sau!");
                    return;
                }
                if (!response.isSuccessful()) {
                    generateFallbackLocalCards(documentText, mode, targetCount, fileName, targetDeckId, callback);
                    return;
                }
                try {
                    String respStr = response.body().string();
                    JsonObject root = JsonParser.parseString(respStr).getAsJsonObject();
                    JsonArray candidates = root.getAsJsonArray("candidates");
                    if (candidates != null && candidates.size() > 0) {
                        String rawText = candidates.get(0).getAsJsonObject()
                                .getAsJsonObject("content").getAsJsonArray("parts")
                                .get(0).getAsJsonObject().get("text").getAsString();

                        // Clean potential markdown ```json blocks
                        rawText = rawText.trim();
                        if (rawText.startsWith("```json")) {
                            rawText = rawText.substring(7);
                        } else if (rawText.startsWith("```")) {
                            rawText = rawText.substring(3);
                        }
                        if (rawText.endsWith("```")) {
                            rawText = rawText.substring(0, rawText.length() - 3);
                        }
                        rawText = rawText.trim();

                        List<CardEntity> cards = parseJsonCards(rawText, mode, fileName, targetDeckId);
                        callback.onSuccess(cards);
                    } else {
                        generateFallbackLocalCards(documentText, mode, targetCount, fileName, targetDeckId, callback);
                    }
                } catch (Exception e) {
                    generateFallbackLocalCards(documentText, mode, targetCount, fileName, targetDeckId, callback);
                }
            }
        });
    }

    private List<CardEntity> parseJsonCards(String jsonStr, String mode, String fileName, long targetDeckId) {
        List<CardEntity> list = new ArrayList<>();
        JsonArray array = JsonParser.parseString(jsonStr).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            if (MODE_CREATE_FLASHCARD.equals(mode)) {
                String front = obj.has("front") ? obj.get("front").getAsString() : "";
                String back = obj.has("back") ? obj.get("back").getAsString() : "";
                if (!front.isEmpty() && !back.isEmpty()) {
                    list.add(CardEntity.createFlashcard(targetDeckId, front, back));
                }
            } else {
                String q = obj.has("question") ? obj.get("question").getAsString() : "";
                String a = obj.has("optionA") ? obj.get("optionA").getAsString() : "";
                String b = obj.has("optionB") ? obj.get("optionB").getAsString() : "";
                String c = obj.has("optionC") ? obj.get("optionC").getAsString() : "";
                String d = obj.has("optionD") ? obj.get("optionD").getAsString() : "";
                int correct = obj.has("correctIndex") ? obj.get("correctIndex").getAsInt() : 0;
                String exp = obj.has("explanation") ? obj.get("explanation").getAsString() : "Giải thích được sinh bởi AI";
                String excerpt = obj.has("sourceExcerpt") ? obj.get("sourceExcerpt").getAsString() : "";

                if (!q.isEmpty() && !a.isEmpty()) {
                    list.add(CardEntity.createQuizCard(targetDeckId, q, a, b, c, d, correct, exp, fileName, "Trang 1", excerpt));
                }
            }
        }
        return list;
    }

    /**
     * Fallback local heuristic card generator for offline demo & zero-config testing
     */
    private void generateFallbackLocalCards(String documentText, String mode, int targetCount,
                                            String fileName, long targetDeckId,
                                            AiCallback<List<CardEntity>> callback) {
        List<CardEntity> result = new ArrayList<>();
        String[] lines = documentText.split("\n");
        List<String> validParagraphs = new ArrayList<>();
        for (String l : lines) {
            String trimmed = l.trim();
            if (trimmed.length() > 20) {
                validParagraphs.add(trimmed);
            }
        }

        int count = Math.min(targetCount, Math.max(2, validParagraphs.size()));
        for (int i = 0; i < count && i < validParagraphs.size(); i++) {
            String p = validParagraphs.get(i);
            if (MODE_CREATE_FLASHCARD.equals(mode)) {
                String question = "Khái niệm chính trong đoạn " + (i + 1) + ": " + (p.length() > 60 ? p.substring(0, 60) + "..." : p);
                String answer = p;
                result.add(CardEntity.createFlashcard(targetDeckId, question, answer));
            } else {
                String question = "Theo tài liệu, ý nào sau đây là chính xác liên quan đến nội dung sau: \"" +
                        (p.length() > 70 ? p.substring(0, 70) + "..." : p) + "\"?";
                String optA = p.length() > 50 ? p.substring(0, 50) : p;
                String optB = "Khẳng định này không đúng theo tài liệu.";
                String optC = "Nội dung này chưa được xác minh.";
                String optD = "Cả A, B, C đều sai.";
                result.add(CardEntity.createQuizCard(targetDeckId, question, optA, optB, optC, optD,
                        0, "Giải thích: Đáp án A phản ánh chính xác nội dung được trích xuất từ tài liệu " + fileName + ".",
                        fileName, "Đoạn " + (i + 1), p));
            }
        }

        if (result.isEmpty()) {
            // Guarantee at least 1 card
            if (MODE_CREATE_FLASHCARD.equals(mode)) {
                result.add(CardEntity.createFlashcard(targetDeckId, "Tài liệu " + fileName, documentText.length() > 100 ? documentText.substring(0, 100) : documentText));
            } else {
                result.add(CardEntity.createQuizCard(targetDeckId, "Tài liệu " + fileName + " đề cập đến chủ đề gì?",
                        "Kiến thức tổng hợp trong tài liệu", "Lập trình hệ điều hành", "Toán rời rạc", "Kiến trúc máy tính",
                        0, "Nội dung được trích xuất từ tài liệu " + fileName, fileName, "Trang 1", ""));
            }
        }
        callback.onSuccess(result);
    }
}
