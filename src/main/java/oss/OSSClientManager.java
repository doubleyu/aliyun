package oss;

import com.aliyun.oss.OSSClient;

/**
 * Created by Admin on 2016/5/18.
 */
public enum OSSClientManager{
    INSTANCE;
    private String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId = "GFLpv2HYrtQMJqQM";
    private String accessKeySecret = "9gHWEXkYndPRn1q1NafTw3vHNIJ5Tr";

   private final OSSClient ossClient;

    OSSClientManager() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public static OSSClientManager getInstance(){
        return INSTANCE;
    }

    public OSSClient getOSSClient(){
        return ossClient;
    }


}
