package org.homework.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hasee on 2015/5/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    public static final String ID = "id";
    public static final String COURSE = "course";
    public static final String CHAPTER = "chapter";
    public static final String SCORE = "score";

    private int id;
    private String course;
    private int chapter;
    private Float score;
}
