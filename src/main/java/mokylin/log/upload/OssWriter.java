package mokylin.log.upload;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.transfer.TransferManager;
//import com.amazonaws.services.s3.transfer.Upload;
//import com.amazonaws.services.s3.transfer.internal.S3ProgressListener;

public class OssWriter {
    private static final Logger logger = LoggerFactory
            .getLogger(OssWriter.class);

    private static final String pathPrefix = "log/log/";

//    private final TransferManager manager;

    private final String bucket;

    private static final ExecutorService exec = new ThreadPoolExecutor(20, 20,
            60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5000),
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

    public OssWriter() {
//        logger.debug("初始化s3");
//        // --- get bucket ---
//        String b = System.getenv(Config.ENV_KEY_S3_BUCKET);
//        if (b == null){
//            b = Config.DEFAULT_S3_BUCKET;
//        }
        this.bucket = null;
//
//        // --- create client ---
//        this.manager = new TransferManager(new AmazonS3Client(
//                CredentialProvider.getCredentials(), Config.CONFIG), exec);
//        this.manager.getAmazonS3Client().setRegion(Config.REGION);
    }
//
//    public AmazonS3 getRawClient(){
//        return manager.getAmazonS3Client();
//    }

    /**
     * 把文件在本地硬盘中的相对路径转换为s3上的绝对路径
     *
     * @param relativePath
     * @return
     */
    public String transformToS3Path(String relativePath) {
        assert relativePath != null;

        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }

        return pathPrefix + relativePath;
    }

//    public Upload upload(String s3Path, File file, S3ProgressListener listener){
//        logger.debug("正在上传: {}", s3Path);
//        return manager.upload(new PutObjectRequest(bucket, s3Path, file),
//                listener);
//    }

}