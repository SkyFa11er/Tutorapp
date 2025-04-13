package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class TutorDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvSubjects, tvSalary, tvSalaryNote, tvIntro, tvDays, tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        tvName = findViewById(R.id.tv_detail_name);
        tvPhone = findViewById(R.id.tv_detail_phone);
        tvSubjects = findViewById(R.id.tv_detail_subjects);
        tvSalary = findViewById(R.id.tv_detail_salary);
        tvSalaryNote = findViewById(R.id.tv_detail_salary_note);
        tvIntro = findViewById(R.id.tv_detail_intro);
        tvDays = findViewById(R.id.tv_detail_days);
        tvTime = findViewById(R.id.tv_detail_time);

        TutorInfo tutor = (TutorInfo) getIntent().getSerializableExtra("tutorInfo");

        if (tutor != null) {
            tvName.setText(tutor.getName());
            tvPhone.setText("聯絡電話：" + tutor.getPhone());

            List<String> subjects = tutor.getSubjects();
            tvSubjects.setText("教授科目：" + String.join("、", subjects));

            tvSalary.setText("薪資：" + tutor.getSalary() + " 元/小時");
            tvSalaryNote.setText(tutor.getSalaryNote().isEmpty() ? "無備註" : "備註：" + tutor.getSalaryNote());
            tvIntro.setText("個人簡介：" + tutor.getIntro());
            tvDays.setText("可預約日期：" + String.join("、", tutor.getDays()));
            tvTime.setText("可預約時間：" + tutor.getStartTime() + " - " + tutor.getEndTime());
        }

        Button btnSendMessage = findViewById(R.id.btn_send_message);
        btnSendMessage.setOnClickListener(v -> {
            if (tutor != null) {
                // 回傳聊天對象名稱給 MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("chat_name", tutor.getName());
                setResult(RESULT_OK, resultIntent);
                finish(); // 關閉此畫面回主頁
            }
        });
    }
}
