package org.homework.utils;

import org.homework.db.model.Score;
import org.homework.db.model.StudentAnswer;
import org.homework.db.model.TableQuestion;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by hasee on 2015/5/5.
 */
public class Utils {

    static final String[] TYPE_INDEX = {"��ѡ��", "��ѡ��","�ж���", "�����", "������"};
    static final String[] TYPE_EXPLAIN = {
            "��ѡ�⣺ÿ��ֻ��һ����ȷ�𰸡�ÿ��1�֡�",
            "��ѡ�⣺ÿ����һ��������ȷ�𰸡�ÿ��2�֡�",
            "�ж��⣺�Ի����ѡ��ÿ��1�֡�",
            "����⣺��գ��Զ��ŷָ��𰸡�ÿ��1�֡�",
            "�����⣺��д������Ŀ��С�"};
    public static final String[] JUDGE_OPTION = {"��", "��"};
    public static final String SPLIT = "#";
    static final int LABEL_MAX_LENGTH = 500;

    public static String getTypeWord(int i){
        return TYPE_INDEX[i-1];
    }
    public static String getTypeExplain(int i) {
        return TYPE_EXPLAIN[i-1];
    }
    public static JLabel buildLabel() {
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setFont(new Font("����", Font.BOLD, 15));
        return label;
    }

    public static ImageIcon getIcon(String url){
        return new ImageIcon(Utils.class.getClassLoader().getResource(url));
    }

    public static String getPath(String url){
        return Utils.class.getClassLoader().getResource(url).getPath();
    }

    public static URL getURL(String url){
        return Utils.class.getClassLoader().getResource(url);
    }

    public static void add4Index(TreeMap<String,TreeMap<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>>> map,
                                 List<StudentAnswer> studentAnswers){
        for(StudentAnswer stuAns : studentAnswers){
            //1��Ŀ¼  ��Ŀ
            String course = stuAns.getCourse();
            TreeMap<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>> sub1Map = map.get(course);
            if(sub1Map == null){
                sub1Map = new TreeMap();
                map.put(course,sub1Map);
            }
            //2��Ŀ¼  �½�
            int  chapter = stuAns.getChapter();
            TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>> sub2Map = sub1Map.get(chapter);
            if (sub2Map == null){
                sub2Map = new TreeMap();
                sub1Map.put(chapter,sub2Map);
            }
            //3��Ŀ¼  �༶
            String stuClass = stuAns.getStudentClass();
            TreeMap<String, TreeMap<Integer,List<StudentAnswer>>> sub3Map = sub2Map.get(stuClass);
            if (sub3Map == null){
                sub3Map = new TreeMap();
                sub2Map.put(stuClass,sub3Map);
            }
            //4��Ŀ¼  ѧ��ѧ������
            String stuNumber = stuAns.getStudentNumber();
            String stuName = stuAns.getStudentName();
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
    }

    public static void add3Index(TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> map,
                                 List<TableQuestion> questions){
        for(TableQuestion q : questions){
            //1��Ŀ¼
            String course = q.getCourse();
            TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>> sub1Map = map.get(course);
            if(sub1Map == null){
                sub1Map = new TreeMap();
                map.put(course,sub1Map);
            }
            //2��Ŀ¼
            int  chapter = q.getChapter();
            TreeMap<Integer,List<TableQuestion>> sub2Map = sub1Map.get(chapter);
            if (sub2Map == null){
                sub2Map = new TreeMap();
                sub1Map.put(chapter,sub2Map);
            }
            //3��Ŀ¼
            int type = q.getType();
            List<TableQuestion> subList = sub2Map.get(type);
            if (subList == null){
                subList = new ArrayList();
                sub2Map.put(type,subList);
            }
            subList.add(q);
        }
    }

    public static void mapAllTestQuestion(TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> map,
                                 List<TableQuestion> questions){
        for(TableQuestion q : questions){
            //��Ŀ
            String course = q.getCourse();
            TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>> sub1Map = map.get(course);
            if(sub1Map == null){
                sub1Map = new TreeMap();
                map.put(course,sub1Map);
            }
            //����
            int  type = q.getType();
            TreeMap<Integer,List<TableQuestion>> sub2Map = sub1Map.get(type);
            if (sub2Map == null){
                sub2Map = new TreeMap();
                sub1Map.put(type,sub2Map);
            }
            //�½�
            int chapter = q.getChapter();
            List<TableQuestion> subList = sub2Map.get(chapter);
            if (subList == null){
                subList = new ArrayList();
                sub2Map.put(chapter,subList);
            }
            subList.add(q);

        }
    }

    public static void add2Index(TreeMap<String,TreeMap<Integer,Float>> map,
                                 List<Score> scores){
        for(Score s : scores){
            //1��Ŀ¼
            String course = s.getCourse();
            TreeMap<Integer,Float> sub1Map = map.get(course);
            if(sub1Map == null){
                sub1Map = new TreeMap();
                map.put(course,sub1Map);
            }
            //2��Ŀ¼
            int  chapter = s.getChapter();
            sub1Map.put(chapter,s.getScore());
        }
    }

    public static String getChineseNum(int num){
        String ret = "";
        String[] table={"��","һ","��","��","��","��","��","��","��","��"};
        String s= num+"";
        for (char c : s.toCharArray()) {
            if(c>='0' && c<='9'){
                ret += table[c-48];
            }
        }
        return ret;
    }
    public static String num2ABC(int num){
        return (char)(num+65) + "";
    }

    public static JTextArea getMutiLineArea(){
        JTextArea jTextArea = new JTextArea();
        jTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setBackground(null);//����͸��
        jTextArea.setEditable(false);//���ɱ༭
        jTextArea.setLineWrap(true);
        jTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        return jTextArea;
    }

    public static void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    public static void main(String[] args){

    }

}
