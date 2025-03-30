package com.example.tutorapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatItem> chatList = new ArrayList<>();

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatAdapter = new ChatAdapter(chatList);
        chatRecyclerView.setAdapter(chatAdapter);

        // 假資料
        chatList.add(new ChatItem("王俊明"));
        chatList.add(new ChatItem("戴廣義"));
        chatList.add(new ChatItem("溫慶宇"));
        chatAdapter.notifyDataSetChanged();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getIntent() != null) {
            String newChatName = getActivity().getIntent().getStringExtra("chat_name");
            if (newChatName != null && !newChatName.isEmpty()) {
                // 檢查是否已存在
                boolean exists = false;
                for (ChatItem item : chatList) {
                    if (item.getName().equals(newChatName)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    chatList.add(0, new ChatItem(newChatName));
                    chatAdapter.notifyItemInserted(0);
                }

                // 移除 Intent 避免重複新增
                getActivity().getIntent().removeExtra("chat_name");
            }
        }
    }

}
