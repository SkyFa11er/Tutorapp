package com.example.tutorapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private TutorAdapter adapter;
    private List<PostItem> postList = new ArrayList<>();
    private String currentTypeFilter = "all"; // "all", "do", "find"

    // 進階條件變數
    private List<String> allSubjects = Arrays.asList(
            "初中語文", "初中數學", "初中物化", "初中英語", "初中生物", "初中政治",
            "高中語文", "高中數學", "高中物化", "高中英語", "高中生物", "高中政治"
    );
    private boolean[] checkedSubjects = new boolean[allSubjects.size()];
    private List<String> selectedSubjectsFilter = new ArrayList<>();
    private String selectedDistrict = "";
    private int minSalary = 0;

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

        // 類型篩選 Spinner
        Spinner spinner = view.findViewById(R.id.spinner_filter);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"全部", "做家教", "招家教"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        currentTypeFilter = "all";
                        break;
                    case 1:
                        currentTypeFilter = "do";
                        break;
                    case 2:
                        currentTypeFilter = "find";
                        break;
                }
                filterAdvancedPosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 條件篩選按鈕
        Button btnFilter = view.findViewById(R.id.btn_filter_advanced);
        btnFilter.setOnClickListener(v -> showAdvancedFilterDialog());

        return view;
    }

    public void addPost(PostItem item) {
        postList.add(0, item);
        adapter.updateList(postList); // 也更新原始資料
        filterAdvancedPosts(); // 每次新增都重新篩選
    }

    private void showAdvancedFilterDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_advanced, null);

        Button btnSelectSubjects = dialogView.findViewById(R.id.btn_select_subjects);
        TextView tvSelectedSubjects = dialogView.findViewById(R.id.tv_selected_subjects);
        Spinner spinnerDistrict = dialogView.findViewById(R.id.spinner_district);
        EditText editMinSalary = dialogView.findViewById(R.id.edit_min_salary);

        // 地區 Spinner 初始化
        String[] districts = {"不限", "越秀區", "天河區", "白雲區", "海珠區", "黃埔區", "荔灣區", "番禺區", "花都區", "南沙區", "增城區", "從化區"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, districts);
        spinnerDistrict.setAdapter(adapter);

        tvSelectedSubjects.setText(selectedSubjectsFilter.isEmpty() ? "尚未選擇" : "已選擇：" + String.join("、", selectedSubjectsFilter));

        btnSelectSubjects.setOnClickListener(v -> {
            AlertDialog.Builder subjectDialog = new AlertDialog.Builder(getContext());
            subjectDialog.setTitle("選擇科目（可多選）");
            subjectDialog.setMultiChoiceItems(
                    allSubjects.toArray(new String[0]),
                    checkedSubjects,
                    (dialog, which, isChecked) -> {
                        if (isChecked) {
                            selectedSubjectsFilter.add(allSubjects.get(which));
                        } else {
                            selectedSubjectsFilter.remove(allSubjects.get(which));
                        }
                    }
            );
            subjectDialog.setPositiveButton("確定", (dialog, which) -> {
                if (selectedSubjectsFilter.isEmpty()) {
                    tvSelectedSubjects.setText("尚未選擇");
                } else {
                    tvSelectedSubjects.setText("已選擇：" + String.join("、", selectedSubjectsFilter));
                }
            });
            subjectDialog.setNegativeButton("取消", null);
            subjectDialog.show();
        });

        new AlertDialog.Builder(getContext())
                .setTitle("進階條件篩選")
                .setView(dialogView)
                .setPositiveButton("確定", (dialog, which) -> {
                    selectedDistrict = spinnerDistrict.getSelectedItem().toString();
                    String salaryStr = editMinSalary.getText().toString().trim();
                    minSalary = salaryStr.isEmpty() ? 0 : Integer.parseInt(salaryStr);
                    filterAdvancedPosts();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void filterAdvancedPosts() {
        List<PostItem> filtered = new ArrayList<>();
        for (PostItem post : postList) {
            boolean typeMatch = currentTypeFilter.equals("all")
                    || (currentTypeFilter.equals("do") && post.getType() == PostItem.TYPE_DO)
                    || (currentTypeFilter.equals("find") && post.getType() == PostItem.TYPE_FIND);

            boolean subjectMatch = true;
            boolean districtMatch = true;
            boolean salaryMatch = true;

            if (post.getType() == PostItem.TYPE_DO && post.getTutorInfo() != null) {
                TutorInfo t = post.getTutorInfo();

                // 科目篩選
                if (!selectedSubjectsFilter.isEmpty()) {
                    subjectMatch = false;
                    for (String s : t.getSubjects()) {
                        if (selectedSubjectsFilter.contains(s)) {
                            subjectMatch = true;
                            break;
                        }
                    }
                }

                // 薪資（String → int）
                int tutorSalary = 0;
                try {
                    tutorSalary = Integer.parseInt(t.getSalary());
                } catch (NumberFormatException e) {
                    tutorSalary = 0;
                }
                salaryMatch = tutorSalary >= minSalary;

            } else if (post.getType() == PostItem.TYPE_FIND && post.getFindTutorInfo() != null) {
                FindTutorInfo f = post.getFindTutorInfo();

                // 科目篩選
                if (!selectedSubjectsFilter.isEmpty()) {
                    subjectMatch = false;
                    for (String s : f.getSubjects()) {
                        if (selectedSubjectsFilter.contains(s)) {
                            subjectMatch = true;
                            break;
                        }
                    }
                }

                // 薪資（int）
                salaryMatch = f.getSalary() >= minSalary;

                // 地區
                if (!selectedDistrict.equals("不限") && !f.getDistrict().equals(selectedDistrict)) {
                    districtMatch = false;
                }
            }

            if (typeMatch && subjectMatch && districtMatch && salaryMatch) {
                filtered.add(post);
            }
        }

        adapter.updateList(filtered);
    }

}
