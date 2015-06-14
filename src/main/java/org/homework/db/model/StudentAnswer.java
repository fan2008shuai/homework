package org.homework.db.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/5/22.
 */
@Data
public class StudentAnswer implements Serializable {
    public static final String ID = "id";
    public static final String COURSE = "course";
    public static final String CHAPTER = "chapter";
    public static final String STUDENT_CLASS = "studentClass";
    public static final String STUDENT_NUMBER = "studentNumber";
    public static final String STUDENT_NAME = "studentName";
    public static final String TYPE = "type";
    public static final String STUDENT_ANSWER = "studentAnswer";

    public static final String ANSWER = "answer";

    int id;
    String course;
    int chapter;
    String studentClass;
    String studentNumber;
    String studentName;
    int type;
    String studentAnswer;

    transient String answer;
}
