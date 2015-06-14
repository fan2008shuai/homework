package org.homework.db.model;

import lombok.Data;

/**
 * Created by lenovo on 2015/5/29.
 */
@Data
public class AllStudentScore {
    public static final String ID = "id";
    public static final String COURSE = "course";
    public static final String CHAPTER = "chapter";
    public static final String STUDENT_CLASS = "studentClass";
    public static final String STUDENT_NUMBER = "studentNumber";
    public static final String STUDENT_NAME = "studentName";
    public static final String SCORE = "score";

    int id;
    String course;
    int chapter;
    String studentClass;
    String studentNumber;
    String studentName;
    float score;

}
