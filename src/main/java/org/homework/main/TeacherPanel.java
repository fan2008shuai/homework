package org.homework.main;

import org.homework.teacher.TCatalogTree;
import org.homework.teacher.TContentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by hasee on 2015/5/21.
 */
public class TeacherPanel extends JPanel {

    public static JPanel leftPanel;
    TContentPanel tContentPanel;

    public TeacherPanel(){
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        //setBackground(Color.GRAY);

        //2.1 右显示
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBackground(Color.WHITE);
        add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));

        tContentPanel = TContentPanel.getTContentPanel();
        panel_1.add(tContentPanel.getScrollPane(), BorderLayout.CENTER);

        //2.2 左目录
        leftPanel = new JPanel();
        leftPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(280, -1));
        add(leftPanel, BorderLayout.WEST);
        leftPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setBackground(new Color(184, 208, 238));
        panel_2.setPreferredSize(new Dimension(-1, 30));
        leftPanel.add(panel_2, BorderLayout.NORTH);

        JLabel lblNewLabel_1 = new JLabel("学生作业");
        lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 15));
        panel_2.add(lblNewLabel_1);

        TCatalogTree cata = new TCatalogTree();
        JScrollPane scrollPane = new JScrollPane(cata.getTree());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
