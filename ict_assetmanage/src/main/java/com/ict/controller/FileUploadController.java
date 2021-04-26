package com.ict.controller;

import com.ict.service.FileOperateUtil;
import com.ict.service.RedisUtil;
import com.ict.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "文件操作相关接口")
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileOperateUtil fileOperateUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "文件上传", httpMethod = "POST")
    @PostMapping(value = {"/job/fileUpload"},produces="application/json;charset=UTF-8")
    public String FileUpload(MultipartFile[] uploadFiles) throws Exception {
        List<MultipartFile> files = Arrays.asList(uploadFiles);
        fileOperateUtil.uploadFile(files);
        return "上传成功";
    }

    @ApiOperation(value = "redis测试", httpMethod = "POST")
    @PostMapping(value = {"/redis"},produces="application/json;charset=UTF-8")
    public String redisTest() throws Exception {
        redisUtil.set("tessss", "这是一个测试");
        // 测试es
        taskService.elasticsearchTest();
        return "上传成功";
    }

    @ApiOperation(value = "es存入数据测试", httpMethod = "POST")
    @PostMapping(value = {"/estest"},produces="application/json;charset=UTF-8")
    public String esTest() throws Exception {
        // 测试es
        taskService.elasticsearchTest();
        return "测试成功";
    }

    @ApiOperation(value = "es查询数据测试", httpMethod = "POST")
    @PostMapping(value = {"/esquerytest"},produces="application/json;charset=UTF-8")
    public String elasticsearchTestQuery() throws Exception {
        // 测试es
        taskService.elasticsearchTestQuery();
        return "测试成功";
    }

}