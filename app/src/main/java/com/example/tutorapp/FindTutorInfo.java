package com.example.tutorapp;

import java.io.Serializable;
import java.util.List;

public class FindTutorInfo implements Serializable {

    private String childName;              // 孩子姓名
    private String phone;                  // 聯絡電話
    private String district;               // 地區（例如：天河區）
    private String address;                // 詳細地址（僅儲存不顯示）
    private int salary;                    // 薪資
    private List<String> subjects;         // 科目需求（多選）
    private List<String> days;             // 可授課日期（預留）
    private String note;                   // 額外需求

    // 建構子
    public FindTutorInfo(String childName, String phone, String district, String address,
                         int salary, List<String> subjects, List<String> days, String note) {
        this.childName = childName;
        this.phone = phone;
        this.district = district;
        this.address = address;
        this.salary = salary;
        this.subjects = subjects;
        this.days = days;
        this.note = note;
    }

    // Getter 方法
    public String getChildName() {
        return childName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDistrict() {
        return district;
    }

    public String getAddress() {
        return address;
    }

    public int getSalary() {
        return salary;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getDays() {
        return days;
    }

    public String getNote() {
        return note;
    }
}
