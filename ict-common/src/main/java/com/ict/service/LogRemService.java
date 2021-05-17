package com.ict.service;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.entity.model.BusiLog;
import com.ict.server.ElasticSearchServer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@Service
public class LogRemService {
    private static final Logger log = LoggerFactory.getLogger(LogRemService.class);

    @Autowired
    private ElasticSearchServer elasticSearchServer;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public String operatorBusiLog(BusiLog busiLog) throws IOException {
//        elasticSearchServer.deleteIndex("busilog");
//        elasticSearchServer.deleteIndex("syslog");
        boolean indexExist = elasticSearchServer.existsIndex("busilog");
        if(!indexExist){
            elasticSearchServer.createIndex("busilog");
        }
        // 1. 创建批量的请求
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("15s");     //  设置超时时间
        ObjectMapper objectMapper = new ObjectMapper();
        BusiLog busiLogRem = objectMapper.convertValue(busiLog, BusiLog.class);
        bulkRequest.add(new IndexRequest("busilog")
                .id(String.valueOf(busiLogRem.getId()))
                .source(JSONUtil.toJsonStr(busiLogRem), XContentType.JSON) );

        // 2. 执行批量创建文档
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 响应 判断是否执行成功
        RestStatus status = responses.status ();
        if(status.getStatus ()!=200){
            log.info("日志存入异常！");
        }
        return "新增成功";
    }
}
