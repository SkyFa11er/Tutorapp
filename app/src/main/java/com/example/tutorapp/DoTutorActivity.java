package com.example.tutorapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class DoTutorActivity extends AppCompatActivity {

    // UI 元件
    private TextInputEditText editName, editPhone, editSalary, editSalaryNote, editIntro;
    private Button btnSelectSubject, btnSelectDays, btnSelectTime, btnSubmit;
    private TextView tvSelectedSubjects, tvSelectedDays, tvSelectedTime;

    // 科目
    private final String[] juniorSubjects = {
            "初中語文", "初中數學", "初中物化", "初中英語", "初中生物", "初中政治"
    };
    private final String[] seniorSubjects = {
            "高中語文", "高中數學", "高中物化", "高中英語", "高中生物", "高中政治"
    };
    private final ArrayList<String> selectedSubjects = new ArrayList<>();

    // 日期
    private final String[] weekdays = {
            "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
    };
    private final ArrayList<String> selectedDays = new ArrayList<>();

    // 時間段
    private String startTime = null;
    private String endTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_tutor);

        // 初始化欄位
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editSalary = findViewById(R.id.edit_salary);
        editSalaryNote = findViewById(R.id.edit_salary_note);
        editIntro = findViewById(R.id.edit_intro);

        btnSelectSubject = findViewById(R.id.btn_select_subject);
        tvSelectedSubjects = findViewById(R.id.tv_selected_subjects);

        btnSelectDays = findViewById(R.id.btn_select_days);
        tvSelectedDays = findViewById(R.id.tv_selected_days);

        btnSelectTime = findViewById(R.id.btn_select_time);
        tvSelectedTime = findViewById(R.id.tv_selected_time);

        btnSubmit = findViewById(R.id.btn_submit);

        // 選擇科目
        btnSelectSubject.setOnClickListener(view -> showSubjectSelectionDialog());

        // 選擇日期
        btnSelectDays.setOnClickListener(view -> showDaySelectionDialog());

        // 選擇時間段
        btnSelectTime.setOnClickListener(view -> showTimeRangePicker());

        // 發布按鈕
        btnSubmit.setOnClickListener(view -> submitForm());
    }

    // 選擇初/高中
    private void showSubjectSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇學段");
        String[] levels = {"初中", "高中"};
        builder.setItems(levels, (dialog, which) -> {
            if (which == 0) {
                showSubjectMultiChoice("初中", juniorSubjects);
            } else {
                showSubjectMultiChoice("高中", seniorSubjects);
            }
        });
        builder.show();
    }

    // 多選科目
    private void showSubjectMultiChoice(String level, String[] subjects) {
        boolean[] checkedItems = new boolean[subjects.length];
        for (int i = 0; i < subjects.length; i++) {
            checkedItems[i] = selectedSubjects.contains(subjects[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇 " + level + " 科目");
        builder.setMultiChoiceItems(subjects, checkedItems, (dialog, which, isChecked) -> {
            if (isChecked) {
                if (!selectedSubjects.contains(subjects[which])) {
                    selectedSubjects.add(subjects[which]);
                }
            } else {
                selectedSubjects.remove(subjects[which]);
            }
        });

        builder.setPositiveButton("確認", (dialog, which) -> {
            if (selectedSubjects.isEmpty()) {
                btnSelectSubject.setText("選擇教授科目");
                tvSelectedSubjects.setText("尚未選擇科目");
            } else {
                btnSelectSubject.setText("已選：" + selectedSubjects.size() + "科目");
                StringBuilder subjectStr = new StringBuilder("選擇科目：");
                for (int i = 0; i < selectedSubjects.size(); i++) {
                    subjectStr.append(selectedSubjects.get(i));
                    if (i != selectedSubjects.size() - 1) {
                        subjectStr.append("、");
                    }
                }
                tvSelectedSubjects.setText(subjectStr.toString());
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 多選星期
    private void showDaySelectionDialog() {
        boolean[] checkedItems = new boolean[weekdays.length];
        for (int i = 0; i < weekdays.length; i++) {
            checkedItems[i] = selectedDays.contains(weekdays[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇可預約日期");
        builder.setMultiChoiceItems(weekdays, checkedItems, (dialog, which, isChecked) -> {
            if (isChecked) {
                if (!selectedDays.contains(weekdays[which])) {
                    selectedDays.add(weekdays[which]);
                }
            } else {
                selectedDays.remove(weekdays[which]);
            }
        });

        builder.setPositiveButton("確認", (dialog, which) -> {
            if (selectedDays.isEmpty()) {
                tvSelectedDays.setText("尚未選擇可預約日期");
            } else {
                StringBuilder sb = new StringBuilder("可預約：");
                for (int i = 0; i < selectedDays.size(); i++) {
                    sb.append(weekdays[i].replace("星期", "週"));
                    if (i != selectedDays.size() - 1) sb.append("、");
                }
                tvSelectedDays.setText(sb.toString());
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 選擇時間段
    private void showTimeRangePicker() {
        TimePickerDialog startPicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            startTime = formatTime(hourOfDay, minute);

            TimePickerDialog endPicker = new TimePickerDialog(this, (view2, hourOfDay2, minute2) -> {
                endTime = formatTime(hourOfDay2, minute2);
                if (startTime != null && endTime != null) {
                    tvSelectedTime.setText("可預約時間：" + startTime + " - " + endTime);
                }
            }, 20, 0, true);

            endPicker.setTitle("選擇結束時間");
            endPicker.show();

        }, 18, 0, true);

        startPicker.setTitle("選擇開始時間");
        startPicker.show();
    }

    private String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    // 發布資料
    private void submitForm() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String salary = editSalary.getText().toString().trim();
        String salaryNote = editSalaryNote.getText().toString().trim();
        String intro = editIntro.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || salary.isEmpty() || intro.isEmpty()) {
            Toast.makeText(this, "請填寫所有必填項目", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSubjects.isEmpty()) {
            Toast.makeText(this, "請選擇教授科目", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "請選擇可預約日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startTime == null || endTime == null) {
            Toast.makeText(this, "請選擇可預約時間段", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder subjects = new StringBuilder();
        for (int i = 0; i < selectedSubjects.size(); i++) {
            subjects.append(selectedSubjects.get(i));
            if (i != selectedSubjects.size() - 1) {
                subjects.append("、");
            }
        }

        StringBuilder days = new StringBuilder();
        for (int i = 0; i < selectedDays.size(); i++) {
            days.append(selectedDays.get(i).replace("星期", "週"));
            if (i != selectedDays.size() - 1) {
                days.append("、");
            }
        }

        String message = "姓名：" + name +
                "\n電話：" + phone +
                "\n教授科目：" + subjects +
                "\n薪資：" + salary + " 元/小時" +
                (salaryNote.isEmpty() ? "" : ("\n備註：" + salaryNote)) +
                "\n簡介：" + intro +
                "\n預約日期：" + days +
                "\n預約時間：" + startTime + " - " + endTime;

        new AlertDialog.Builder(this)
                .setTitle("確認發布資料")
                .setMessage(message)
                .setPositiveButton("確定發布", (dialog, which) -> {
                    Toast.makeText(this, "已發布（暫時模擬）", Toast.LENGTH_SHORT).show();
                    // TODO: 實際發送資料到廣場展示
                })
                .setNegativeButton("取消", null)
                .show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("tutorInfo", new TutorInfo(
                name, phone, selectedSubjects, salary, salaryNote, intro,
                selectedDays, startTime, endTime
        ));
        setResult(RESULT_OK, resultIntent);
        finish();  // 返回 MainActivity

    }
}
