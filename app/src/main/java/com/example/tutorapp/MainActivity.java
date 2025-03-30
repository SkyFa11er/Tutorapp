package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DO_TUTOR = 1;
    private static final int REQUEST_FIND_TUTOR = 2;

    private FloatingActionButton btnDoTutor;
    private FloatingActionButton btnFindTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDoTutor = findViewById(R.id.btn_do_tutor);
        btnFindTutor = findViewById(R.id.btn_find_tutor);

        btnDoTutor.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "做家教按鈕觸發了！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DoTutorActivity.class);
            startActivityForResult(intent, REQUEST_DO_TUTOR);
        });

        btnFindTutor.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "招家教按鈕觸發了！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, FindTutorActivity.class);
            startActivityForResult(intent, REQUEST_FIND_TUTOR);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new FirstFragment())
                        .commit();
                btnDoTutor.setVisibility(View.VISIBLE);
                btnFindTutor.setVisibility(View.VISIBLE);
                return true;
            } else if (item.getItemId() == R.id.search) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new ChatFragment())
                        .commit();
                btnDoTutor.setVisibility(View.GONE);
                btnFindTutor.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "已切換到聊天頁面", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new SecondFragment())
                        .commit();
                btnDoTutor.setVisibility(View.GONE);
                btnFindTutor.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

        // 預設顯示 Chat 或 Home 畫面
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("open_chat", false)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new ChatFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.search);
            btnDoTutor.setVisibility(View.GONE);
            btnFindTutor.setVisibility(View.GONE);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, new FirstFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
            btnDoTutor.setVisibility(View.VISIBLE);
            btnFindTutor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // 透過 FragmentManager 找到 FirstFragment 實例
            FirstFragment fragment = (FirstFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);

            if (fragment instanceof FirstFragment) {
                if (requestCode == REQUEST_DO_TUTOR) {
                    TutorInfo tutor = (TutorInfo) data.getSerializableExtra("tutorInfo");
                    if (tutor != null) {
                        fragment.addPost(new PostItem(tutor));
                        Toast.makeText(this, "成功發布做家教資訊", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_FIND_TUTOR) {
                    FindTutorInfo find = (FindTutorInfo) data.getSerializableExtra("findTutorInfo");
                    if (find != null) {
                        fragment.addPost(new PostItem(find));
                        Toast.makeText(this, "成功發布招家教資訊", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
