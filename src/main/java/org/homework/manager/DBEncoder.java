package org.homework.manager;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.homework.utils.Utils.getPath;

/**
 * Created by hasee on 2015/5/26.
 */
public class DBEncoder {
    static Path path = Paths.get(getPath("main.db").substring(1,getPath("main.db").length()));
//    static Path path = Paths.get(getPath("main.db"));
    public static String initMainDB() throws Exception {
        byte[] newDBBytes = SecurityEncode.coderByDES(Files.readAllBytes(path),ManagerMain.key, Cipher.DECRYPT_MODE);
        final Path tmpPath = Files.createTempFile("log", ".sp");
        Files.write(tmpPath, newDBBytes);

        //main.db是不变的，不用更新，只能通过管理员改变
//        new Thread(new Runnable() {
//            public void run() {
//                while (true){
//                    try {
//                        byte[] newDBBytes = SecurityEncode.coderByDES(Files.readAllBytes(tmpPath),ManagerMain.key, Cipher.ENCRYPT_MODE);
//                        Files.write(path, newDBBytes);
//                        Thread.sleep(5000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.exit(0);
//                    }
//                }
//            }
//        }).start();

        return tmpPath.toString();
    }

    public static void main(String[] args) throws Exception{
        Path path = Files.createTempFile("log", ".sp");
        System.out.println(path);
    }
}
