package oss;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

/**
 * This sample demonstrates how to upload/download an object to/from
 * Aliyun OSS using the OSS SDK for Java.
 */
public class SimpleGetObjectSample {

    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "GFLpv2HYrtQMJqQM";
    private static String accessKeySecret = "9gHWEXkYndPRn1q1NafTw3vHNIJ5Tr";

    private static String bucketName = "my-oss-bucket74f32080-35d0-46a8-847c-c269469eaa5a";
    private static String key = "myfirstfile";

    public static OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    public static void main(String[] args) throws IOException {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
//        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {

            /**
             * Note that there are two ways of uploading an object to your bucket, the one
             * by specifying an input stream as content source, the other by specifying a file.
             */

            /*
             * Upload an object to your bucket from an input stream
             */
//            System.out.println("Uploading a new object to OSS from an input stream\n");
//            String content = "Thank you for using Aliyun Object Storage Service";
//            client.putObject(bucketName, key, new ByteArrayInputStream(content.getBytes()));

            /*
             * Upload an object to your bucket from a file
             */
//            System.out.println("Uploading a new object to OSS from a file\n");
//            client.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));

            // 上传
//            InputStream inputStream = new URL("https://www.aliyun.com/").openStream();
//            client.putObject(bucketName, key, inputStream);


/*            //创建文件夹
            final String keySuffixWithSlash = "parent_directory/";
            client.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));*/


            /*
             * Download an object from your bucket
             */
//            System.out.println("Downloading an object");
//            OSSObject object = client.getObject(new GetObjectRequest(bucketName, key));
//            System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
//            displayTextInputStream(object.getObjectContent());


            SimpleGetObjectSample sgos = new SimpleGetObjectSample();
            sgos.uploadToOSS(bucketName, "E:\\vbox\\gjqt-log\\log_file\\2\\svr_2_92_20160222000000401.log", "2/svr_2_92_20160222000000401.log");

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
    }

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("0123456789011234567890\n");
        writer.close();

        return file;
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("\t" + line);
        }
        System.out.println();

        reader.close();
    }


    public boolean uploadToOSS(String bucketName, String localPath, String ossPath) {
        try {
            File file = new File(localPath);
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, ossPath, file));
            // 读取上传回调返回的消息内容
/*        byte[] buffer = new byte[1024];

        // 一定要close，否则会造成连接资源泄漏
        try {
            putObjectResult.getCallbackResponseBody().read(buffer);
            putObjectResult.getCallbackResponseBody().close();
            System.out.println(Arrays.toString(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            System.out.println("UPLOAD SUCCEED");
            return true;
        }  catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
