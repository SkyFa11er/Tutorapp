package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private TutorAdapter adapter;
    private List<PostItem> postList = new ArrayList<>();

    public FirstFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TutorAdapter(postList, postItem -> {
            if (postItem.getType() == PostItem.TYPE_DO) {
                Intent intent = new Intent(getActivity(), TutorDetailActivity.class);
                intent.putExtra("tutorInfo", postItem.getTutorInfo());
                startActivity(intent);
            } else if (postItem.getType() == PostItem.TYPE_FIND) {
                Intent intent = new Intent(getActivity(), FindTutorDetailActivity.class);
                intent.putExtra("findTutorInfo", postItem.getFindTutorInfo());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    // 提供方法讓 MainActivity 可以新增資料
    public void addPost(PostItem item) {
        postList.add(0, item);
        if (adapter != null) {
            adapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }
    }
}
