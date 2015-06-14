package org.homework.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by hasee on 2015/5/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableQuestion implements Serializable{
    public static final String ID = "id";
    public static final String COURSE = "course";
    public static final String CHAPTER = "chapter";
    public static final String TYPE = "type";
    public static final String MAIN_CONTENT = "main_content";
    public static final String ELE_CONTENT = "element_content";
    public static final String ANSWER = "answer";
    public static final String ANSWER_EXPLAIN = "answer_explain";
    public static final String MY_ANSWER = "my_answer";
    public static final String NOTE = "note";
    public static final String COLLECT_STATUS = "collect_status";

    public static final int COLLECT_NOT = 0;
    public static final int COLLECT_YES = 1;

    int id;
    String course;
    int chapter;
    int type;
    transient String main_content;
    transient String ele_content;
    String answer;
    transient String answerExplain;
    String myAnswer;
    transient String note;
    transient int collectStatus;
}
