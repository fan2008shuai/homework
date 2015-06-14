package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.StudentAnswer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.homework.utils.Utils.*;

public class TContentPanel extends JPanel {

    public static final String PRE = "question\\";

    ArrayList<JTextField> comprehensiveScoreTextFiled = new ArrayList<JTextField>();
    JTextField compSumScore = new JTextField(10);
    JTextField sumScoreText = new JTextField(10);
    float sumScore = 0;
    float objectiveScore = 0;

    JLabel labelTitle;
    JScrollPane scrollPane;
    public static boolean isCollectPanel = false;

    static TContentPanel tContentPanel = new TContentPanel();
    public static TContentPanel getTContentPanel() {
        return tContentPanel;
    }

    public JScrollPane getScrollPane(){

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(this);

        labelTitle = new JLabel("无标题");
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitle.setBackground(Color.WHITE);
        scrollPane.setColumnHeaderView(labelTitle);
        labelTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        labelTitle.setFont(new Font("黑体", Font.BOLD, 20));
        scrollPane.getColumnHeader().setBackground(Color.white);
        return scrollPane;
    }

    public TContentPanel(){
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    private float getUnitScore(String str) {
        String tmp = "";

        int start = str.lastIndexOf("每题");
        if (start == -1) {
            return -1;
        }
        int end = str.indexOf("分", start);
        if (end == -1) {
            return -1;
        }
        for (int i = start+2; i < end; i++) {
            tmp = tmp + str.charAt(i);
        }
        return Float.parseFloat(tmp);
    }

    public void fullContent(final String stuClass, final String stuNumName, final String course, final Integer chapter, TreeMap<Integer, List<StudentAnswer>> map){

        removeAll();
        labelTitle.setText("批改作业");

        sumScore = 0;
        objectiveScore = 0;
        sumScoreText.setText(null);
        compSumScore.setText(null);
        for (JTextField jTextField : comprehensiveScoreTextFiled) {
            jTextField.setText(null);
        }

        for (Map.Entry<Integer, List<StudentAnswer>> entry : map.entrySet()) {
            //List<StudentAnswer> list = entry.getValue();
            int type = entry.getKey();
            float unitScore = 0;
            String typeExplain = getTypeExplain(type);
            JLabel labelTypeExplain = buildLabel();
            labelTypeExplain.setText(getChineseNum(type) + "、" + typeExplain);
            unitScore = getUnitScore(typeExplain);
//            if (unitScore == -1) {
//                System.out.println("他是简答题" + unitScore);
//            }
//            else {
//                System.out.println(unitScore);
//            }
            labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
            labelTypeExplain.setFont(new Font("宋体", Font.BOLD, 15));

            add(labelTypeExplain);


            /*
            for(int i=0; i < list.size(); i++) {
                StudentAnswer t = list.get(i);
                tContentPanel.addPanel(t);
            }
            */
            addPanel(unitScore, entry.getKey(), entry.getValue());
        }

        final JPanel sumScorePanel = new JPanel();
        sumScorePanel.setBackground(Color.WHITE);
        sumScorePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        sumScorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        sumScorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sumScorePanel.add(new JLabel("总得分："));
        sumScorePanel.add(sumScoreText);
        sumScoreText.setEditable(false);
        final JButton submitScore = new JButton("提交成绩");
        sumScorePanel.add(submitScore);
        submitScore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == submitScore) {
                    float comsumS = 0;
                    sumScore = 0;
                    for (JTextField jTextField : comprehensiveScoreTextFiled) {
                        String s = jTextField.getText();
                        if (!s.equals("")) {
                            comsumS += Float.parseFloat(s);
                        }
                    }
                    sumScore = objectiveScore + comsumS;
                    compSumScore.setText(Float.toString(comsumS));
                    sumScoreText.setText(Float.toString(sumScore));

                    String[] stu = stuNumName.split("_");

                    //更新到数据库
                    DBConnecter.updateAllStudentScore(stuClass, stu[0], stu[1], course, chapter, sumScore);

                }
            }
        });

        add(sumScorePanel);

        //有空白Panel才能删除最后一个控件（答案）
        JPanel plaitPanel = new JPanel();
        plaitPanel.setBackground(Color.white);
        add(plaitPanel);
        validate();
        repaint();
    }

    //显示单选 多选  判断
    private void showObjectiveAnswer(float unitScore, List<StudentAnswer> stuAnsList) {
        //成绩panel
        final JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField textField = new JTextField(10);
        textField.setEditable(false);
        scorePanel.add(new JLabel("成绩："));
        scorePanel.add(textField);
        add(scorePanel);

        //答案panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        List<JTable> tableList = new ArrayList<JTable>();

//        for (int i = stuAnsList.size(); i < 27; i++) {
//             StudentAnswer stuAns = new StudentAnswer();
//            stuAns.setStudentAnswer("A"+i%4);
//            stuAns.setAnswer("A" + i % 4 + 1);
//            stuAnsList.add(stuAns);
//        }

        float score = 0;
        int count = 0;


        int tableCount = stuAnsList.size()/10 +1;
        for (int i = 0; i < tableCount; i++) {
            JTable table = new JTable(new DefaultTableModel(3, 11));
            table.setEnabled(false);
            table.setValueAt("题号", 0, 0);
            table.setValueAt("学生答案", 1, 0);
            table.setValueAt("参考答案", 2, 0);
            table.setBorder(new LineBorder(new Color(0, 0, 0)));
            table.setRowHeight(20);

            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            table.setDefaultRenderer(Object.class, tcr);

            for (int j = 1; j <= 10 && (j+(10*i)) <= stuAnsList.size(); j++) {
                table.setValueAt(j + 10 * i, 0, j);
                String answer = stuAnsList.get(j + (10 * i) - 1).getStudentAnswer();
                String rightAnswer = stuAnsList.get(j + (10 * i) - 1).getAnswer();
                table.setValueAt(rightAnswer, 2, j);
                if (answer.equals(rightAnswer)) {
                    table.setValueAt(answer, 1, j);
                    count++;
                }
                else
                    table.setValueAt("<html><font color=\"red\">" +
                            answer + "</font></html>", 1, j);
            }
            tableList.add(table);
        }


        for (int i = 0; i < tableCount; i++) {
            tablePanel.add(Box.createVerticalStrut(3));
            tablePanel.add(tableList.get(i));
        }

        score = unitScore * count;
        textField.setText(Float.toString(score));

        objectiveScore += score;


        add(tablePanel);

    }

    //显示填空  简答
    private void showSubjectiveAnswer(float unitScore, Integer type, List<StudentAnswer> stuAnsList) {
        int textLineCount = 0;


        float score = 0;
        int count = 0;

        //成绩显示
        final JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField textField = new JTextField(10);
        scorePanel.add(new JLabel("成绩："));


        if (type == 4) {
            textLineCount = 2;
            scorePanel.add(textField);
            textField.setEditable(false);
        }
        else {
            textLineCount = 5;
            scorePanel.add(compSumScore);
            compSumScore.setEnabled(false);
        }

        add(scorePanel);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(Color.WHITE);
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(answerPanel);

        for (Integer i = 1; i <= stuAnsList.size(); i++) {
            //题号显示
            final JPanel scoreNumberPanel = new JPanel();
            scoreNumberPanel.setBackground(Color.WHITE);
            scoreNumberPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            scoreNumberPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            scoreNumberPanel.add(new JLabel(i.toString() + "."));
            if (type == 5) {
                scoreNumberPanel.add(new JLabel("成绩："));
                JTextField compScoreTextFiled = new JTextField(10);
                scoreNumberPanel.add(compScoreTextFiled);
                comprehensiveScoreTextFiled.add(compScoreTextFiled);
            }
            answerPanel.add(scoreNumberPanel);

            //学生答案与参考答案panel
            JPanel textAreaPanel = new JPanel();
            textAreaPanel.setLayout(new GridLayout(2,1,0,3));
            textAreaPanel.setBackground(Color.WHITE);
            textAreaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            //学生答案
            final JPanel stuAnsPanel = new JPanel();
            stuAnsPanel.setBackground(Color.WHITE);
            stuAnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            //stuAnsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            final JTextArea textStuAns = new JTextArea(textLineCount, 120);
            textStuAns.setBackground(Color.WHITE);
            textStuAns.setEditable(false);
            textStuAns.setLineWrap(true);
            //textStuAns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            JScrollPane scrollStuAns = new JScrollPane(textStuAns);
            stuAnsPanel.add(scrollStuAns);

            //参考答案
            final JPanel correctAnsPanel = new JPanel();
            correctAnsPanel.setBackground(Color.WHITE);
            correctAnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            final JTextArea textCorrectAns = new JTextArea(textLineCount,120);
            textCorrectAns.setBackground(Color.WHITE);
            textCorrectAns.setEditable(false);
            textCorrectAns.setLineWrap(true);
            JScrollPane scrollCorrectAns = new JScrollPane(textCorrectAns);
            correctAnsPanel.add(scrollCorrectAns);

            String stuAnswer = stuAnsList.get(i-1).getStudentAnswer();
            String correctAnswer = stuAnsList.get(i-1).getAnswer();
            if (type == 4 && !(stuAnswer.equals(correctAnswer))) {
//                String[] student = stuAnswer.split(",");
//                String[] correct = stuAnswer.split(",");
//                for (int j = 0; j < correct.length; j++) {
//                    String s = student[j];
//                    String c = correct[j];
//                    if (!s.equals(c)) {
//                        appendToPane(textStuAns, s, Color.RED);
//                    }
//                    if (i == correct.length - 1) {
//                        appendToPane(textStuAns, ",", Color.BLACK);
//                    }
//                }
                textStuAns.setForeground(Color.RED);
            }

            if (type == 4 && stuAnswer.equals(correctAnswer)) {
                count++;
            }

            //else {
                textStuAns.setText("学生答案：\n\r" + stuAnswer);
            //}
            textCorrectAns.setText("参考答案：\n\r" + correctAnswer);

            textAreaPanel.add(stuAnsPanel);
            textAreaPanel.add(correctAnsPanel);

            answerPanel.add(Box.createVerticalStrut(3));
            answerPanel.add(textAreaPanel);
        }

        if (type == 4) {
            score = unitScore * count;
            textField.setText(Float.toString(score));
            objectiveScore += score;
        }


    }

    private void addPanel(float unitScore, Integer type, List<StudentAnswer> stuAnsList) {

        switch (type)
        {
            //单选  多选  判断
            case 1:
            case 2:
            case 3:
                showObjectiveAnswer(unitScore, stuAnsList);
                break;
            //填空  简答
            case 4:
            case 5:
                showSubjectiveAnswer(unitScore, type, stuAnsList);
                break;
            default:
                break;
        }

    }


    public static void main(String[] args) {
    }
}
