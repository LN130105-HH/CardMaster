package com.cardmaster.app.ui.deck;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.entity.SubjectEntity;
import com.cardmaster.app.data.repository.AppRepository;

public class SubjectEditorDialog {

    public interface OnSubjectSavedListener {
        void onSaved();
    }

    public static void show(Context context, AppRepository repository, long userId, SubjectEntity editSubject, OnSubjectSavedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(editSubject != null ? "Chỉnh sửa môn học" : "Thêm môn học mới");

        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View view = inflater.inflate(R.layout.dialog_subject_editor, null);
        EditText etName = view.findViewById(R.id.etSubjectName);
        EditText etDesc = view.findViewById(R.id.etSubjectDesc);

        if (editSubject != null) {
            etName.setText(editSubject.getName());
            etDesc.setText(editSubject.getDescription());
        }

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, "Vui lòng nhập tên môn học", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editSubject != null) {
                editSubject.setName(name);
                editSubject.setDescription(desc);
                repository.updateSubject(editSubject, () -> {
                    if (listener != null) listener.onSaved();
                });
            } else {
                SubjectEntity newSubject = new SubjectEntity(userId, name, desc, System.currentTimeMillis());
                repository.insertSubject(newSubject, () -> {
                    if (listener != null) listener.onSaved();
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
