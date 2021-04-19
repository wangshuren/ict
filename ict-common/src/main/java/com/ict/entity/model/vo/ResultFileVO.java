package com.ict.entity.model.vo;

import java.util.List;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
public class ResultFileVO {
    /*文件名*/
    private String fileName;
    /*文件存储路径*/
    private String fileId;
    /*图片缩率图路径*/
    private String extFileId;
    /*文件上传状态 0：成功，1：失败*/
    private int status;

    private int width;

    private int height;

    private long fileSize;

    //标签json字符串
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public ResultFileVO() {
    }

    public ResultFileVO(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExtFileId() {
        return extFileId;
    }

    public void setExtFileId(String extFileId) {
        this.extFileId = extFileId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}