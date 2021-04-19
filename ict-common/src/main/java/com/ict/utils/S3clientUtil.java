package com.ict.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.ict.config.MinioConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
@Component
public class S3clientUtil {
    private AmazonS3 amazonS3 = MinioConfig.getS3client();

    private static final Logger logger = LoggerFactory.getLogger(S3clientUtil.class);

    /**
     * @Author wsr
     * @Date 2021/1/7 10:36
     * @Desc <p>创建桶<p>
     * @Param bucket_name
     * @Return void
     */
    public void createBucket(String bucket_name) {
        logger.info("minio令牌桶:{}", bucket_name);
        if (!amazonS3.doesBucketExistV2(bucket_name)) {
            try {
                amazonS3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                logger.error("minio令牌桶创建失败", e.getMessage());
            }
        }
    }

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    public String getFileUrl(String fileUrl) throws Exception{

        logger.info("文件下载路径:"+ fileUrl);
        int index = fileUrl.indexOf("/");
        String filePath = fileUrl.substring(index + 1);
        String bucketName = fileUrl.substring(0, index);
//            S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, filePath));
//            if (Objects.isNull(object)) {
//                return null;
//            }
        //设置过期时间 9700小时
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 60 * 60 * 24 * 1000;
        expiration.setTime(expTimeMillis);

        URL url=amazonS3.generatePresignedUrl(bucketName,filePath,expiration);
        logger.info("s3获取文件路径:"+ url);
        if (Objects.nonNull(url)) {
            return url.toString();
        }
        return null;
    }

    /**
     * 单文件上传
     * bucketName   String	    存储桶名称。
     * objectName	String	    指定文件上传到的文件路径。
     * file	        String	    本地文件.
     *
     * @return 返回绝对文件路径
     */

    public String uploadFile(String bucketName, String fileName, InputStream inputStream, String fileType,
                             long size) {
        try {
            //获取系统当前时间并将其转换为string类型,当作文件路径
            String time = IctFileUtil.getDateFormat2(new Date());
            String bucketPath = bucketName + "/" + time;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            objectMetadata.setContentType(fileType);
            amazonS3.putObject(bucketPath, fileName, inputStream, objectMetadata);

            return bucketPath + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件异常", e.getMessage());
        }
        return null;
    }


    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    public byte[] downloadFile(String fileUrl) {
        logger.info("文件下载路径:"+ fileUrl);
        int index = fileUrl.indexOf("/");
        String filePath = fileUrl.substring(index + 1);
        String bucketName = fileUrl.substring(0, index);


        S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, filePath));
        if (object != null) {
            InputStream input = null;
            FileOutputStream fileOutputStream = null;
            try {
                //获取文件流
                input = object.getObjectContent();
                byte[] bytes = IOUtils.toByteArray(input);
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    /**
     * 删除存储桶中的文件对象
     *
     * @param fileUrl String 存储桶里的对象名称
     * @return
     */
    public Boolean deleteFile(String fileUrl) {
        logger.info("文件删除路径:", fileUrl);
        try {
            int index = fileUrl.indexOf("/");
            String filePath = fileUrl.substring(index + 1);
            String bucketName = fileUrl.substring(0, index);
            // 先判断存储桶和文件对象是否存在
            boolean isExist = amazonS3.doesBucketExistV2(bucketName);
            S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, filePath));
            if (isExist && object != null) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, filePath));
                return true;
            }
        } catch (Exception e) {
            logger.error("图片删除失败", e);
        }
        return false;
    }

    /**
     * 删除存储桶中的文件对象
     *
     * @param fileUrl String 存储桶里的对象名称
     * @return
     */
    public Boolean deleteFileFromS3(String fileUrl) {
        logger.info("文件删除路径:"+fileUrl);
        try {
            int index = fileUrl.indexOf("/");
            String filePath = fileUrl.substring(index + 1);
            String bucketName = fileUrl.substring(0, index);
            amazonS3.deleteObject(bucketName, filePath);
            return true;
        } catch (Exception e) {
            logger.error("图片删除失败"+fileUrl);
        }
        return false;
    }
}