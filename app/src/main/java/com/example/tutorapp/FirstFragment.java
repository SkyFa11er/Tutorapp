package com.example.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private TutorAdapter adapter;
    private List<PostItem> fullList = new ArrayList<>();
    private List<PostItem> filteredList = new ArrayList<>();

    public FirstFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TutorAdapter(filteredList, postItem -> {
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

        // 切換選單邏輯
        RadioGroup radioGroup = view.findViewById(R.id.radio_filter);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_all) {
                filterPosts("all");
            } else if (checkedId == R.id.radio_do) {
                filterPosts("do");
            } else if (checkedId == R.id.radio_find) {
                filterPosts("find");
            }
        });

        return view;
    }

    // ✅ 新增貼文資料
    public void addPost(PostItem item) {
        fullList.add(0, item);
        filterPosts(getCurrentFilterType());
    }

    // ✅ 根據選項篩選貼文
    private void filterPosts(String type) {
        filteredList.clear();
        if (type.equals("all")) {
            filteredList.addAll(fullList);
        } else if (type.equals("do")) {
            for (PostItem item : fullList) {
                if (item.getType() == PostItem.TYPE_DO) {
                    filteredList.add(item);
                }
            }
        } else if (type.equals("find")) {
            for (PostItem item : fullList) {
                if (item.getType() == PostItem.TYPE_FIND) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // ✅ 取得目前被選取的類型
    private String getCurrentFilterType() {
        if (getView() == null) return "all";
        RadioGroup group = getView().findViewById(R.id.radio_filter);
        int checkedId = group.getCheckedRadioButtonId();
        if (checkedId == R.id.radio_do) return "do";
        if (checkedId == R.id.radio_find) return "find";
        return "all";
    }
}
