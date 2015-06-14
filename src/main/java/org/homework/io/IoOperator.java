package org.homework.io;

import com.lowagie.text.Image;
import org.homework.db.DBConnecter;
import org.homework.db.model.AllStudentScore;
import org.homework.db.model.StudentAnswer;
import org.homework.db.model.TableQuestion;
import org.homework.main.MainFrame;
import org.homework.manager.ManagerMain;
import org.homework.manager.SecurityEncode;
import org.homework.student.CatalogTree;
import org.homework.student.ContentPanel;
import org.homework.teacher.TCatalogTree;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.homework.io.PDFOperator.PDFLiner;
import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/7.
 */
public class IoOperator {

    public static void submitWork(String course,int chapter, TreeMap<Integer, List<TableQuestion>> map) {
        String direct = getFileDirectChoose();
        if (direct != null) {
            String name = MainFrame.user.getName();
            String message = name + "_" + course + "_" + chapter;
            String path = direct + "\\" + message;
            try {
                StudentWork studentWork = new StudentWork(name,course,chapter,map);
                FileOutputStream out = new FileOutputStream(path);
                ObjectOutputStream oos = null;
                ByteArrayOutputStream baos = null;
                // ���л�
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(studentWork);
                byte[] bytes = baos.toByteArray();
                //����
                byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.ENCRYPT_MODE);
                out.write(newBytes);
                out.close();
                JOptionPane.showMessageDialog(null, "�ύ�ɹ���");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
            }
        }
    }

    public static void generatePDF(int chapter, TreeMap<Integer, List<TableQuestion>> map) {
        String direct = getFileDirectChoose();
        String path = null;
        if (direct != null) {
            if (chapter == -1) {
                path = direct + "\\" + "��������.pdf";
            }
            else {
                path = direct + "\\" + "��" + getChineseNum(chapter) + "��" +
                        "����.pdf";
            }

            List<PDFLiner> list = new ArrayList<PDFLiner>();
            if (chapter == -1) {
                list.add(new PDFLiner("��������", 25, Font.BOLD));
            }
            else {
                list.add(new PDFLiner("��" + getChineseNum(chapter) + "��" + "����", 25, Font.BOLD));
            }

            for (Map.Entry<Integer, List<TableQuestion>> entry : map.entrySet()) {
                list.add(new PDFLiner(getTypeWord(entry.getKey()), 20, Font.BOLD));
                for (int i = 0; i < entry.getValue().size(); i++) {
                    TableQuestion t = entry.getValue().get(i);

                    String startSentence = t.getMain_content();
                    if (startSentence.startsWith("[")) {//ͼƬ
                        list.add(new PDFLiner((i + 1) + ". ", 15, Font.PLAIN));
                        try {
                            list.add(new PDFLiner(Image.getInstance(getURL(
                                    ContentPanel.PRE + startSentence.substring(1, startSentence.length() - 1)))
                                    , 15, Font.PLAIN));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        list.add(new PDFLiner((i + 1) + ". " + t.getMain_content(), 15, Font.PLAIN));
                    if (t.getEle_content() != null && !t.getEle_content().equals("")) {
                        String[] strs = t.getEle_content().split(SPLIT);
                        for (int j = 0; j < strs.length; j++) {
                            list.add(new PDFLiner(("  " + num2ABC(j) + ". " + strs[j]), 15, Font.PLAIN));
                        }
                    }
                }
            }
            try {
                PDFOperator.writePdf(list, path);
                JOptionPane.showMessageDialog(null, "�����ɹ���");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "�ļ��Ѵ��ڻ����ļ���ȡ�д�");
            }
        }
    }

    public static void importScore() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                //file����
                byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.DECRYPT_MODE);
                ByteArrayInputStream in = new ByteArrayInputStream(newBytes);
                ObjectInputStream oin = new ObjectInputStream(in);
                Map<String,List<StudentScore>> map = (Map<String, List<StudentScore>>) oin.readObject();
                List<StudentScore> list = map.get(MainFrame.user.getName());
                //List<StudentScore> list = map.get("���ڹ���һ��_2008100134_����");
                for(StudentScore s : list){
                    DBConnecter.updateScore(s.getCourse(), s.getChapter(), s.getScore());
                    //�������ν��棡
                    if (CatalogTree.allScore.get(s.getCourse()) != null) {
                        CatalogTree.allScore.get(s.getCourse()).put(s.getChapter(), s.getScore());
                    }
                    else {
                        TreeMap<Integer, Float> map1 = new TreeMap<Integer, Float>();
                        map1.put(s.getChapter(), s.getScore());
                        CatalogTree.allScore.put(s.getCourse(), map1);
                    }
                }
                CatalogTree.initTop();
                JOptionPane.showMessageDialog(null, "����ɹ���");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "��ȡʧ�ܣ�");
            }
        }
    }

    public static void exportScore() throws Exception{
        String direct = getFileDirectChoose();
        if(direct == null)
            return;
        List<AllStudentScore> allStudentScoreList = DBConnecter.getAllStudentScores();
        Map<String, List<StudentScore>> studentScoreMap = new TreeMap<String, List<StudentScore>>();

        for (AllStudentScore allStudentScore : allStudentScoreList) {
            String stuInfo = null;
            stuInfo = allStudentScore.getStudentClass() + "_" + allStudentScore.getStudentNumber()
                                                        + "_" + allStudentScore.getStudentName();
            List<StudentScore> list = studentScoreMap.get(stuInfo);
            if (list == null) {
                list = new ArrayList<StudentScore>();
                studentScoreMap.put(stuInfo, list);
            }
            list.add(new StudentScore(stuInfo, allStudentScore.getCourse(),
                      allStudentScore.getChapter(), allStudentScore.getScore()));
        }
        String path = direct + "\\" + "ѧ���ɼ�";
        try {
            FileOutputStream out = new FileOutputStream(path);
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            // ���л�
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(studentScoreMap);
            byte[] bytes = baos.toByteArray();
            //����
            byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.ENCRYPT_MODE);
            out.write(newBytes);
            out.close();
            JOptionPane.showMessageDialog(null, "����ѧ���ɼ��ɹ���");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "����ѧ���ɼ�ʧ�ܣ�");
        }

    }

    public static void importStudentWork() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                //file����
                byte[] newBytes = SecurityEncode.coderByDES(bytes, ManagerMain.key, Cipher.DECRYPT_MODE);
                ByteArrayInputStream in = new ByteArrayInputStream(newBytes);
                ObjectInputStream oin = new ObjectInputStream(in);
                StudentWork studentWork  = (StudentWork) oin.readObject();
                String[] stuInfo = studentWork.getName().split("_");
                String stuClass = stuInfo[0];
                String stuNumber = stuInfo[1];
                String stuName = stuInfo[2];
                String course = studentWork.getCourse();
                Integer chapter = studentWork.getChapter();

                //���ļ�������У��
                String name = fileChooser.getSelectedFile().getName();
                String[] strs = name.split("_");
                String studentFileName = "";
                String studentFileNameClass = strs[0];
                String studentFileNameNumber = strs[1];
                String studentFileNameName = strs[2];
                String studentFileNameCourse = strs[3];
                String studentFileNameChapter = strs[4];
                for (int i = 0; i < strs.length - 2; i++) {
                    studentFileName += "_";
                    studentFileName += strs[i];
                }
                studentFileName = studentFileName.substring(1,studentFileName.length());
                if(!studentFileName.equals(studentWork.getName())){
                    JOptionPane.showMessageDialog(null,"ѧ��" + studentFileName + "���ף���");
                    //���µ����ݿ�
                    DBConnecter.updateAllStudentScore(studentFileNameClass, studentFileNameNumber,
                                studentFileNameName, studentFileNameCourse, Integer.parseInt(studentFileNameChapter), -1f);
                    //
                }else{
                    DBConnecter.updateStudentAnswer(studentWork);
                    TreeMap<Integer, List<TableQuestion>> map = (TreeMap<Integer, List<TableQuestion>>)studentWork.getData();
                    ArrayList<StudentAnswer> studentAnswers = new ArrayList<StudentAnswer>();
                    for (Map.Entry<Integer, List<TableQuestion>> entry : map.entrySet()) {
                        Integer type = entry.getKey();
                        for (TableQuestion tableQuestion : entry.getValue()) {
                            Integer id = tableQuestion.getId();
                            String stuAnswer = tableQuestion.getMyAnswer().replaceAll("#","");
                            String correctAnswer = tableQuestion.getAnswer();
                            StudentAnswer stuAns = new StudentAnswer();
                            stuAns.setId(id);
                            stuAns.setCourse(course);
                            stuAns.setChapter(chapter);
                            stuAns.setStudentClass(stuClass);
                            stuAns.setStudentNumber(stuNumber);
                            stuAns.setStudentName(stuName);
                            stuAns.setType(type);
                            stuAns.setStudentAnswer(stuAnswer);
                            stuAns.setAnswer(correctAnswer);
                            studentAnswers.add(stuAns);
                        }
                    }
                    //TCatalogTree.allStudentAnswer.clear();
                    for(StudentAnswer stuAns : studentAnswers){
                        //1��Ŀ¼  ��Ŀ
                        course = stuAns.getCourse();
                        TreeMap<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>> sub1Map = TCatalogTree.allStudentAnswer.get(course);
                        if(sub1Map == null){
                            sub1Map = new TreeMap();
                            TCatalogTree.allStudentAnswer.put(course,sub1Map);
                        }
                        //2��Ŀ¼  �½�
                        chapter = stuAns.getChapter();
                        TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>> sub2Map = sub1Map.get(chapter);
                        if (sub2Map == null){
                            sub2Map = new TreeMap();
                            sub1Map.put(chapter,sub2Map);
                        }
                        //3��Ŀ¼  �༶
                        stuClass = stuAns.getStudentClass();
                        TreeMap<String, TreeMap<Integer,List<StudentAnswer>>> sub3Map = sub2Map.get(stuClass);
                        if (sub3Map == null){
                            sub3Map = new TreeMap();
                            sub2Map.put(stuClass,sub3Map);
                        }
                        //4��Ŀ¼  ѧ��ѧ������
                        stuNumber = stuAns.getStudentNumber();
                        stuName = stuAns.getStudentName();
                        String stuNumName = stuNumber + "_" + stuName;
                        TreeMap<Integer,List<StudentAnswer>> sub4Map = sub3Map.get(stuNumName);
                        if (sub4Map == null) {
                            sub4Map = new TreeMap();
                            sub3Map.put(stuNumName,sub4Map);
                        }
                        //5��Ŀ¼  ��Ŀ����   ����ʾ
                        int type = stuAns.getType();
                        List<StudentAnswer> subList = sub4Map.get(type);
                        if (subList == null){
                            subList = new ArrayList();
                            sub4Map.put(type,subList);
                        }

                        subList.add(stuAns);
                    }
                    TCatalogTree.initTop();


                    //List<StudentScore> list = map.get("���ڹ���һ��_2008100134_����");
                    //for(StudentScore s : list){
                    //    DBConnecter.updateScore(s.getCourse(), s.getChapter(), s.getScore());
                    //�������ν��棡
                    //CatalogTree.allScore.get(s.getCourse()).put(s.getChapter(),s.getScore());
                    //}
                    //CatalogTree.initTop();
                    JOptionPane.showMessageDialog(null, "����ɹ���");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "��ȡʧ�ܣ�");
            }
        }
    }

    public static String getFileDirectChoose() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            return filePath;
        }
        return null;
    }

    public static void main(String[] args) {
    }


}
