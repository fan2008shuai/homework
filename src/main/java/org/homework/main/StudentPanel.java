package org.homework.main;

import org.homework.student.CatalogTree;
import org.homework.student.ContentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by hasee on 2015/5/21.
 */
public class StudentPanel extends JPanel {

    public static JPanel leftPanel;
    public static JPanel rightPanel;
    ContentPanel contentPanel;

    public StudentPanel(){
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        //2.1 右显示
        rightPanel = new JPanel();
        rightPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        rightPanel.setBackground(Color.WHITE);
        add(rightPanel, BorderLayout.CENTER);
        rightPanel.setLayout(new BorderLayout(0, 0));

        contentPanel = ContentPanel.loadContentPanel();
        rightPanel.add(contentPanel.getScrollPane(), BorderLayout.CENTER);

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

        JLabel lblNewLabel_1 = new JLabel("题库目录");
        lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 15));
        panel_2.add(lblNewLabel_1);

        CatalogTree cata = new CatalogTree();
        JScrollPane scrollPane = new JScrollPane(cata.getTree());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
