package com.example.tutorapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class FindTutorActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress, etSalary, etNote;
    private Spinner spinnerDistrict;
    private Button btnSelectSubject, btnSubmit;
    private TextView tvSelectedSubjects;

    // 初中科目（與 DoTutorActivity 一致）
    private final String[] juniorSubjects = {
            "初中語文", "初中數學", "初中英語", "初中物理", "初中化學", "初中生物", "初中歷史", "初中地理", "初中政治"
    };

    // 高中科目（與 DoTutorActivity 一致）
    private final String[] seniorSubjects = {
            "高中語文", "高中數學", "高中英語", "高中物理", "高中化學", "高中生物", "高中地理", "高中歷史", "高中政治"
    };

    private final List<String> selectedSubjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor);

        // 初始化欄位
        etName = findViewById(R.id.edit_child_name);
        etPhone = findViewById(R.id.edit_phone);
        etAddress = findViewById(R.id.edit_address);
        etSalary = findViewById(R.id.edit_salary);
        etNote = findViewById(R.id.edit_note);
        spinnerDistrict = findViewById(R.id.spinner_district);
        btnSelectSubject = findViewById(R.id.btn_select_subject);
        tvSelectedSubjects = findViewById(R.id.tv_selected_subjects);
        btnSubmit = findViewById(R.id.btn_submit);

        // 地區 Spinner 初始化
        String[] districts = {"越秀區", "天河區", "白雲區", "海珠區", "黃埔區", "荔灣區", "番禺區", "花都區", "南沙區", "增城區", "從化區"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        spinnerDistrict.setAdapter(adapter);

        // 選擇科目（與 DoTutor 相同邏輯）
        btnSelectSubject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(FindTutorActivity.this);
            builder.setTitle("選擇科目分類");

            String[] gradeOptions = {"初中", "高中"};
            builder.setItems(gradeOptions, (dialogInterface, which) -> {
                String[] subjectArray = (which == 0) ? juniorSubjects : seniorSubjects;
                boolean[] checked = new boolean[subjectArray.length];
                List<String> tempSelected = new ArrayList<>(selectedSubjects);

                AlertDialog.Builder subjectDialog = new AlertDialog.Builder(FindTutorActivity.this);
                subjectDialog.setTitle("選擇" + gradeOptions[which] + "科目（可多選）");

                subjectDialog.setMultiChoiceItems(subjectArray, checked, (dialog, index, isChecked) -> {
                    String subject = subjectArray[index];
                    if (isChecked) {
                        if (!tempSelected.contains(subject)) tempSelected.add(subject);
                    } else {
                        tempSelected.remove(subject);
                    }
                });

                subjectDialog.setPositiveButton("確定", (dialog, w) -> {
                    selectedSubjects.clear();
                    selectedSubjects.addAll(tempSelected);
                    if (selectedSubjects.isEmpty()) {
                        tvSelectedSubjects.setText("尚未選擇科目");
                    } else {
                        tvSelectedSubjects.setText("已選擇：" + String.join("、", selectedSubjects));
                    }
                });

                subjectDialog.setNegativeButton("取消", null);
                subjectDialog.show();
            });

            builder.show();
        });

        // 發布按鈕
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String district = spinnerDistrict.getSelectedItem().toString();
            String address = etAddress.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();
            String note = etNote.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || salaryStr.isEmpty()) {
                Toast.makeText(this, "請填寫完整資訊", Toast.LENGTH_SHORT).show();
                return;
            }

            int salary = Integer.parseInt(salaryStr);

            // 封裝資料
            FindTutorInfo info = new FindTutorInfo(
                    name,
                    phone,
                    district,
                    address,
                    salary,
                    new ArrayList<>(selectedSubjects),
                    new ArrayList<>(), // 尚未處理的時間段
                    note
            );

            // 回傳資料給 MainActivity
            Intent intent = new Intent();
            intent.putExtra("findTutorInfo", info);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
