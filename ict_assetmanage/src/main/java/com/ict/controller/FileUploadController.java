package com.ict.controller;

import com.ict.service.FileOperateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 文件上传
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileOperateUtil fileOperateUtil;


    @PostMapping(value = {"job/fileUpload"},produces="application/json;charset=UTF-8")
    public String FileUpload(MultipartFile[] uploadFiles) throws Exception {
        List<MultipartFile> files = Arrays.asList(uploadFiles);
        fileOperateUtil.uploadFile(files);
        return "上传成功";
    }
}