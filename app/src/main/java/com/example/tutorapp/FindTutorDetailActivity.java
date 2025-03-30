package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FindTutorDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvDistrict, tvSubjects, tvSalary, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutor_detail);

        tvName = findViewById(R.id.tv_detail_name);
        tvPhone = findViewById(R.id.tv_detail_phone);
        tvDistrict = findViewById(R.id.tv_detail_district); // 新增
        tvSubjects = findViewById(R.id.tv_detail_subjects);
        tvSalary = findViewById(R.id.tv_detail_salary);
        tvNote = findViewById(R.id.tv_detail_note);

        FindTutorInfo info = (FindTutorInfo) getIntent().getSerializableExtra("findTutorInfo");

        if (info != null) {
            tvName.setText("孩子姓名：" + info.getChildName());
            tvPhone.setText("聯絡電話：" + info.getPhone());
            tvDistrict.setText("地區：廣州" + info.getDistrict()); // 顯示地區
            tvSubjects.setText("科目需求：" + String.join("、", info.getSubjects()));
            tvSalary.setText("預算：" + info.getSalary() + " 元/小時");
            tvNote.setText(info.getNote().isEmpty() ? "無其他需求" : "需求說明：" + info.getNote());
        }

        Button btnSendMessage = findViewById(R.id.btn_send_message);
        btnSendMessage.setOnClickListener(v -> {
            if (info != null) {
                Intent intent = new Intent(FindTutorDetailActivity.this, MainActivity.class);
                intent.putExtra("chat_name", info.getChildName());
                intent.putExtra("open_chat", true);
                startActivity(intent);
            }
        });
    }
}
