package com.cardmaster.app.data.io;

import android.content.Context;
import android.net.Uri;

import com.cardmaster.app.data.local.entity.CardEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CsvImporter {

    public static class ImportItem {
        public CardEntity card;
        public boolean isSelected = true;
        public boolean isValid = true;
        public String errorMessage = "";
        public int lineNumber;

        public ImportItem(CardEntity card, int lineNumber) {
            this.card = card;
            this.lineNumber = lineNumber;
        }
    }

    public static List<ImportItem> parseFile(Context context, Uri uri, String fileName, long targetDeckId) throws Exception {
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return parseXlsx(context, uri, targetDeckId);
        } else {
            return parseCsv(context, uri, targetDeckId);
        }
    }

    private static List<ImportItem> parseCsv(Context context, Uri uri, long targetDeckId) throws Exception {
        List<ImportItem> results = new ArrayList<>();
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) return results;

            // Remove UTF-8 BOM if present
            if (headerLine.startsWith("\uFEFF")) {
                headerLine = headerLine.substring(1);
            }

            List<String> headers = parseCsvLine(headerLine);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                headerMap.put(headers.get(i).trim().toLowerCase(), i);
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                List<String> row = parseCsvLine(line);
                ImportItem item = buildCardFromRow(row, headerMap, targetDeckId, lineNumber);
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Parse Excel (.xlsx) file without external bulky libraries
     */
    private static List<ImportItem> parseXlsx(Context context, Uri uri, long targetDeckId) throws Exception {
        List<ImportItem> results = new ArrayList<>();
        List<String> sharedStrings = new ArrayList<>();

        // 1st pass: extract shared strings
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("xl/sharedStrings.xml".equals(entry.getName())) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
                    StringBuilder xml = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) xml.append(line);

                    Pattern tPattern = Pattern.compile("<t[^>]*>(.*?)</t>");
                    Matcher matcher = tPattern.matcher(xml.toString());
                    while (matcher.find()) {
                        sharedStrings.add(unescapeXml(matcher.group(1)));
                    }
                    break;
                }
            }
        }

        // 2nd pass: parse sheet1.xml
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("xl/worksheets/sheet1.xml".equals(entry.getName())) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
                    StringBuilder xml = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) xml.append(line);

                    // Parse <row> tags
                    Pattern rowPattern = Pattern.compile("<row[^>]*>(.*?)</row>");
                    Matcher rowMatcher = rowPattern.matcher(xml.toString());

                    Map<String, Integer> headerMap = new HashMap<>();
                    int rowNum = 0;

                    while (rowMatcher.find()) {
                        rowNum++;
                        String rowXml = rowMatcher.group(1);
                        List<String> cellValues = extractRowCells(rowXml, sharedStrings);

                        if (rowNum == 1) {
                            for (int i = 0; i < cellValues.size(); i++) {
                                headerMap.put(cellValues.get(i).trim().toLowerCase(), i);
                            }
                        } else {
                            ImportItem item = buildCardFromRow(cellValues, headerMap, targetDeckId, rowNum);
                            results.add(item);
                        }
                    }
                    break;
                }
            }
        }

        return results;
    }

    private static List<String> extractRowCells(String rowXml, List<String> sharedStrings) {
        List<String> cells = new ArrayList<>();
        Pattern cPattern = Pattern.compile("<c[^>]*?(?:t=\"([^\"]*)\")?[^>]*>(?:<v>([^<]*)</v>)?</c>");
        Matcher cMatcher = cPattern.matcher(rowXml);

        while (cMatcher.find()) {
            String type = cMatcher.group(1);
            String val = cMatcher.group(2);
            if (val == null) {
                cells.add("");
                continue;
            }
            if ("s".equals(type)) {
                try {
                    int sIndex = Integer.parseInt(val);
                    if (sIndex < sharedStrings.size()) {
                        cells.add(sharedStrings.get(sIndex));
                    } else {
                        cells.add(val);
                    }
                } catch (Exception e) {
                    cells.add(val);
                }
            } else {
                cells.add(val);
            }
        }
        return cells;
    }

    private static ImportItem buildCardFromRow(List<String> row, Map<String, Integer> headerMap, long targetDeckId, int lineNumber) {
        String type = getVal(row, headerMap, "type");
        if (type.isEmpty()) {
            // Check if question exists -> QUIZ, else if front exists -> FLASHCARD
            if (!getVal(row, headerMap, "question").isEmpty()) {
                type = CardEntity.TYPE_QUIZ;
            } else {
                type = CardEntity.TYPE_FLASHCARD;
            }
        }

        if (type.equalsIgnoreCase("quiz") || type.equalsIgnoreCase("quiz_card")) {
            String question = getVal(row, headerMap, "question");
            String optA = getVal(row, headerMap, "option_a");
            if (optA.isEmpty()) optA = getVal(row, headerMap, "optiona");
            String optB = getVal(row, headerMap, "option_b");
            if (optB.isEmpty()) optB = getVal(row, headerMap, "optionb");
            String optC = getVal(row, headerMap, "option_c");
            if (optC.isEmpty()) optC = getVal(row, headerMap, "optionc");
            String optD = getVal(row, headerMap, "option_d");
            if (optD.isEmpty()) optD = getVal(row, headerMap, "optiond");

            String correctStr = getVal(row, headerMap, "correct_index");
            if (correctStr.isEmpty()) correctStr = getVal(row, headerMap, "correct");

            int correctIdx = 0;
            try {
                if (correctStr.equalsIgnoreCase("A") || correctStr.equals("0")) correctIdx = 0;
                else if (correctStr.equalsIgnoreCase("B") || correctStr.equals("1")) correctIdx = 1;
                else if (correctStr.equalsIgnoreCase("C") || correctStr.equals("2")) correctIdx = 2;
                else if (correctStr.equalsIgnoreCase("D") || correctStr.equals("3")) correctIdx = 3;
                else correctIdx = Integer.parseInt(correctStr);
            } catch (Exception ignored) {}

            String explanation = getVal(row, headerMap, "explanation");
            String sourceFile = getVal(row, headerMap, "source_file");
            String sourcePage = getVal(row, headerMap, "source_page");
            String sourceExcerpt = getVal(row, headerMap, "source_excerpt");

            CardEntity card = CardEntity.createQuizCard(targetDeckId, question, optA, optB, optC, optD, correctIdx, explanation, sourceFile, sourcePage, sourceExcerpt);
            ImportItem item = new ImportItem(card, lineNumber);

            if (question.isEmpty() || optA.isEmpty() || optB.isEmpty()) {
                item.isValid = false;
                item.errorMessage = "Thiếu câu hỏi hoặc lựa chọn đáp án";
                item.isSelected = false;
            }
            return item;
        } else {
            // Flashcard
            String front = getVal(row, headerMap, "front");
            String back = getVal(row, headerMap, "back");
            CardEntity card = CardEntity.createFlashcard(targetDeckId, front, back);
            ImportItem item = new ImportItem(card, lineNumber);

            if (front.isEmpty() || back.isEmpty()) {
                item.isValid = false;
                item.errorMessage = "Thiếu mặt trước hoặc mặt sau của thẻ";
                item.isSelected = false;
            }
            return item;
        }
    }

    private static String getVal(List<String> row, Map<String, Integer> headerMap, String colName) {
        Integer idx = headerMap.get(colName.toLowerCase());
        if (idx != null && idx < row.size()) {
            return row.get(idx).trim();
        }
        return "";
    }

    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        result.add(cur.toString().trim());
        return result;
    }

    private static String unescapeXml(String xml) {
        return xml.replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'");
    }
}
