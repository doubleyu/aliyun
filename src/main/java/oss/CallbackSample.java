package oss;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.Callback.CalbackBodyType;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 上传回调使用示例
 *
 */
public class CallbackSample {

    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "GFLpv2HYrtQMJqQM";
    private static String accessKeySecret = "9gHWEXkYndPRn1q1NafTw3vHNIJ5Tr";
    private static String bucketName = "my-oss-bucket74f32080-35d0-46a8-847c-c269469eaa5a";
    // 您的回调服务器地址，如http://oss-demo.aliyuncs.com:23450或http://30.2.36.126:9090
    private static final String callbackUrl = "http://127.0.0.1:9090";


    public static void main(String[] args) throws IOException {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            String content = "Hello OSS";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "key",
                    new ByteArrayInputStream(content.getBytes()));

            Callback callback = new Callback();
            callback.setCallbackUrl(callbackUrl);
            callback.setCallbackHost("oss-cn-hangzhou.aliyuncs.com");
            callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
            callback.setCalbackBodyType(CalbackBodyType.JSON);
            callback.addCallbackVar("x:var1", "value1");
            callback.addCallbackVar("x:var2", "value2");
            putObjectRequest.setCallback(callback);

            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            byte[] buffer = new byte[1024];
            putObjectResult.getCallbackResponseBody().read(buffer);
            putObjectResult.getCallbackResponseBody().close();

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
            ossClient.shutdown();
        }
    }
}