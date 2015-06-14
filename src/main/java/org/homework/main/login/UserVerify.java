package org.homework.main.login;

import org.homework.manager.SecurityEncode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by hasee on 2015/5/30.
 */
public class UserVerify {

    public static String getCDiskNum(){
        return getSerialNumber("C");
    }
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("damn", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber"; // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;

            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    public static String getMac() {
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;

                StringBuilder builder = new StringBuilder();
                for (byte b : mac) {
                    char hex[] = {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f'
                    };
                    builder.append("" + hex[(b >> 4) & 0x0f] + hex[b & 0x0f]);
                    builder.append("-");
                }
                builder.deleteCharAt(builder.length() - 1);
                return builder.toString();

            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    JFrame frame = new JFrame();
                    frame.setLocation(400, 300);
                    frame.setSize(464, 140);
                    frame.setTitle("获取磁盘码");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().setLayout(null);

                    JLabel label_1 = new JLabel("磁盘码：");
                    label_1.setBounds(24, 32, 70, 15);
                    frame.getContentPane().add(label_1);
                    final JTextField password = new JTextField("");
                    password.setEditable(false);
                    password.setBounds(88, 26, 322, 27);
                    frame.getContentPane().add(password);
                    JButton btnNewButton = new JButton("获取");
                    btnNewButton.setBounds(345, 66, 93, 23);
                    btnNewButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            password.setText(getSerialNumber("C"));
                        }
                    });
                    frame.getContentPane().add(btnNewButton);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(getSerialNumber("C"));
    }
}
