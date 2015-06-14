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
public class User {
    public static final int STUDENT = 1;
    public static final int TEACHER = 2;

    private String name;
    private String password;
    private int type;
}
