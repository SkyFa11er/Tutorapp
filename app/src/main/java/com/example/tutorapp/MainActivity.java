package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DO_TUTOR = 1;
    private static final int REQUEST_FIND_TUTOR = 2;
    private static final int REQUEST_CHAT = 3;

    private FloatingActionButton btnDoTutor;
    private FloatingActionButton btnFindTutor;

    private List<PostItem> postList = new ArrayList<>();
    private TutorAdapter tutorAdapter;
    private FirstFragment firstFragment;

    private List<ChatItem> chatList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private ChatFragment chatFragment;

    private SecondFragment secondFragment;

    private FragmentManager fm;
    private androidx.fragment.app.Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDoTutor = findViewById(R.id.btn_do_tutor);
        btnFindTutor = findViewById(R.id.btn_find_tutor);

        tutorAdapter = new TutorAdapter(postList, postItem -> {
            if (postItem.getType() == PostItem.TYPE_DO) {
                Intent intent = new Intent(MainActivity.this, TutorDetailActivity.class);
                intent.putExtra("tutorInfo", postItem.getTutorInfo());
                startActivityForResult(intent, REQUEST_CHAT);
            } else if (postItem.getType() == PostItem.TYPE_FIND) {
                Intent intent = new Intent(MainActivity.this, FindTutorDetailActivity.class);
                intent.putExtra("findTutorInfo", postItem.getFindTutorInfo());
                startActivityForResult(intent, REQUEST_CHAT);
            }
        });
        firstFragment = new FirstFragment(postList, tutorAdapter);

        chatAdapter = new ChatAdapter(chatList);
        chatFragment = new ChatFragment(chatList, chatAdapter);

        secondFragment = new SecondFragment();

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.contentFrame, secondFragment, "3").hide(secondFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, chatFragment, "2").hide(chatFragment).commit();
        fm.beginTransaction().add(R.id.contentFrame, firstFragment, "1").commit();
        activeFragment = firstFragment;

        btnDoTutor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DoTutorActivity.class);
            startActivityForResult(intent, REQUEST_DO_TUTOR);
        });

        btnFindTutor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FindTutorActivity.class);
            startActivityForResult(intent, REQUEST_FIND_TUTOR);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
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

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("open_chat", false)) {
            bottomNavigationView.setSelectedItemId(R.id.search);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_DO_TUTOR) {
                TutorInfo tutor = (TutorInfo) data.getSerializableExtra("tutorInfo");
                if (tutor != null) {
                    firstFragment.addPost(new PostItem(tutor));
                }
            } else if (requestCode == REQUEST_FIND_TUTOR) {
                FindTutorInfo find = (FindTutorInfo) data.getSerializableExtra("findTutorInfo");
                if (find != null) {
                    firstFragment.addPost(new PostItem(find));
                }
            } else if (requestCode == REQUEST_CHAT) {
                String chatName = data.getStringExtra("chat_name");
                if (chatName != null) {
                    addChatAndSwitch(chatName);
                }
            }
        }
    }

    private void addChatAndSwitch(String name) {
        for (ChatItem item : chatList) {
            if (item.getName().equals(name)) {
                switchToChat();
                return;
            }
        }
        chatList.add(0, new ChatItem(name));
        chatAdapter.notifyItemInserted(0);
        switchToChat();
    }

    private void switchToChat() {
        fm.beginTransaction().hide(activeFragment).show(chatFragment).commit();
        activeFragment = chatFragment;
        btnDoTutor.setVisibility(View.GONE);
        btnFindTutor.setVisibility(View.GONE);
    }
}
