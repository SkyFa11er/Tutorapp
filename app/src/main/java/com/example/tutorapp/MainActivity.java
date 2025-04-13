package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DO_TUTOR = 1;
    private static final int REQUEST_FIND_TUTOR = 2;

    private FloatingActionButton btnDoTutor;
    private FloatingActionButton btnFindTutor;

    // 三個 Fragment 實例（只建立一次）
    private FirstFragment firstFragment = new FirstFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private SecondFragment secondFragment = new SecondFragment();
    private Fragment activeFragment = firstFragment; // 預設顯示的是廣場頁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDoTutor = findViewById(R.id.btn_do_tutor);
        btnFindTutor = findViewById(R.id.btn_find_tutor);

        // 發布按鈕邏輯
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

        // Fragment 初始化只加一次
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.contentFrame, secondFragment, "2").hide(secondFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, chatFragment, "1").hide(chatFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, firstFragment, "0").commit();

        // BottomNavigation 切換 Fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                fm.beginTransaction().hide(activeFragment).show(firstFragment).commit();
                activeFragment = firstFragment;
                btnDoTutor.setVisibility(View.VISIBLE);
                btnFindTutor.setVisibility(View.VISIBLE);
                return true;

            } else if (itemId == R.id.search) {
                fm.beginTransaction().hide(activeFragment).show(chatFragment).commit();
                activeFragment = chatFragment;
                btnDoTutor.setVisibility(View.GONE);
                btnFindTutor.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "已切換到聊天頁面", Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.profile) {
                fm.beginTransaction().hide(activeFragment).show(secondFragment).commit();
                activeFragment = secondFragment;
                btnDoTutor.setVisibility(View.GONE);
                btnFindTutor.setVisibility(View.GONE);
                return true;
            }

            return false;
        });


        // 是否從發消息跳轉進來（預設開啟 chat 頁）
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("open_chat", false)) {
            fm.beginTransaction().hide(activeFragment).show(chatFragment).commit();
            activeFragment = chatFragment;
            bottomNavigationView.setSelectedItemId(R.id.search);
            btnDoTutor.setVisibility(View.GONE);
            btnFindTutor.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.home); // 預設在首頁
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // 找到 FirstFragment，新增貼文
            if (firstFragment != null) {
                if (requestCode == REQUEST_DO_TUTOR) {
                    TutorInfo tutor = (TutorInfo) data.getSerializableExtra("tutorInfo");
                    if (tutor != null) {
                        firstFragment.addPost(new PostItem(tutor));
                        Toast.makeText(this, "成功發布做家教資訊", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_FIND_TUTOR) {
                    FindTutorInfo find = (FindTutorInfo) data.getSerializableExtra("findTutorInfo");
                    if (find != null) {
                        firstFragment.addPost(new PostItem(find));
                        Toast.makeText(this, "成功發布招家教資訊", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
