package com.example.tutorapp;

import java.io.Serializable;
import java.util.List;

public class TutorInfo implements Serializable {
    private String name;
    private String phone;
    private List<String> subjects;
    private String salary;
    private String salaryNote;
    private String intro;
    private List<String> days;
    private String startTime;
    private String endTime;

    public TutorInfo(String name, String phone, List<String> subjects,
                     String salary, String salaryNote, String intro,
                     List<String> days, String startTime, String endTime) {
        this.name = name;
        this.phone = phone;
        this.subjects = subjects;
        this.salary = salary;
        this.salaryNote = salaryNote;
        this.intro = intro;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // --- Getter methods ---
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public String getSalary() {
        return salary;
    }

    public String getSalaryNote() {
        return salaryNote;
    }

    public String getIntro() {
        return intro;
    }

    public List<String> getDays() {
        return days;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
