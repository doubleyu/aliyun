package com.dby.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadBigFile {
    public static void main(String[] args) throws Exception {
        for(String str : readBigTxt()){
            System.out.println(str);
        };
    }

    public static List<String> readBigTxt() {
        List<String> fileToUploadlst = new ArrayList<String>();
        //原文件路径
        String filePath = "E:/vbox/gjqt-log/log_file/LogFile.txt";
        File file = new File(filePath);
        BufferedInputStream bis = null;
        BufferedReader in = null;
        FileWriter fw = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            //每次读取10M到缓冲区
            in = new BufferedReader(new InputStreamReader(bis, "UTF-8"),
                    10 * 1024 * 1024);
            int count = 0;
            while (in.ready()) {
                //当count>20000的时候退出循环，只是测试，不需要读取太多行数据
                if (count > 200) {
                    break;
                }
                String line = in.readLine();
                fileToUploadlst.add(line);
                count++;
//                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fw != null) {
                    fw.flush();
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件输出完成");
        return fileToUploadlst;
    }
}
