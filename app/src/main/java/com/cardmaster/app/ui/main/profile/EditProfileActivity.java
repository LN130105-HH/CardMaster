package com.cardmaster.app.ui.main.profile;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.R;
import com.cardmaster.app.data.auth.FirebaseAuthManager;
import com.cardmaster.app.databinding.ActivityEditProfileBinding;
import com.cardmaster.app.util.SessionManager;

import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private SessionManager sessionManager;
    private FirebaseAuthManager authManager;

    private String selectedAvatarUri = "";
    private String selectedDob = "";
    private int dobYear = 2003, dobMonth = 7, dobDay = 15;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedAvatarUri = uri.toString();
                    binding.ivAvatarPreview.setImageURI(uri);
                    Toast.makeText(this, "Đã chọn ảnh đại diện mới", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        authManager = new FirebaseAuthManager(this);

        loadInitialData();
        setupListeners();
    }

    private void loadInitialData() {
        binding.etName.setText(sessionManager.getUserName());
        binding.etEmail.setText(sessionManager.getUserEmail());

        // Gender
        String gender = sessionManager.getGender();
        if ("Nữ".equalsIgnoreCase(gender)) {
            binding.rbGenderFemale.setChecked(true);
        } else if ("Khác".equalsIgnoreCase(gender)) {
            binding.rbGenderOther.setChecked(true);
        } else if ("Không muốn cung cấp".equalsIgnoreCase(gender)) {
            binding.rbGenderUndisclosed.setChecked(true);
        } else {
            binding.rbGenderMale.setChecked(true);
        }

        // DOB
        selectedDob = sessionManager.getDateOfBirth();
        if (TextUtils.isEmpty(selectedDob)) {
            selectedDob = "15/08/2003";
        }
        binding.tvSelectedDob.setText(selectedDob);

        // Avatar
        selectedAvatarUri = sessionManager.getAvatarUri();
        if (!TextUtils.isEmpty(selectedAvatarUri)) {
            try {
                binding.ivAvatarPreview.setImageURI(Uri.parse(selectedAvatarUri));
            } catch (Exception e) {
                binding.ivAvatarPreview.setImageResource(R.drawable.ic_person);
            }
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        // Avatar selection
        binding.ivAvatarPreview.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        // Date Picker
        binding.btnPickDob.setOnClickListener(v -> showDatePicker());

        // Save
        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - 20; // default around ~20 years old
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            Calendar chosen = Calendar.getInstance();
            chosen.set(y, m, d);

            if (chosen.getTimeInMillis() > System.currentTimeMillis()) {
                binding.tvDobError.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Ngày sinh không được ở tương lai!", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.tvDobError.setVisibility(View.GONE);
            dobYear = y;
            dobMonth = m;
            dobDay = d;
            selectedDob = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, (m + 1), y);
            binding.tvSelectedDob.setText(selectedDob);
        }, year, month, day);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void saveProfile() {
        String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
        if (TextUtils.isEmpty(name)) {
            binding.tilName.setError("Vui lòng nhập họ và tên");
            return;
        } else {
            binding.tilName.setError(null);
        }

        // Gender check
        String gender;
        int checkedId = binding.rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbGenderFemale) {
            gender = "Nữ";
        } else if (checkedId == R.id.rbGenderOther) {
            gender = "Khác";
        } else if (checkedId == R.id.rbGenderUndisclosed) {
            gender = "Không muốn cung cấp";
        } else if (checkedId == R.id.rbGenderMale) {
            gender = "Nam";
        } else {
            binding.tvGenderError.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.tvGenderError.setVisibility(View.GONE);

        binding.btnSave.setEnabled(false);

        authManager.updateProfile(name, selectedAvatarUri, gender, selectedDob, new FirebaseAuthManager.SimpleCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(EditProfileActivity.this, "Đã lưu thông tin hồ sơ lên Firebase thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(EditProfileActivity.this, "Lỗi cập nhật: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
