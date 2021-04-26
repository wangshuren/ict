package com.ict.service;

import cn.hutool.json.JSONUtil;
import com.ict.dao.IctDictDao;
import com.ict.entity.IctDict;
import com.ict.server.ElasticSearchServer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private IctDictDao ictDictDao;

    private static String TESTINDEX = "test_index";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticSearchServer elasticSearchServer;

    public void elasticsearchTest() throws IOException {
        // 查询所有数据
        List<IctDict> dictList = ictDictDao.selectDicts();
        List<String> codeList = new ArrayList<>();
        for(IctDict bd : dictList){
            codeList.add(bd.getDictCode());
        }

//        elasticSearchServer.deleteIndex(TESTINDEX);
        boolean indexExist = elasticSearchServer.existsIndex(TESTINDEX);
        if(!indexExist){
            elasticSearchServer.createIndex(TESTINDEX);
        }
        // 创建批量的请求
        BulkRequest bulkRequest = new BulkRequest();
        // 设置超时时间
        bulkRequest.timeout("10s");
        for(IctDict bd : dictList){
            bulkRequest.add(new IndexRequest(TESTINDEX)
                    .id(String.valueOf(bd.getId()))
                    .source(JSONUtil.toJsonStr(bd), XContentType.JSON) );
        }
        // 执行批量创建文档
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 判断是否执行成功
        RestStatus status = responses.status ();
        if(status.getStatus ()!=200){
            logger.error("存入标签异常");
        }
    }

    public void elasticsearchTestQuery() throws IOException {
        // 1. 创建批量搜索请求，并绑定索引
        SearchRequest searchRequest = new SearchRequest(TESTINDEX);

        // 2. 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery("SheBei","dictCode"));
        sourceBuilder.from(0);
        sourceBuilder.size(9999);

        // 3. 将查询条件放入搜索请求request中
        searchRequest.source(sourceBuilder);
        // 4. 发起查询请求获取数据
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        response.getHits ();
        SearchHit[] hits = response.getHits ().getHits ();

        for (SearchHit hit : hits){
            String jsonstr = JSONUtil.toJsonStr(hit.getSourceAsMap ());
            IctDict bd = JSONUtil.toBean(jsonstr,IctDict.class);

            System.out.println(bd.getDictName());
        }

        searchRequest = new SearchRequest(TESTINDEX);
        WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("dictCode", "*Zuo*");//搜索名字中含有Zuo的文档
        sourceBuilder.query(queryBuilder1);
        searchRequest.source(sourceBuilder);
        response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        response.getHits ();
        hits = response.getHits ().getHits ();
        for (SearchHit hit : hits){
            String jsonstr = JSONUtil.toJsonStr(hit.getSourceAsMap ());
            IctDict bd = JSONUtil.toBean(jsonstr,IctDict.class);

            System.out.println(bd.getDictName());
        }

        // 模糊查询 should(相当于SQL中的or关键字)
//        WildcardQueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery("interest", "*read*");//搜索interest中含有read的文档
//        BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();//name中含有jack或者interest含有read，相当于or
//        boolQueryBuilder.should(queryBuilder1);
//        boolQueryBuilder.should(queryBuilder2);

        //name中必须含有jack,interest中必须含有read,相当于and
//        boolQueryBuilder.must(queryBuilder1);
//        boolQueryBuilder.must(queryBuilder2);
    }
}