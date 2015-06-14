package org.homework.teacher;

import org.homework.db.DBConnecter;
import org.homework.db.model.TableQuestion;
import org.homework.io.IoOperator;
import org.homework.student.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static org.homework.utils.Utils.*;

/**
 * Created by lenovo on 2015/6/1.
 */
public class TTestPaper extends MouseAdapter {
    //                           ��Ŀ           ����             �½�                 ���
    public final static TreeMap<String,TreeMap<Integer,TreeMap<Integer, java.util.List<TableQuestion>>>> allTestQuestion = new TreeMap();

    JDialog jDialog=null; //����һ���յĶԻ������
    JButton createPaperBtn;
    JPanel centerPanel;
    JScrollPane centerScrollPane;

    static String course = null;

    //ÿ�����ͣ����µ�����
    ArrayList<ArrayList<JTextField>> textFieldArray = new ArrayList<ArrayList<JTextField>>();
    //ArrayList<ArrayList<Integer>> questionCount = new ArrayList<ArrayList<Integer>>();
    final Map<JCheckBox, TableQuestion> checkboxList = new HashMap();


    TreeMap<Integer, TreeMap<Integer, java.util.List<TableQuestion>>> tempTestMap = new TreeMap<Integer, TreeMap<Integer, java.util.List<TableQuestion>>>();

    static {
        java.util.List<TableQuestion> questions = DBConnecter.getAllQuestion();
        mapAllTestQuestion(allTestQuestion, questions);
    }

    public TTestPaper(JFrame jFrame) {

        String[] courseArray = new String[allTestQuestion.size() + 1];
        courseArray[0] = "";
        int i = 1;
        int rowMax = 0;
        int columnMax = 0;

        //��Ŀ       ����         �½�
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, List<TableQuestion>>>> entry : allTestQuestion.entrySet()) {
            courseArray[i++] = entry.getKey();
            //if (flag) {
                for (Map.Entry<Integer, TreeMap<Integer, List<TableQuestion>>> entry2 : entry.getValue().entrySet()) {
                    if (entry.getValue().size() > rowMax) {
                        rowMax = entry.getValue().size();
                    }
                    for (Map.Entry<Integer, List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                        if (entry2.getValue().size() > columnMax) {
                            columnMax = entry2.getValue().size();
                        }
                    }
                }
        }

        for (int j = 0; j < rowMax-1; j++) {
            ArrayList<JTextField> tmp = new ArrayList<JTextField>();
            for (int k = 0; k < columnMax; k++) {
                tmp.add(new JTextField(20));
            }
            textFieldArray.add(tmp);
        }

                /* ��ʼ��jDialog1
        * ָ���Ի����ӵ����ΪjFrame,����Ϊ"Dialog",���Ի���Ϊ����ʱ,������������
        * �����û�������(��̬�Ի���) */
        jDialog = new JDialog(jFrame,"�����Ծ�",true);

        jDialog.setSize(new Dimension(650, 620));
        /* ���öԻ����ʼ��ʾ����Ļ���е�λ�� */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        jDialog.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel courseLabel = new JLabel("��Ŀ");
        courseLabel.setBackground(Color.WHITE);
        final JComboBox courseComboBox = new JComboBox(courseArray);
        courseComboBox.setBackground(Color.WHITE);

        topPanel.add(courseLabel);
        topPanel.add(courseComboBox);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        centerScrollPane = new JScrollPane(centerPanel);
        jDialog.getContentPane().add(centerScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        jDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        createPaperBtn = new JButton("�����Ծ�");
        bottomPanel.add(createPaperBtn);

        createPaperBtn.addMouseListener(this);

        courseComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        TTestPaper.course = (String)e.getItem();
                        //tempTestMap.clear();
                        //textFieldArray.clear();
                        for (ArrayList<JTextField> list : textFieldArray) {
                            for (JTextField list2 : list) {
                                list2.setText(null);
                            }
                        }
                        //questionCount.clear();
                        showCenterPanel();
                        break;
                    default:
                        break;
                }
            }
        });

        jDialog.setVisible(true);
    }

    private void showCenterPanel() {
//        for (int i = 0; i < 50; i++) {
//            JLabel lab = new JLabel("i");
//            centerPanel.add(lab);
//        }


        jDialog.getContentPane().remove(centerScrollPane);
        centerPanel.removeAll();

        //���ڷ���textFieldArray
        int i = 0;
        int j = 0;
        //��Ŀ
        for (Map.Entry<String, TreeMap<Integer, TreeMap<Integer, java.util.List<TableQuestion>>>> entry1 : allTestQuestion.entrySet()) {
            if (TTestPaper.course.equals(entry1.getKey())) {
                //����
                for (Map.Entry<Integer,TreeMap<Integer,java.util.List<TableQuestion>>> entry2 : entry1.getValue().entrySet()) {
                    tempTestMap.put(entry2.getKey(), entry2.getValue());

                    int type = entry2.getKey();

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.setBackground(Color.WHITE);
                    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                    JScrollPane scrollPane = new JScrollPane(panel);
                    centerPanel.add(scrollPane);

                    if (entry2.getKey() != 5) {
                        JPanel panel_2 = new JPanel();
                        panel_2.setLayout(new GridLayout(entry2.getValue().size()+1, 1));
                        panel_2.setBackground(Color.WHITE);
                        panel.add(panel_2);

                        JLabel labelTypeExplain = buildLabel();
                        labelTypeExplain.setText(getTypeWord(type));
                        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                        labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));
                        panel_2.add(labelTypeExplain);

                        //�½�
                        for (Map.Entry<Integer,java.util.List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {
                            JPanel panel_3 = new JPanel();
                            panel_3.setLayout(new FlowLayout(FlowLayout.LEFT));
                            panel_3.setBackground(Color.WHITE);
                            panel_2.add(panel_3);

                            String s1 = "��" + entry3.getKey() + "��(��" + entry3.getValue().size() + "��С��)";
                            JLabel label_1 = new JLabel(s1);
                            panel_3.add(label_1);
                            panel_3.add(textFieldArray.get(i).get(j));
                            panel_3.add(new JLabel("��"));

//                            java.util.List<TableQuestion> list = tempTestMap.get(entry2.getKey());
//                            if (list == null) {
//                                list = new ArrayList<TableQuestion>();
//                                tempTestMap.put(entry2.getKey(), list);
//                            }
//                            for (int k = 0; k < entry3.getValue().size(); k++) {
//                                list.add(entry3.getValue().get(k));
//                            }
                            j++;
                        }
                        j = 0;
                        i++;
                    }
                    else {
                        JPanel panel_2_1 = new JPanel();
                        panel_2_1.setLayout(new BoxLayout(panel_2_1, BoxLayout.Y_AXIS));
                        panel_2_1.setBackground(Color.WHITE);
                        panel.add(panel_2_1);

                        JLabel labelTypeExplain = buildLabel();
                        labelTypeExplain.setText(getTypeWord(type));
                        labelTypeExplain.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                        labelTypeExplain.setFont(new Font("����", Font.BOLD, 15));
                        panel_2_1.add(labelTypeExplain);

                        //�½�
                        for (final Map.Entry<Integer,java.util.List<TableQuestion>> entry3 : entry2.getValue().entrySet()) {

                            for (int k = 1; k <= entry3.getValue().size(); k++) {
                                String s1 = "��" + entry3.getKey() + "��.��" + k + "��";
                                JCheckBox checkBox = new JCheckBox(s1);
                                checkBox.setBackground(Color.WHITE);
                                panel_2_1.add(checkBox);
                                checkboxList.put(checkBox, entry3.getValue().get(k-1));

                                String startSentence = entry3.getValue().get(k-1).getMain_content();
                                if(startSentence.startsWith("[")){//ͼƬ
                                    JLabel startLabel = new JLabel();
                                    startLabel.setIcon(getIcon(ContentPanel.PRE + startSentence.substring(1,startSentence.length()-1)));
                                    startLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
                                    panel_2_1.add(startLabel);
                                }else {
                                    JTextArea jTextArea = new JTextArea();
                                    jTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                                    jTextArea.setBackground(null);
                                    jTextArea.setEditable(false);
                                    jTextArea.setLineWrap(true);
                                    jTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                                    jTextArea.setText(startSentence);
                                    jTextArea.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                                    panel_2_1.add(jTextArea);
                                }

                                checkBox.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {

                                    }
                                });
                            }

                        }

                    }

                }
            }
        }
        jDialog.getContentPane().add(centerScrollPane, BorderLayout.CENTER);
        jDialog.setVisible(true);


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TreeMap<Integer, List<TableQuestion>> resultTestMap = new TreeMap<Integer, List<TableQuestion>>();
        if (e.getSource() == createPaperBtn) {
            for (int i = 0; i < textFieldArray.size(); i++) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int j = 0; j < textFieldArray.get(i).size(); j++) {
                    String s = textFieldArray.get(i).get(j).getText();
                    if (!s.equals("")) {
                        int count = Integer.parseInt(s);
                        list.add(count);

                        ArrayList<TableQuestion> list1 = new ArrayList<TableQuestion>();
                        //System.out.println(list1);
                        for (int k = 0; k < tempTestMap.get(i+1).get(j+1).size(); k++) {
                            list1.add(tempTestMap.get(i+1).get(j+1).get(k));
                        }

                        for (int k = 0; k < count; k++) {
                            Random random = new Random();
                            int index = random.nextInt(list1.size());
                            java.util.List<TableQuestion> list2 = resultTestMap.get(i+1);
                            if (list2 == null) {
                                list2 = new ArrayList<TableQuestion>();
                                resultTestMap.put(i+1, list2);
                            }
                            list2.add(list1.get(index));
                            list1.remove(index);
                        }

                    }
                }
                //questionCount.add(list);


            }
            //System.out.println(questionCount);
            //System.out.println(tempTestMap);
            //System.out.println("FUCK��" + resultTestMap);

            //fuxuankuang
            //super.mouseClicked(e);
            for (Map.Entry<JCheckBox, TableQuestion> entry4 : checkboxList.entrySet()) {
                if (entry4.getKey().isSelected()) {
                    //resultTestMap.put(5, entry.getValue());
                    java.util.List<TableQuestion> list = resultTestMap.get(5);
//                    boolean have = false;
                    if (list == null) {
                        list = new ArrayList<TableQuestion>();
                        resultTestMap.put(5, list);
                    }
//                    for (TableQuestion t : list){
//                        if(t.equals(entry4.getValue())) {
//                            have = true;
//                            break;
//                        }
//                    }
//                    if(!have)
                        list.add(entry4.getValue());
                }
            }

            //System.out.println("FUCK FUCK  FUCK :::::" + resultTestMap);

            IoOperator.generatePDF(-1, resultTestMap);
        }
    }
}
