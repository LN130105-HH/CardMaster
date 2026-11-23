package com.cardmaster.app.ui.deck;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.SubjectEntity;
import com.cardmaster.app.data.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DeckEditorDialog {

    public interface OnDeckSavedListener {
        void onSaved();
    }

    public static void show(Context context, AppRepository repository, long userId, DeckEntity editDeck, OnDeckSavedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(editDeck != null ? "Chỉnh sửa bộ thẻ" : "Tạo bộ thẻ mới");

        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View view = inflater.inflate(R.layout.dialog_deck_editor, null);
        Spinner spSubjects = view.findViewById(R.id.spSubjects);
        EditText etTitle = view.findViewById(R.id.etDeckTitle);
        EditText etDesc = view.findViewById(R.id.etDeckDesc);

        if (editDeck != null) {
            etTitle.setText(editDeck.getTitle());
            etDesc.setText(editDeck.getDescription());
        }

        List<SubjectEntity> subjectList = new ArrayList<>();
        List<String> subjectNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, subjectNames);
        spSubjects.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<SubjectEntity> list = AppDatabase.getInstance(context).subjectDao().getSubjects(userId);
            view.post(() -> {
                subjectList.clear();
                subjectNames.clear();
                if (list != null && !list.isEmpty()) {
                    subjectList.addAll(list);
                    for (SubjectEntity s : list) {
                        subjectNames.add(s.getName());
                    }
                } else {
                    // Create default subject if none
                    SubjectEntity def = new SubjectEntity(userId, "Môn học chung", "", System.currentTimeMillis());
                    subjectList.add(def);
                    subjectNames.add(def.getName());
                }
                adapter.notifyDataSetChanged();

                if (editDeck != null) {
                    for (int i = 0; i < subjectList.size(); i++) {
                        if (subjectList.get(i).getId() == editDeck.getSubjectId()) {
                            spSubjects.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(context, "Vui lòng nhập tên bộ thẻ", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPos = spSubjects.getSelectedItemPosition();
            long subjectId = (selectedPos >= 0 && selectedPos < subjectList.size()) ? subjectList.get(selectedPos).getId() : 1;

            long now = System.currentTimeMillis();
            if (editDeck != null) {
                editDeck.setTitle(title);
                editDeck.setDescription(desc);
                editDeck.setSubjectId(subjectId);
                editDeck.setUpdatedAt(now);
                repository.updateDeck(editDeck, () -> {
                    if (listener != null) listener.onSaved();
                });
            } else {
                DeckEntity newDeck = new DeckEntity(subjectId, userId, title, desc, now, now);
                repository.insertDeck(newDeck, () -> {
                    if (listener != null) listener.onSaved();
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
