package com.dby.threads;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.dby.io.ReadBigFile;
import oss.OSSClientManager;
import oss.OSSSimpleUploader;


import java.io.*;
import java.util.concurrent.*;

/**
 * Created by Admin on 2016/5/18.
 */
public class ExecutorServices {
    private static final int threadCount = Runtime.getRuntime()
            .availableProcessors();
    public static final OSSSimpleUploader ossUploader = new OSSSimpleUploader();

    private static final ExecutorService exec = new ThreadPoolExecutor(
            threadCount, threadCount, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(threadCount * 3),
            new RejectedExecutionHandler() {

                @Override
                public void rejectedExecution(Runnable r,
                                              ThreadPoolExecutor executor) {
                    try {
                        // block until there's room
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        throw new RejectedExecutionException(
                                "Unexpected InterruptedException", e);
                    }
                }
            });

    public static void main(String args[]) {

        for (String fileName : ReadBigFile.readBigTxt()) {
            String localPath = "E:/vbox/gjqt-log/log_file/" + fileName;
            exec.execute(new UploadRunner(localPath, fileName));

        }

        ;


//            for(int i=0;i<100;i++){
//            exec.execute(new UploadRunner(Integer.toString(i),Integer.toString(i)));
//            System.out.println("************* a" + i + " *************");
//            }
        exec.shutdown();

/*        int threadCounter = 0;
        while (true) {
            threadCounter++;
            // Adding threads one by one
            System.out.println("Adding DemoTask : " + threadCounter);
            exec.execute(new UploadRunner());

            if (threadCounter == 100)
                break;
        }*/


    }
}

class UploadRunner implements Runnable {
    private static final String bucketName = "my-oss-bucket74f32080-35d0-46a8-847c-c269469eaa5a";//此处暂时写死
    private static final String fileListName = "E:/vbox/gjqt-log/log_file/fileList.txt";
    private String localPath;
    private String ossPath;

    OSSClient ossClient = OSSClientManager.getInstance().getOSSClient();

    UploadRunner(String localPath, String ossPath) {
        this.localPath = localPath;
        this.ossPath = ossPath;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程被调用了。");
        System.out.println("OSSClient : "+ossClient);

        File file = new File(localPath);
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, ossPath, file));
        } catch (OSSException e) {
            e.printStackTrace();
            return;
        } catch (ClientException e) {
            e.printStackTrace();
            return;
        } catch (Exception e){
            return;
        }
        System.out.println("FINISH " + localPath);
        writeToFileList(ossPath);
    }

    private synchronized void writeToFileList(String line) {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(fileListName, true));
            output.append(line + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


 class TestSingletonRunner implements Runnable{

     @Override
     public void run() {
         System.out.println(Thread.currentThread().getName() + "线程被调用了。");

         OSSClient ossClient = OSSClientManager.INSTANCE.getOSSClient();
         System.out.println("OSSClient : "+ossClient);
     }
 }
