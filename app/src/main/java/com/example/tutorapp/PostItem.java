package com.example.tutorapp;

import java.io.Serializable;

public class PostItem implements Serializable {
    public static final int TYPE_DO = 0;
    public static final int TYPE_FIND = 1;

    private int type; // 0 = 做家教, 1 = 招家教
    private TutorInfo tutorInfo;
    private FindTutorInfo findTutorInfo;

    public PostItem(TutorInfo tutorInfo) {
        this.type = TYPE_DO;
        this.tutorInfo = tutorInfo;
    }

    public PostItem(FindTutorInfo findTutorInfo) {
        this.type = TYPE_FIND;
        this.findTutorInfo = findTutorInfo;
    }

    public int getType() {
        return type;
    }

    public TutorInfo getTutorInfo() {
        return tutorInfo;
    }

    public FindTutorInfo getFindTutorInfo() {
        return findTutorInfo;
    }
}
