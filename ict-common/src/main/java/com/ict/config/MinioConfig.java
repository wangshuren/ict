package com.ict.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @Description: minio配置
 * @Author wangsr
 * @Date 2021/4/19
 * @Version 1.0
 */
@Component
public class MinioConfig {
    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);


    public static AmazonS3 getS3client() {

        try {
            Properties props = new Properties();
            //获取jar包当前的目录
            String absolutePath = new File("").getAbsolutePath();
            String filePath = absolutePath + System.getProperty("file.separator") + "config" + System.getProperty(
                    "file.separator") + "bootstrap.properties";
            logger.info("-----------获取配置文件路径----------" + filePath + "---------------------");
            File file = new File(filePath);
            // 输入流
            InputStream in;
            if (file.exists()) {
                in = new FileInputStream(file);
                props.load(in);
                in.close();
            } else {
                ClassPathResource resource = new ClassPathResource("bootstrap.properties");
                in=resource.getInputStream();
                //解决中文乱码
                BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                if (in != null) {
                    props.load(bf);
                }
                bf.close();
            }
            in.close();
            //也可以将值转换为Map
            Map minioMap = new HashMap();
            minioMap.put("url",props.getProperty("minio.url"));
            minioMap.put("accessKey",props.getProperty("minio.accessKey"));
            minioMap.put("secretKey",props.getProperty("minio.secretKey"));
            logger.info("-----------获取配置文信息----------" + minioMap.toString() + "---------------------");

            //logger.info("----minio配置-----------------"+minioMap.toString());
            if (Objects.isNull(minioMap)) {
                throw new RuntimeException("minio配置文件为空");
            }
            if (Objects.isNull(minioMap.get("url"))) {
                throw new RuntimeException("minio配置文件为空");
            }

            if (Objects.isNull(minioMap.get("accessKey"))) {
                throw new RuntimeException("minio配置文件为空");
            }
            if (Objects.isNull(minioMap.get("secretKey"))) {
                throw new RuntimeException("minio配置文件为空");
            }
            String minioUrl = minioMap.get("url").toString();
            String accessKey = minioMap.get("accessKey").toString();
            String secretKey = minioMap.get("secretKey").toString();


            AmazonS3 s3client = null;
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            ClientConfiguration cc = new ClientConfiguration();
            cc.setMaxConnections(50);
            //add by lianyd  先写死换成awsv2 签名
            cc.setSignerOverride("S3SignerType");
            s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(cc)
                    // 设置要用于请求的端点配置（服务端点和签名区域）// 我的s3服务器
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioUrl, "cn-north-1"))
                    // 是否使用路径方式，是的话s3.xxx.sn/bucketname
                    .withPathStyleAccessEnabled(true)
                    .build();
            return s3client;
        } catch (IOException e) {
            logger.info("----minio配置异常-----------------");
            e.printStackTrace();
        }
        return null;
    }
}