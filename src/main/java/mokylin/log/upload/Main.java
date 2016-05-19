//package mokylin.log.upload;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.sql.SQLException;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.RejectedExecutionHandler;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import com.google.common.base.Charsets;
//import com.google.common.io.ByteStreams;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.base.Stopwatch;
//import com.mokylin.log.index.IndexRebuilder;
//import com.mokylin.log.upload.reader.MultiLocalFileStatusReader;
//
///**
// * 本地有个文件列表
// * @author Timmy
// *
// */
//public class Main{
//    private static final Logger logger = LoggerFactory.getLogger(Main.class);
//
//    private static final int threadCount = Runtime.getRuntime()
//            .availableProcessors();
//
//    private static final ExecutorService exec = new ThreadPoolExecutor(
//            threadCount, threadCount, 60L, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<Runnable>(threadCount * 3),
//            new RejectedExecutionHandler(){
//
//                @Override
//                public void rejectedExecution(Runnable r,
//                                              ThreadPoolExecutor executor){
//                    try{
//                        // block until there's room
//                        executor.getQueue().put(r);
//                    } catch (InterruptedException e){
//                        throw new RejectedExecutionException(
//                                "Unexpected InterruptedException", e);
//                    }
//                }
//            });
//
//    private static final ScheduledExecutorService scheduled = Executors
//            .newScheduledThreadPool(2);
//
//    public static void main(String[] args) throws IOException, SQLException{
///*        if (args.length != 0){
//            switch (args[0]){
//                case "rebuild":
//                    IndexRebuilder.main(args);
//                    return;
//
//                case "compact":{
//                    MultiLocalFileStatusReader localStatusReader = new MultiLocalFileStatusReader(
//                            false);
//
//                    localStatusReader.compact();
//                    return;
//                }
//
//                case "version":{
//                    try (InputStream in = ClassLoader
//                            .getSystemResourceAsStream("version.txt")){
//                        if (in == null){
//                            logger.error("找不到版本文件！");
//                            throw new IllegalStateException();
//                        }
//
//                        byte[] content = ByteStreams.toByteArray(in);
//                        if (content.length == 0){
//                            logger.error("版本文件内容为空！");
//                            throw new IllegalStateException();
//                        }
//                        String version = new String(content, Charsets.UTF_8)
//                                .trim();
//
//                        System.out.println(version);
//
//                    } catch (Exception e){
//                        logger.error("读版本文件出错！");
//                        throw new IllegalStateException(e);
//                    }
//                    return;
//                }
//                default:
//                    System.err.println("unknown param: " + args[0]);
//                    return;
//            }
//        }*/
////        System.setProperty("org.apache.commons.logging.Log",
////                "org.apache.commons.logging.impl.NoOpLog"); // 禁用亚马逊logger
//        // 主线程直接开始
//
//        OssWriter oss = new OssWriter();
////        SqsWriter sqs = new SqsWriter();
////        final MultiLocalFileStatusReader localStatusReader = new MultiLocalFileStatusReader(
////                false);
//
////        AwsServices aws = new AwsServices(s3, sqs, localStatusReader);
////        MultiFileContainerUploader uploader = new MultiFileContainerUploader(
////                aws);
//
////        SurfixWhiteList whiteList = new SurfixWhiteList();
//
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run(){
//                logger.info("Shutting down");
//
//                scheduled.shutdown();
//                exec.shutdown();
//
//                try{
//                    scheduled.awaitTermination(10, TimeUnit.MINUTES);
//                } catch (InterruptedException e){
//                    logger.error("scheduled线程超时未关闭成功", e);
//                }
//                try{
//                    exec.awaitTermination(10, TimeUnit.MINUTES);
//                } catch (InterruptedException e){
//                    logger.error("exec线程超时未关闭成功", e);
//                }
//
//                logger.info("Shutdown complete");
//            }
//        });
//
//        scheduled.scheduleWithFixedDelay(new Runnable(){
//            @Override
//            public void run(){
//                try{
//                    localStatusReader.commit();
//                } catch (Throwable ex){
//                    logger.error(
//                            "Main.main(...).new Runnable() {...}.run file status commit出错",
//                            ex);
//                }
//            }
//        }, 10, 10, TimeUnit.MINUTES);
//
//        RoundRunner runner = new RoundRunner(aws, whiteList, uploader);
//
//        if (args.length != 0 && args[0].equals("once")){
//            runner.run();
//        } else {
//            scheduled.scheduleAtFixedRate(runner, 0, 3, TimeUnit.MINUTES);
//        }
//    }
//
//    private static class RoundRunner implements Runnable{
//        private final AwsServices aws;
//        private final SurfixWhiteList whiteList;
//        private final MultiFileContainerUploader uploader;
//        private final File currentFolder;
//        private final String currentFolderPath;
//
//        private RoundRunner(AwsServices aws, SurfixWhiteList whiteList,
//                            MultiFileContainerUploader uploader) throws IOException{
//            this.aws = aws;
//            this.whiteList = whiteList;
//            this.uploader = uploader;
//
//            String folderPath = System.getenv(Config.ENV_KEY_DATA_FOLDER);
//            if (folderPath == null){
//                folderPath = ".";
//            }
//
//            this.currentFolder = new File(folderPath);
//            this.currentFolderPath = currentFolder.getCanonicalPath();
//            logger.debug("数据路径: {}", currentFolderPath);
//        }
//
//        @Override
//        public void run(){
//            processFolder(currentFolder.toPath());
//        }
//
//        private void processFolder(Path folder){
//            try{
//                Stopwatch watch = Stopwatch.createStarted();
//                try (DirectoryStream<Path> ds = Files
//                        .newDirectoryStream(folder)){
//                    for (Path p : ds){
//                        try{
//                            processFile(p);
//                        } catch (Throwable a){
//
//                        }
//                    }
//                }
//                logger.info("round finished in {} minutes",
//                        watch.stop().elapsed(TimeUnit.MINUTES));
//            } catch (Throwable ex){
//                logger.error("processFolder出错", ex);
//            }
//        }
//
//        private void processFile(Path p) throws IOException{
//            File f = p.toFile();
//            String name = f.getName();
//
//            if (name.startsWith(".")){
//                // 隐藏文件不处理, 可能是rsync了一半的临时文件
//                return;
//            }
//
//            if (f.isDirectory()){
//                processFolder(p);
//                return;
//            }
//
//            if (!whiteList.isSurfixWhiteList(getSurfix(name))){
//                return;
//            }
//
//            String fileName = f.getCanonicalPath();
//
//            String relativeFileName = fileName
//                    .replaceFirst(currentFolderPath, "");
//
//            String s3Name = aws.getS3().transformToS3Path(relativeFileName);
//
//            if (aws.getLocalStatusReader().isFileSuccess(s3Name)){
//                return;
//            }
//
//            exec.execute(new SingleFileRunner(f, s3Name, aws, uploader));
//        }
//    }
//
//    private static String getSurfix(String file){
//        int dotIndex = file.lastIndexOf(".");
//        if (dotIndex == -1){
//            return "";
//        }
//
//        return file.substring(dotIndex + 1, file.length());
//    }
//}