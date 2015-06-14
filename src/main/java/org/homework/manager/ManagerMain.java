package org.homework.manager;

import org.homework.db.DBConnecter;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by hasee on 2015/5/30.
 */
public class ManagerMain extends JFrame{

    private final JTextField password;
    private JPasswordField passwordField;
    private JTextField textField;
    String trueCipher = DBConnecter.getKV("cipher");
    public static final String key = "我真真的是key啊哦~呵。";

    public ManagerMain() {
        this.setLocation(400, 300);
        this.setSize(464, 221);
        this.setTitle("注册机");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("\u5BC6    \u94A5\uFF1A");
        lblNewLabel.setBounds(24, 28, 70, 15);
        getContentPane().add(lblNewLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(88, 22, 322, 27);
        getContentPane().add(passwordField);

        JLabel label = new JLabel("信息输入：");
        label.setBounds(24, 70, 70, 15);
        getContentPane().add(label);

        textField = new JTextField();
        textField.setBounds(88, 64, 322, 27);
        getContentPane().add(textField);
        textField.setColumns(10);

        JButton button = new JButton("生成加密库");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String cipher = new String(passwordField.getPassword());
                String text = textField.getText();
                if (cipher.equals(trueCipher)) {
                    generateCode();
                } else {
                    JOptionPane.showMessageDialog(null, "密钥错误！");
                }
            }
        });
        button.setBounds(242, 146, 93, 23);
        getContentPane().add(button);

        JButton btnNewButton = new JButton("生成密钥");
        btnNewButton.setBounds(345, 146, 93, 23);
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String cipher = new String(passwordField.getPassword());
                String text = textField.getText();
                if (cipher.equals(trueCipher)) {
                    System.out.println("HEHE:" + cipher + text);
                    password.setText(SecurityEncode.getBASE64(text + "_" + cipher));
                } else {
                    JOptionPane.showMessageDialog(null, "密钥错误！");
                }
            }
        });
        getContentPane().add(btnNewButton);

        JLabel label_1 = new JLabel("\u6CE8 \u518C \u7801\uFF1A");
        label_1.setBounds(24, 112, 70, 15);
        getContentPane().add(label_1);

        password = new JTextField("");
        password.setEditable(false);
        password.setBounds(88, 106, 322, 27);
        getContentPane().add(password);
        this.setVisible(true);
    }

    private void generateCode() {
        JFileChooser fileChooser = new JFileChooser("F:\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] newDBBytes = SecurityEncode.coderByDES(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        key, Cipher.ENCRYPT_MODE);
                File newF = new File(file.getParentFile().getAbsolutePath()+"/main.db");
                OutputStream out = new FileOutputStream(newF);
                out.write(newDBBytes);
                JOptionPane.showMessageDialog(null, "生成成功！");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"生成失败！请使用管理者权限运行！");
            }
        }
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new ManagerMain();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
