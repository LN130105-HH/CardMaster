package com.cardmaster.app.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    public static final long MAX_FILE_SIZE_BYTES = 20 * 1024 * 1024; // 20 MB
    public static final long WARN_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            } catch (Exception ignored) {}
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result != null ? result : "file";
    }

    public static long getFileSize(Context context, Uri uri) {
        long size = 0;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (index >= 0) {
                    size = cursor.getLong(index);
                }
            }
        } catch (Exception ignored) {}
        return size;
    }

    /**
     * Reads text content from TXT, CSV, or extracts text from DOCX (OpenXML)
     */
    public static String extractTextContent(Context context, Uri uri, String fileName) throws Exception {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".docx")) {
            return extractTextFromDocx(context, uri);
        } else {
            // Read as text
            StringBuilder sb = new StringBuilder();
            try (InputStream is = context.getContentResolver().openInputStream(uri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        }
    }

    /**
     * DOCX files are zip archives containing 'word/document.xml'.
     * Extracts text inside <w:t> tags.
     */
    private static String extractTextFromDocx(Context context, Uri uri) throws Exception {
        StringBuilder textBuilder = new StringBuilder();
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("word/document.xml".equals(entry.getName())) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
                    StringBuilder xmlContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        xmlContent.append(line);
                    }

                    // Extract all <w:p> paragraphs and <w:t> texts
                    Pattern pPattern = Pattern.compile("<w:p[ >](.*?)</w:p>");
                    Matcher pMatcher = pPattern.matcher(xmlContent.toString());
                    Pattern tPattern = Pattern.compile("<w:t[^>]*>(.*?)</w:t>");

                    while (pMatcher.find()) {
                        String pBlock = pMatcher.group(1);
                        Matcher tMatcher = tPattern.matcher(pBlock);
                        StringBuilder pText = new StringBuilder();
                        while (tMatcher.find()) {
                            pText.append(tMatcher.group(1));
                        }
                        if (pText.length() > 0) {
                            textBuilder.append(pText).append("\n");
                        }
                    }
                    break;
                }
            }
        }
        return textBuilder.toString();
    }
}
