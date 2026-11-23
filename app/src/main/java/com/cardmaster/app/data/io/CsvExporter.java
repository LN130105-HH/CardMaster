package com.cardmaster.app.data.io;

import android.content.Context;
import android.net.Uri;

import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.SubjectEntity;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExporter {

    public static void exportDeckToCsv(Context context, Uri destUri,
                                       SubjectEntity subject, DeckEntity deck,
                                       List<CardEntity> cards) throws Exception {
        String subjectName = subject != null ? subject.getName() : "";
        String deckTitle = deck != null ? deck.getTitle() : "";

        try (OutputStream os = context.getContentResolver().openOutputStream(destUri);
             OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {

            // Write UTF-8 BOM so Excel opens it with proper Vietnamese encoding
            writer.write("\uFEFF");

            // CSV Header
            writer.write("type,front,back,question,option_a,option_b,option_c,option_d,correct_index,explanation,source_file,source_page,source_excerpt,subject,deck\n");

            for (CardEntity card : cards) {
                StringBuilder line = new StringBuilder();
                if (CardEntity.TYPE_QUIZ.equals(card.getType())) {
                    line.append("quiz,");
                    line.append("\"\","); // front
                    line.append("\"\","); // back
                    line.append(escape(card.getQuestion())).append(",");
                    line.append(escape(card.getOptionA())).append(",");
                    line.append(escape(card.getOptionB())).append(",");
                    line.append(escape(card.getOptionC())).append(",");
                    line.append(escape(card.getOptionD())).append(",");
                    line.append(card.getCorrectIndex()).append(",");
                    line.append(escape(card.getExplanation())).append(",");
                    line.append(escape(card.getSourceFile())).append(",");
                    line.append(escape(card.getSourcePage())).append(",");
                    line.append(escape(card.getSourceExcerpt())).append(",");
                } else {
                    line.append("flashcard,");
                    line.append(escape(card.getFront())).append(",");
                    line.append(escape(card.getBack())).append(",");
                    line.append("\"\",\"\",\"\",\"\",\"\",0,\"\","); // empty quiz columns
                    line.append("\"\",\"\",\"\",");
                }
                line.append(escape(subjectName)).append(",");
                line.append(escape(deckTitle)).append("\n");

                writer.write(line.toString());
            }
            writer.flush();
        }
    }

    private static String escape(String val) {
        if (val == null) return "\"\"";
        return "\"" + val.replace("\"", "\"\"").replace("\n", " ") + "\"";
    }
}
