package com.ict.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
@Configuration
@EnableAutoConfiguration
public class CoreFileUtil {
    /**
     * 自定义配置属性
     * 缩率图尺寸 150x150
     */
    @Value("${ai_ict.img-size-150}")
    private int imgSize150;

    public int getImgSize150() {
        return imgSize150;
    }

    public void setImgSize150(int imgSize150) {
        this.imgSize150 = imgSize150;
    }
}
