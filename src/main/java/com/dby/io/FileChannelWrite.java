package com.dby.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileReader;
import java.io.BufferedReader;

public class FileChannelWrite {
    public static void main (String [] args)
            throws Exception {

        new FileChannelWrite().writeFile();
    }
    private void writeFile()
            throws IOException {

        String input = "file channel example";
        System.out.print("Input string: " + input);

        byte [] inputBytes = input.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(inputBytes);

        String filePath = "writefile.txt";
        FileOutputStream fos = new FileOutputStream(filePath);
        FileChannel fileChannel = fos.getChannel();
        fileChannel.write(buffer);
        fileChannel.close();
        fos.close();

        printFileContents(filePath);
    }
    private void printFileContents(String path)
            throws IOException {

        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String textRead = br.readLine();
        System.out.println("File contents: ");

        while (textRead != null) {

            System.out.println("     " + textRead);
            textRead = br.readLine();
        }
        fr.close();
        br.close();
    }
}
