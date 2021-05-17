package com.ict.server;

import cn.hutool.json.JSONUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/5/17 0017 17:07
 */
@Component
public class ElasticSearchServer {
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 功能描述：deleteIndex 删除指定的索引
     *
     * @param
     * @return boolean
     */
    public boolean deleteIndex(String index) throws IOException {
        // 1. 创建一个delete请求，用于删除索引信息
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);

        // 2. 客户端执行删除请求
        AcknowledgedResponse result = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        boolean flag = (boolean) JSONUtil.toBean(JSONUtil.toJsonStr(result), HashMap.class).get("acknowledged");

        return flag;
    }

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String index) throws IOException {
        // 1. 创建一个get请求获取指定索引的信息
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);

        // 2. 客户端执行请求判断索引是否存在
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        return exists;
    }


    /**
     * 功能描述：createIndex 索引的创建
     * @param
     * @return 返回索引名称
     */
    public String createIndex(String index) throws IOException {
        // 1. 创建索引请求
        CreateIndexRequest firstIndex = new CreateIndexRequest(index);
        //配置映射
        String mapping = "";
        mapping = "{\n" +
                "\t\"mappings\": {\n" +
                "\t\t\"properties\": {\n" +
                "\t\t\t\"id\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"opUserId\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"opUserName\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"opUserRealName\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"busiName\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"busiOp\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"status\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"createTimeStr\": {\n" +
                "\t\t\t\t\"type\": \"keyword\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"createTime\": {\n" +
                "\t\t\t\t\"type\": \"date\"\n" +
//                    "\t\t\t\t\"format\":\"yyyy-MM-dd HH:mm:ss\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        firstIndex.source(mapping, XContentType.JSON);
        // 2. 客户端执行创建索引的请求
        CreateIndexResponse response = null;
        try {
            response = restHighLevelClient.indices().create(firstIndex, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.index();
    }
}
