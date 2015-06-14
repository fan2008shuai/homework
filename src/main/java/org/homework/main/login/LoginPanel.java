package org.homework.main.login;

import org.homework.db.DBConnecter;
import org.homework.db.model.User;
import org.homework.main.MainFrame;
import org.homework.manager.SecurityEncode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Created by hasee on 2015/5/30.
 */
public class LoginPanel  extends JFrame implements ActionListener {
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnOK;
    private JButton btnExit;
    String trueCipher = DBConnecter.getKV("cipher");

    private Path path;
    private File file;
    private boolean fileExisted;

    public LoginPanel() throws FileNotFoundException {
        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(20, 30, 10, 30));
        p1.setLayout(new BorderLayout());
        lblUsername = new JLabel("用户名:");
        tfUsername = new JTextField(12);
        p1.add(lblUsername, BorderLayout.WEST);
        p1.add(tfUsername, BorderLayout.CENTER);
        JPanel p2 = new JPanel();
        //p2.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        p2.setBorder(BorderFactory.createEmptyBorder(10,30, 10, 30));
        p2.setLayout(new BorderLayout());
        lblPassword = new JLabel("\u5BC6  \u7801:");
        tfPassword = new JPasswordField(12);
        p2.add(lblPassword, BorderLayout.WEST);
        p2.add(tfPassword, BorderLayout.CENTER);
        JPanel p3 = new JPanel();
        p3.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnOK = new JButton("登录");
        btnOK.addActionListener(this);
        btnExit = new JButton("取消");
        btnExit.addActionListener(this);
        p3.add(btnOK);
        p3.add(btnExit);
        getContentPane().add(p1, BorderLayout.NORTH);
        getContentPane().add(p2, BorderLayout.CENTER);
        getContentPane().add(p3, BorderLayout.SOUTH);

        file = new File("loginFile");
        fileExisted = file.exists();

        if (fileExisted) {
            Scanner scanner = new Scanner(file);
            int i = 0;
            String[] logInfo = new String[2];
            while (scanner.hasNext()) {
                logInfo[i++] = scanner.next();
            }
            scanner.close();
            tfUsername.setText(logInfo[0]);
            tfPassword.setText(logInfo[1]);
        }

        this.setLocation(400, 300);
        this.setSize(300, 200);
        this.setTitle("登录");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("登录")) {

            String name = tfUsername.getText();
            String password = new String(tfPassword.getPassword());


            System.out.println(name + " " + password);
            if(name.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
                return;
            }
            User user = DBConnecter.getUser(name);
            //第一重验证
            if(user == null || !user.getPassword().equals(password)){
                JOptionPane.showMessageDialog(this, "用户名或密码错误！");
                return;
            }
            String text = SecurityEncode.getFromBASE64(user.getPassword());
            System.out.println("HEHE" + name + " " + text);
            String[] strs = text.split("_");
            System.out.println(strs);
            //第二重和第三重验证，磁盘码和密钥
            if(!strs[strs.length-2].equals(UserVerify.getCDiskNum()) ||
                    !strs[strs.length-1].equals(trueCipher)){
                JOptionPane.showMessageDialog(this, "机器匹配或密钥错误！");
                System.out.println(strs[strs.length-2] + " " + strs[strs.length-1]);
                return;
            }else {
                if (!fileExisted) {
                    try {
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        PrintWriter pw = new PrintWriter(file);
                        pw.println(name);
                        pw.println(password);
                        pw.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }

                MainFrame frame = new MainFrame(user);
                String title = DBConnecter.getKV("title");
                if(title == null)
                    frame.setTitle("作业系统");
                else
                    frame.setTitle(title);
                frame.setVisible(true);
                this.dispose();
            }
        } else if (e.getActionCommand().equals("取消")) {
            System.exit(0);
        }
    }
}
