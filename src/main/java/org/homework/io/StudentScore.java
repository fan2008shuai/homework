package org.homework.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.homework.manager.ManagerMain;
import org.homework.manager.SecurityEncode;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2015/5/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScore implements Serializable{
    String name;
    String course;
    int chapter;
    float score;
    public static void main(String[] args) throws Exception {
        FileOutputStream out = new FileOutputStream("�Ǻ�");
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // ���л�
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        Map<String,List<StudentScore>> map = new HashMap<String,List<StudentScore>>();
        List<StudentScore> list = new ArrayList<StudentScore>();
        list.add(new StudentScore("����","Ӣ��",2,63));
        map.put("����",list);
        oos.writeObject(map);
        byte[] bytes = baos.toByteArray();
        //����
        byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.ENCRYPT_MODE);
        out.write(newBytes);
        out.close();
    }
}
