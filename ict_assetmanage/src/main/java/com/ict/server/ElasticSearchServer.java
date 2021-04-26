package com.ict.server;

import cn.hutool.json.JSONUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
@Component
public class ElasticSearchServer {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServer.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     * @param indexName
     * @param numberOfShards
     * @param numberOfReplicas
     * @throws IOException
     */
    public void createIndex(String indexName, int numberOfShards, int numberOfReplicas) throws IOException {
        if (!existsIndex(indexName)) {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // settings部分
            request.settings(Settings.builder()
                    // 创建索引时，分配的主分片的数量
                    .put("index.number_of_shards", numberOfShards)
                    // 创建索引时，为每一个主分片分配的副本分片的数量
                    .put("index.number_of_replicas", numberOfReplicas)
            );
            // mapping部分 除了用json字符串来定义外，还可以使用Map或者XContentBuilder
            request.mapping("{\n" +
                    "  \"properties\": {\n" +
                    "    \"message\": {\n" +
                    "      \"type\": \"text\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}", XContentType.JSON);
            // 创建索引(同步的方式)
            // CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);

            // 创建索引(异步的方式)
            restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
                @Override
                public void onResponse(CreateIndexResponse createIndexResponse) {
                    logger.debug("执行情况:" + createIndexResponse);
                }

                @Override
                public void onFailure(Exception e) {
                    logger.error("执行失败的原因:" + e.getMessage()) ;
                }
            });
        }
    }

    /**
     * 功能描述：createIndex 索引的创建
     * @param
     * @return 返回索引名称
     */
    public String createIndex(String index) throws IOException {
        // 1. 创建索引请求
        CreateIndexRequest firstIndex = new CreateIndexRequest(index);

        // 2. 客户端执行创建索引的请求
        CreateIndexResponse response = restHighLevelClient.indices().create(firstIndex, RequestOptions.DEFAULT);

        return response.index();
    }


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
     * 更新索引的settings配置
     * @param indexName
     * @throws IOException
     */
    public void updateIndexSettings(String indexName) throws IOException {
        UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
        String settingKey = "index.number_of_replicas";
        int settingValue = 2;
        Settings.Builder settingsBuilder = Settings.builder().put(settingKey, settingValue);
        request.settings(settingsBuilder);
        // 是否更新已经存在的settings配置 默认false
        request.setPreserveExisting(true);

        // 更新settings配置(同步)
        //esClient.indices().putSettings(request, RequestOptions.DEFAULT);

        // 更新settings配置(异步)
        restHighLevelClient.indices().putSettingsAsync(request, RequestOptions.DEFAULT, new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                logger.debug("执行情况:" + acknowledgedResponse);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("执行失败的原因:" + e.getMessage()) ;
            }
        });
    }

    /**
     * 更新索引的mapping配置
     * @param indexName
     * @throws IOException
     */
    public void putIndexMapping(String indexName) throws IOException {
        PutMappingRequest request = new PutMappingRequest(indexName);
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("new_parameter");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "ik_max_word");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.source(builder);

        // 新增mapping配置(同步)
        //AcknowledgedResponse putMappingResponse = esClient.indices().putMapping(request, RequestOptions.DEFAULT);
        // 新增mapping配置(异步)
        restHighLevelClient.indices().putMappingAsync(request, RequestOptions.DEFAULT, new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                logger.debug("执行情况:" + acknowledgedResponse);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("执行失败的原因:" + e.getMessage()) ;
            }
        });
    }

    /**
     * 新增Document 使用json字符串
     * @param indexName
     * @throws IOException
     */
    public void addDocument1(String indexName) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2020-03-28\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);

        request.routing("routing");

        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 新增Document 使用map
     * @param indexName
     * @throws IOException
     */
    public void addDocument2(String indexName) throws IOException{
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest(indexName).id("1").source(jsonMap);

        indexRequest.routing("routing");

        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.debug("执行情况: " + indexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("执行失败的原因");
            }
        });
    }

    /**
     * 修改Document
     * @param indexName
     * @throws IOException
     */
    public void updateDocument(String indexName) throws IOException{
        // 传入索引名称和需要更新的Document的id
        UpdateRequest request = new UpdateRequest(indexName, "1");
        // 更新的内容会与数据本身合并， 若存在则更新，不存在则新增
        // 组装更新内容的数据结构有四种: json字符串、Map、XContentBuilder、Key-Value

        // json字符串
//        String jsonString = "{" +
//                "\"updated\":\"2020-03-29\"," +
//                "\"reason\":\"daily update\"" +
//                "}";
//        request.doc(jsonString);

        // Map
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("updated", new Date());
//        jsonMap.put("reason", "daily update");
//        request.doc(jsonMap);

        // XContentBuilder
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        builder.timeField("updated", new Date());
//        builder.timeField("reason", "daily update");
//        builder.endObject();
//        request.doc(builder);

        // Key-Value
        request.doc("updated", new Date(),"reason", "daily update");

        // 同步的方式发送更新请求
        restHighLevelClient.update(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除Document
     * @param indexName
     * @throws IOException
     */
    public void deleteDocument(String indexName) throws IOException{
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
        // 待删除的数据需要满足的条件
        deleteByQueryRequest.setQuery(new TermQueryBuilder("user", "kimchy"));
        // 忽略版本冲突
        deleteByQueryRequest.setConflicts("proceed");

        restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
    }

    /**
     * bulk api批量操作
     * @param indexName
     * @throws IOException
     */
    public void bulkDocument(String indexName) throws IOException{
        BulkRequest request = new BulkRequest();
        // 删除操作
        request.add(new DeleteRequest(indexName, "3"));
        // 更新操作
        request.add(new UpdateRequest(indexName, "2")
                .doc(XContentType.JSON,"other", "test"));
        // 普通的PUT操作，相当于全量替换或新增
        request.add(new IndexRequest(indexName).id("4")
                .source(XContentType.JSON,"field", "baz"));
        restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * bulk api批量操作
     * @param indexName
     * @throws IOException
     */
    public void bulkAddDocument(String indexName) throws IOException{
        BulkRequest request = new BulkRequest();
        // 删除操作
        request.add(new DeleteRequest(indexName, "3"));
        // 更新操作
        request.add(new UpdateRequest(indexName, "2")
                .doc(XContentType.JSON,"other", "test"));
        // 普通的PUT操作，相当于全量替换或新增
        request.add(new IndexRequest(indexName).id("4")
                .source(XContentType.JSON,"field", "baz"));
        restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 搜索描述中包含dubbo的document，并筛选过滤年龄15~40之间的document
     * @param indexNmae
     * @throws IOException
     */
    public void searchDocument(String indexNmae) throws IOException{
        SearchRequest searchRequest = new SearchRequest(indexNmae);

        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();

        // 过滤出年龄在15~40岁之间的docuemnt
        booleanQueryBuilder.filter(QueryBuilders.rangeQuery("age").from(15).to(40));
        // bool must条件， 找出description字段中包含Dubbo的document
        booleanQueryBuilder.must(QueryBuilders.matchQuery("description", "Dubbo"));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(booleanQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        // 同步的方式发送请求
        restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }
}