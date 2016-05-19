package oss;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.Arrays;

/**
 * This sample demonstrates how to upload/download an object to/from
 * Aliyun OSS using the OSS SDK for Java.
 */
public class OSSSimpleUploader {

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

            OSSSimpleUploader sgos = new OSSSimpleUploader();
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
            client.putObject(new PutObjectRequest(OSSSimpleUploader.bucketName, ossPath, file));
            System.out.println("UPLOAD SUCCEED");
            return true;
        } catch (OSSException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e){
            return false;
        }

    }

}
