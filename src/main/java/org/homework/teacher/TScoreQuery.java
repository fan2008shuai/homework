package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.AllStudentScore;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2015/5/29.
 */
public class TScoreQuery extends MouseAdapter {


    JDialog jDialog=null; //创建一个空的对话框对象
    JComboBox queryAsComboBox;
    JButton queryButton;
    JTextField courseTextField;
    JTextField queryAsTextField;
    JPanel tablePanel = null;
    JTable table = null;
    static String queryAs = "班级";

    public TScoreQuery(JFrame jFrame) {
                /* 初始化jDialog1
        * 指定对话框的拥有者为jFrame,标题为"Dialog",当对话框为可视时,其他构件不能
        * 接受用户的输入(静态对话框) */
        jDialog = new JDialog(jFrame,"成绩查询",true);

        jDialog.setSize(new Dimension(650, 620));
        /* 设置对话框初始显示在屏幕当中的位置 */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("科目");
        courseLabel.setBackground(Color.WHITE);
        courseTextField = new JTextField(20);
        courseTextField.setBackground(Color.WHITE);

        final String[] queryAs = {"班级", "学号"};
        queryAsComboBox = new JComboBox(queryAs);
        queryAsComboBox.setBackground(Color.WHITE);
        queryAsTextField = new JTextField(30);
        queryAsTextField.setBackground(Color.WHITE);

        queryButton = new JButton("查询");
        queryButton.setBackground(Color.WHITE);

        topPanel.add(courseLabel);
        topPanel.add(courseTextField);
        topPanel.add(queryAsComboBox);
        topPanel.add(queryAsTextField);
        topPanel.add(queryButton);

        queryAsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TScoreQuery.queryAs = (String)e.getItem();
                        break;
                    default:
                        break;
                }

            }
        });
        queryButton.addMouseListener(this);

        jDialog.setVisible(true);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
       if (e.getSource() == queryButton) {

           String course = courseTextField.getText();
           String queryAsText = queryAsTextField.getText();
           java.util.List<AllStudentScore> allStuScoreList = DBConnecter.getAllStudentScores(course, queryAs, queryAsText);
           //System.out.println("Befor sort: " + allStuScoreList);
           Collections.sort(allStuScoreList, new Comparator<AllStudentScore>() {
               public int compare(AllStudentScore arg0, AllStudentScore arg1) {
                   if (arg0.getStudentNumber().compareTo(arg1.getStudentNumber()) == 0) {
                       return Integer.valueOf(arg0.getChapter()).compareTo(Integer.valueOf(arg1.getChapter()));
                   } else {
                       return arg0.getStudentNumber().compareTo(arg1.getStudentNumber());
                   }
               }

           });
           //System.out.println("After sort: " + allStuScoreList);

           //System.out.println(allStuScoreList);

           Map<String, java.util.List<Float>> allStuScoreMap = new TreeMap<String, java.util.List<Float>>();

           for (AllStudentScore allStudentScore : allStuScoreList) {
               String stuNumber = allStudentScore.getStudentNumber();
               String stuName = allStudentScore.getStudentName();
               String stuNumName = stuNumber + "_" + stuName;
               java.util.List<Float> scoreList = allStuScoreMap.get(stuNumName);
               if (scoreList == null) {
                   scoreList = new ArrayList<Float>();
                   allStuScoreMap.put(stuNumName, scoreList);
               }
               scoreList.add(allStudentScore.getScore());
           }

           int row = allStuScoreMap.size() + 1;
           int column = 0;
           for (Map.Entry<String, List<Float>> entry : allStuScoreMap.entrySet()) {
               int elemSize = entry.getValue().size();
               if (elemSize > column) {
                   column = elemSize;
               }
           }
           column += 3;
           if (tablePanel != null && table != null) {
               tablePanel.remove(table);
               jDialog.getContentPane().remove(tablePanel);
               table = null;
               tablePanel = null;
               System.gc();
           }
           createTable(row, column, allStuScoreMap);
           //System.out.println(allStuScoreMap);


        }
    }

    private void createTable(int row, int column, Map<String, java.util.List<Float>> allStuScoreMap) {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        jDialog.getContentPane().add(tablePanel, BorderLayout.CENTER);

        table = new JTable(new DefaultTableModel(row, column));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        table.setEnabled(false);
        table.setValueAt("学号", 0, 0);
        table.setValueAt("姓名", 0, 1);
        table.setValueAt("平均分", 0, column-1);
        for (int i = 1; i <= column - 3; i++) {
            String s = "第" + i + "章";
            table.setValueAt(s, 0, i+1);
        }
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setRowHeight(20);
        tablePanel.add(scrollPane);

        int rowIndex = 0;
        int columnIndex = 0;
        float scoreSum = 0;
        int index = 0;
        for (Map.Entry<String, List<Float>> entry : allStuScoreMap.entrySet()) {

            rowIndex++;
            columnIndex = 0;
            scoreSum = 0;
            index = 0;

            String[] stuNumName = entry.getKey().split("_");
            table.setValueAt(stuNumName[0], rowIndex, columnIndex++);
            table.setValueAt(stuNumName[1], rowIndex, columnIndex++);
            for (Float score : entry.getValue()) {
                if(score == -1f) {
                    table.setValueAt("作弊", rowIndex, columnIndex++);
                    scoreSum += 0;
                } else{
                    table.setValueAt(score, rowIndex, columnIndex++);
                    scoreSum += score;
                }
                index++;
            }
            table.setValueAt(scoreSum / index, rowIndex, column - 1);

        }

        jDialog.setVisible(true);
    }
}
