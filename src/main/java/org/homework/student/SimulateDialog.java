package org.homework.student;

import org.homework.db.model.TableQuestion;
import org.homework.main.StudentPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import static org.homework.utils.Utils.getChineseNum;

/**
 * Created by hasee on 2015/5/8.
 */
public class SimulateDialog extends MouseAdapter{

    JDialog jDialog=null; //创建一个空的对话框对象
    private JTextField textField;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    JPanel checkPanel;
    TreeMap<Integer, java.util.List<TableQuestion>> questionMap = new TreeMap();
    TreeMap<Integer, java.util.List<TableQuestion>> resultMap = new TreeMap();

    public SimulateDialog(JFrame jFrame) {
        /* 初始化jDialog1
        * 指定对话框的拥有者为jFrame,标题为"Dialog",当对话框为可视时,其他构件不能
        * 接受用户的输入(静态对话框) */
        jDialog = new JDialog(jFrame,"模拟考试",true);

        jDialog.setSize(new Dimension(536, 283));
        /* 设置对话框初始显示在屏幕当中的位置 */
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width - jDialog.getWidth()) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - jDialog.getHeight()) / 2;
        jDialog.setLocation(w, h);

        jDialog.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("\u8BBE\u7F6E\u9898\u91CF");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 18));
		lblNewLabel.setBounds(79, 10, 90, 21);
        jDialog.getContentPane().add(lblNewLabel);

		JLabel label = new JLabel("\u8BD5\u9898\u79D1\u76EE");
		label.setFont(new Font("宋体", Font.BOLD, 18));
		label.setBounds(347, 10, 90, 21);
        jDialog.getContentPane().add(label);

		JButton btnNewButton = new JButton("\u5F00\u59CB\u8003\u8BD5");
		btnNewButton.setBounds(417, 217, 93, 23);
        jDialog.getContentPane().add(btnNewButton);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 41, 233, 166);
        jDialog.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label_1 = new JLabel("\u9898");
		label_1.setBounds(152, 13, 28, 15);
		panel.add(label_1);

		textField = new JTextField();
		textField.setBounds(102, 10, 37, 21);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("1. \u5355\u9009\u9898");
		lblNewLabel_1.setBounds(38, 13, 54, 15);
		panel.add(lblNewLabel_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(102, 38, 37, 21);
		panel.add(textField_2);

		JLabel label_4 = new JLabel("2. \u591A\u9009\u9898");
		label_4.setBounds(38, 41, 54, 15);
		panel.add(label_4);

		JLabel label_5 = new JLabel("\u9898");
		label_5.setBounds(152, 41, 28, 15);
		panel.add(label_5);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(102, 69, 37, 21);
		panel.add(textField_3);

		JLabel label_6 = new JLabel("3. \u5224\u65AD\u9898");
		label_6.setBounds(38, 72, 54, 15);
		panel.add(label_6);

		JLabel label_7 = new JLabel("\u9898");
		label_7.setBounds(152, 72, 28, 15);
		panel.add(label_7);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(102, 100, 37, 21);
		panel.add(textField_4);

		JLabel label_8 = new JLabel("4. \u586B\u7A7A\u9898");
		label_8.setBounds(38, 103, 54, 15);
		panel.add(label_8);

		JLabel label_9 = new JLabel("\u9898");
		label_9.setBounds(152, 103, 28, 15);
		panel.add(label_9);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(102, 131, 37, 21);
		panel.add(textField_5);

		JLabel label_10 = new JLabel("5. \u7B80\u7B54\u9898");
		label_10.setBounds(38, 134, 54, 15);
		panel.add(label_10);

		JLabel label_11 = new JLabel("\u9898");
		label_11.setBounds(152, 134, 28, 15);
		panel.add(label_11);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(277, 41, 233, 166);
        jDialog.getContentPane().add(scrollPane);

		JLabel label_2 = new JLabel("\u79D1\u76EE\u540D\u79F0");
		scrollPane.setColumnHeaderView(label_2);

		checkPanel = new JPanel();
		scrollPane.setViewportView(checkPanel);
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        initCheckPanel();
        btnNewButton.addMouseListener(this);

        jDialog.setVisible(true);
    }

    private void initCheckPanel() {
        final Map<JCheckBox,TreeMap<Integer, java.util.List<TableQuestion>>> boxList = new HashMap();
        for (final Map.Entry<String,TreeMap<Integer,TreeMap<Integer, java.util.List<TableQuestion>>>> entry1 : CatalogTree.allData.entrySet()){
            for (Map.Entry<Integer,TreeMap<Integer, java.util.List<TableQuestion>>> entry2 : entry1.getValue().entrySet()){
                String str = entry1.getKey() + "." + "第" + getChineseNum(entry2.getKey()) + "章";
                JCheckBox checkBox = new JCheckBox(str);
                checkPanel.add(checkBox);
                boxList.put(checkBox, entry2.getValue());
                checkBox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        questionMap = new TreeMap();
                        for (Map.Entry<JCheckBox,TreeMap<Integer, java.util.List<TableQuestion>>> entry3 : boxList.entrySet()){
                            if(entry3.getKey().isSelected()){
                                for (Map.Entry<Integer, java.util.List<TableQuestion>> entry4 : entry3.getValue().entrySet()){
                                    if(!questionMap.containsKey(entry4.getKey()))
                                        questionMap.put(entry4.getKey(),new ArrayList<TableQuestion>());
                                    questionMap.get(entry4.getKey()).addAll(entry4.getValue());
                                }
                            }
                        }
                    }
                });
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        resultMap = new TreeMap();
        String str1 = textField.getText();
        if(!str1.equals(""))
            find2QuestionList(1,Integer.parseInt(str1));
        String str2 = textField_2.getText();
        if(!str2.equals(""))
            find2QuestionList(2,Integer.parseInt(str2));
        String str3 = textField_3.getText();
        if(!str3.equals(""))
            find2QuestionList(3,Integer.parseInt(str3));
        String str4 = textField_4.getText();
        if(!str4.equals(""))
            find2QuestionList(4,Integer.parseInt(str4));
        String str5 = textField_5.getText();
        if(!str5.equals(""))
            find2QuestionList(5, Integer.parseInt(str5));

        StudentPanel.leftPanel.setVisible(false);
        ContentPanel.reloadContentPanel().fullContent("模拟考试", resultMap);
        jDialog.dispose();
    }

    private void find2QuestionList(int type,int number){
        java.util.List<TableQuestion> list = new ArrayList<TableQuestion>();
        java.util.List<TableQuestion> oldList = questionMap.get(type);
        if(oldList != null){
            Collections.shuffle(oldList);
            for (int i = 0; i<number && i<oldList.size(); i++) {
                list.add(oldList.get(i));
            }
            resultMap.put(type,list);
        }
    }

    public static void main(String[] args){
    }


}
