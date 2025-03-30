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

    private final String[] allSubjects = {"語文", "數學", "英語", "物理", "化學", "生物", "歷史", "地理", "政治"};
    private final boolean[] checkedSubjects = new boolean[allSubjects.length];
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

        // 初始化 Spinner（地區）
        String[] districts = {"越秀區", "天河區", "白雲區", "海珠區", "黃埔區", "荔灣區", "番禺區", "花都區", "南沙區", "增城區", "從化區"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        spinnerDistrict.setAdapter(adapter);

        // 科目多選邏輯
        btnSelectSubject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(FindTutorActivity.this);
            builder.setTitle("選擇科目（可多選）");

            builder.setMultiChoiceItems(allSubjects, checkedSubjects, (dialog, which, isChecked) -> {
                if (isChecked) {
                    selectedSubjects.add(allSubjects[which]);
                } else {
                    selectedSubjects.remove(allSubjects[which]);
                }
            });

            builder.setPositiveButton("確定", (dialog, which) -> {
                if (selectedSubjects.isEmpty()) {
                    tvSelectedSubjects.setText("尚未選擇科目");
                } else {
                    tvSelectedSubjects.setText("已選擇：" + String.join("、", selectedSubjects));
                }
            });

            builder.setNegativeButton("取消", null);
            builder.show();
        });

        // 發布按鈕點擊後提交資料
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();
            String note = etNote.getText().toString().trim();
            String district = spinnerDistrict.getSelectedItem().toString();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "請輸入姓名和聯絡電話", Toast.LENGTH_SHORT).show();
                return;
            }

            int salary = salaryStr.isEmpty() ? 0 : Integer.parseInt(salaryStr);

            // 封裝資料
            FindTutorInfo info = new FindTutorInfo(
                    name,
                    phone,
                    district,
                    address,
                    salary,
                    new ArrayList<>(selectedSubjects),
                    new ArrayList<>(), // 未實作，留空
                    note
            );

            Intent resultIntent = new Intent();
            resultIntent.putExtra("findTutorInfo", info);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
