package com.hivescm.escenter.test;

import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZHJ
 * @Date 2017/12/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class TestNativeES {

    private static final String SYSTEM_NAME = "ES";
    private static final String INDEX_NAME = "test_accounts";
    private static final String TYPE_NAME = "person";

    @Autowired
    private TransportClient esClient;

    @Test
    public void testSave() {
        long now = System.currentTimeMillis();
        int r = RandomUtils.nextInt(1, 100);
        String name = "张三_" + r;

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", name);
        dataMap.put("userName", name);
        dataMap.put("age", r);
        dataMap.put("desc", "Python开发工程师");

        IndexResponse response = esClient.prepareIndex(INDEX_NAME, TYPE_NAME)
                .setId(String.valueOf(now))
                .setSource(dataMap).get();
        Assert.assertTrue(response != null && DocWriteResponse.Result.CREATED == response.getResult());
    }

    @Test
    public void testQuery() {
//        SearchResponse response = esClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
//         .matchQuery('name','')
//        name='zhangsan' and (age=1 or sex=1) and a in []

        SearchResponse response = esClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("userName", "张三"))
                        .must(QueryBuilders.matchQuery("desc", "数据库"))
                        .must(QueryBuilders.boolQuery()
                                .should(QueryBuilders.rangeQuery("age").lt(60))
                                .should(QueryBuilders.matchQuery("title", "xxx"))))


//                        .must( QueryBuilders.rangeQuery("age").lt(60))
//                        .must(QueryBuilders.matchQuery("title","xxx")))
                .addSort("age", SortOrder.DESC).setFrom(0).setSize(1).get();
        System.out.println("--->>>>>" + response);


        // client.prepareSearch(INDEX_NAME,TYPE_NAME)
        //      .query(
        //          and("userName", QueryType.match, "张三")
        //          and("desc", QueryType.match, "数据库")
        //          .or(1,2).or("age",20).sort(age,desc).page(1,10).get();

    }

//    @Test
//    public void testDeleteByQuery() {
//        String index = "test_accounts";
//        String type = "person";
//        BulkRequestBuilder bulkRequest = esClient.prepareBulk();
//        SearchResponse response = esClient.prepareSearch(index).setTypes(type)
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.matchAllQuery())
//                .setFrom(0).setSize(1000).get();
//        for (SearchHit hit : response.getHits()) {
//            String id = hit.getId();
//            System.out.println(id);
//            bulkRequest.add(esClient.prepareDelete(index, type, id).request());
//        }
//        BulkResponse bulkResponse = bulkRequest.get();
//        if (bulkResponse.hasFailures()) {
//            for (BulkItemResponse item : bulkResponse.getItems()) {
//                System.out.println(item.getFailureMessage());
//            }
//        } else {
//            System.out.println("delete ok");
//        }
//    }
}
