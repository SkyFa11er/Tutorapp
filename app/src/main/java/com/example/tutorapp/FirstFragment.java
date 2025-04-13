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

    // 篩選條件變數
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

        // 類型切換 RadioGroup（全部／做家教／招家教）
        RadioGroup radioGroup = view.findViewById(R.id.radio_filter);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_all) {
                currentTypeFilter = "all";
            } else if (checkedId == R.id.radio_do) {
                currentTypeFilter = "do";
            } else if (checkedId == R.id.radio_find) {
                currentTypeFilter = "find";
            }
            filterAdvancedPosts();
        });


        // 進階條件篩選
        Button btnFilter = view.findViewById(R.id.btn_filter_advanced);
        btnFilter.setOnClickListener(v -> showAdvancedFilterDialog());

        return view;
    }

    public void addPost(PostItem item) {
        postList.add(0, item);
        adapter.updateList(postList);
        filterAdvancedPosts();
    }

    private void showAdvancedFilterDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_advanced, null);

        Button btnSelectSubjects = dialogView.findViewById(R.id.btn_select_subjects);
        TextView tvSelectedSubjects = dialogView.findViewById(R.id.tv_selected_subjects);
        Spinner spinnerDistrict = dialogView.findViewById(R.id.spinner_district);
        EditText editMinSalary = dialogView.findViewById(R.id.edit_min_salary);

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

                if (!selectedSubjectsFilter.isEmpty()) {
                    subjectMatch = false;
                    for (String s : t.getSubjects()) {
                        if (selectedSubjectsFilter.contains(s)) {
                            subjectMatch = true;
                            break;
                        }
                    }
                }

                int tutorSalary = 0;
                try {
                    tutorSalary = Integer.parseInt(t.getSalary());
                } catch (NumberFormatException ignored) {}
                salaryMatch = tutorSalary >= minSalary;

            } else if (post.getType() == PostItem.TYPE_FIND && post.getFindTutorInfo() != null) {
                FindTutorInfo f = post.getFindTutorInfo();

                if (!selectedSubjectsFilter.isEmpty()) {
                    subjectMatch = false;
                    for (String s : f.getSubjects()) {
                        if (selectedSubjectsFilter.contains(s)) {
                            subjectMatch = true;
                            break;
                        }
                    }
                }

                salaryMatch = f.getSalary() >= minSalary;

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
