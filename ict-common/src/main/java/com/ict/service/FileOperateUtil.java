package com.ict.service;

import com.ict.entity.model.vo.ResultFileVO;
import com.ict.utils.CoreFileUtil;
import com.ict.utils.IctFileUtil;
import com.ict.utils.S3clientUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
@Component
public class FileOperateUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileOperateUtil.class);


    @Value("${minio.bucketName}")
    private String bucketName;
    @Autowired
    private CoreFileUtil coreUtil;

    @Value("${minio.task.bucketName}")
    private String taskBucketName;

    @Autowired
    private S3clientUtil s3clientUtil;

    public String getTaskBucketName() {
        return taskBucketName;
    }

    public void setTaskBucketName(String taskBucketName) {
        this.taskBucketName = taskBucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ResultFileVO> uploadFile(List<MultipartFile> files) {
        List<ResultFileVO> list = new ArrayList<>();

        //判断令牌桶是否存在
        try {
            s3clientUtil.createBucket(bucketName);
            s3clientUtil.createBucket(taskBucketName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        logger.info("----进入上传-----------------");
        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String newName = UUID.randomUUID().toString().replaceAll("-", "") + fileType;
            ResultFileVO vo = new ResultFileVO(fileName);
            File filetominio = null;
            InputStream fileInputStream = null;
            try {
                Long time1 = System.currentTimeMillis();
                //执行上传图片操作
                String file_id = s3clientUtil.uploadFile(bucketName, newName, file.getInputStream(),
                        file.getContentType(), file.getSize());
                Long time2 = System.currentTimeMillis();
                logger.info("上传源文件耗时：" + (time2 - time1));

                //执行缩率图生成
                String thumbanName = UUID.randomUUID().toString().replaceAll("-", "") + fileType;
                filetominio = new File(thumbanName);
                if (!filetominio.exists()) {
                    filetominio.createNewFile();
                }
                Thumbnails.of(file.getInputStream()).size(coreUtil.getImgSize150(), coreUtil.getImgSize150()).toFile(filetominio);
                Long time3 = System.currentTimeMillis();
                logger.info("生成缩率图耗时：" + (time3 - time2));

                fileInputStream = new FileInputStream(filetominio);
                //执行上传缩率图操作
                String ext_file_id = s3clientUtil.uploadFile(bucketName, thumbanName,
                        fileInputStream, file.getContentType(), filetominio.length());
                logger.info("上传缩率图耗时：" + (System.currentTimeMillis() - time3));

                //如果 file_id 为 null 说明文件上传失败，返回错误信息
                if (file_id == null || ext_file_id == null) {
                    vo.setStatus(IctFileUtil.FILE_UPLOAD_DOWNLOAD_STATUS_ERROR);
                } else {
                    vo.setFileId(file_id);
                    vo.setExtFileId(ext_file_id);
                    vo.setStatus(IctFileUtil.FILE_UPLOAD_DOWNLOAD_STATUS_SUCCESS);
                }
            } catch (Exception e) {
                vo.setStatus(IctFileUtil.FILE_UPLOAD_DOWNLOAD_STATUS_ERROR);
                logger.error("文件 [" + file.getOriginalFilename() + "] 上传失败", e);
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filetominio.delete();
            }
            list.add(vo);

        }
        return list;
    }

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    public byte[] downloadFile(String fileUrl) {
        return s3clientUtil.downloadFile(fileUrl);
    }

    /**
     * 删除存储桶中的文件对象
     *
     * @param fileUrl String 存储桶里的对象名称
     * @return
     */
    public Boolean deleteFile(String fileUrl) {
        return s3clientUtil.deleteFile(fileUrl);
    }
}
