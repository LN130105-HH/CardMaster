package com.cardmaster.app.ui.planner;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class StudyPlanEditorDialog {

    public interface OnPlanSavedListener {
        void onSaved();
    }

    public static void show(Context context, AppRepository repository, long userId,
                            StudyPlanEntity editPlan, long defaultDeckId,
                            OnPlanSavedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(editPlan != null ? "Chỉnh sửa lịch ôn tập" : "Tạo lịch ôn tập mới");

        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View view = inflater.inflate(R.layout.dialog_study_plan_editor, null);

        Spinner spDeck = view.findViewById(R.id.spPlanDeck);
        TextView tvDateTime = view.findViewById(R.id.tvSelectDateTime);
        Spinner spRepeat = view.findViewById(R.id.spRepeatMode);
        CheckBox cbRemind = view.findViewById(R.id.cbRemind);

        List<DeckEntity> decks = new ArrayList<>();
        List<String> deckTitles = new ArrayList<>();
        ArrayAdapter<String> deckAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, deckTitles);
        spDeck.setAdapter(deckAdapter);

        String[] repeats = new String[]{"Một lần duy nhất", "Lặp lại hàng ngày", "Lặp lại hàng tuần"};
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, repeats);
        spRepeat.setAdapter(repeatAdapter);

        final Calendar calendar = Calendar.getInstance();
        if (editPlan != null) {
            calendar.setTimeInMillis(editPlan.getScheduledTime());
            cbRemind.setChecked(editPlan.isRemind());
            if (StudyPlanEntity.REPEAT_DAILY.equals(editPlan.getRepeatMode())) spRepeat.setSelection(1);
            else if (StudyPlanEntity.REPEAT_WEEKLY.equals(editPlan.getRepeatMode())) spRepeat.setSelection(2);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            cbRemind.setChecked(true);
        }
        tvDateTime.setText(DateUtils.formatDateTime(calendar.getTimeInMillis()));

        cbRemind.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED && context instanceof Activity) {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            1001
                    );
                }
            }
        });

        tvDateTime.setOnClickListener(v -> {
            new DatePickerDialog(context, (dView, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new TimePickerDialog(context, (tView, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    tvDateTime.setText(DateUtils.formatDateTime(calendar.getTimeInMillis()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            List<DeckEntity> list = AppDatabase.getInstance(context).deckDao().getDecks(userId);
            view.post(() -> {
                decks.clear();
                deckTitles.clear();
                if (list != null && !list.isEmpty()) {
                    decks.addAll(list);
                    int sel = 0;
                    for (int i = 0; i < list.size(); i++) {
                        DeckEntity d = list.get(i);
                        deckTitles.add(d.getTitle());
                        long targetId = (editPlan != null ? editPlan.getDeckId() : defaultDeckId);
                        if (d.getId() == targetId) sel = i;
                    }
                    deckAdapter.notifyDataSetChanged();
                    spDeck.setSelection(sel);
                } else {
                    deckTitles.add("Chưa có bộ thẻ");
                    deckAdapter.notifyDataSetChanged();
                }
            });
        });

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            int selectedDeckPos = spDeck.getSelectedItemPosition();
            if (selectedDeckPos < 0 || selectedDeckPos >= decks.size()) {
                Toast.makeText(context, "Vui lòng chọn bộ thẻ", Toast.LENGTH_SHORT).show();
                return;
            }
            long deckId = decks.get(selectedDeckPos).getId();

            String repeatMode = StudyPlanEntity.REPEAT_ONCE;
            if (spRepeat.getSelectedItemPosition() == 1) repeatMode = StudyPlanEntity.REPEAT_DAILY;
            else if (spRepeat.getSelectedItemPosition() == 2) repeatMode = StudyPlanEntity.REPEAT_WEEKLY;

            boolean remind = cbRemind.isChecked();
            long scheduledTime = calendar.getTimeInMillis();

            if (editPlan != null) {
                editPlan.setDeckId(deckId);
                editPlan.setScheduledTime(scheduledTime);
                editPlan.setRepeatMode(repeatMode);
                editPlan.setRemind(remind);
                editPlan.setSyncStatus(StudyPlanEntity.SYNC_PENDING); // Mark for calendar sync
                editPlan.setLastModifiedAt(System.currentTimeMillis());
                repository.updatePlan(editPlan, () -> {
                    if (listener != null) listener.onSaved();
                });
            } else {
                StudyPlanEntity newPlan = new StudyPlanEntity(
                        deckId, userId, scheduledTime, repeatMode, remind,
                        null, 0, StudyPlanEntity.SYNC_PENDING, System.currentTimeMillis()
                );
                repository.insertPlan(newPlan, () -> {
                    if (listener != null) listener.onSaved();
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}