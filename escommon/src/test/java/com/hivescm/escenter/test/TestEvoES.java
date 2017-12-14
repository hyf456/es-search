package com.hivescm.escenter.test;

import com.hivescm.escenter.evo.ESClient;
import com.hivescm.escenter.evo.ESQueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author ZHJ
 * @Date 2017/12/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class TestEvoES {

    private static final String SYSTEM_NAME = "ES";
    private static final String INDEX_NAME = "test_accounts";
    private static final String TYPE_NAME = "person";

    @Autowired
    private ESClient esClient;

    @Test
    public void testQuery_0() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .match("userName", "张三")
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_1() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .match("userName", "张三")
                .lt("age", 76)
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_2() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .and(
                        ESQueryBuilder.match("userName", "张三"),
                        ESQueryBuilder.lt("age", 76))
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_3() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .or(
                        ESQueryBuilder.match("userName", "李四"),
                        ESQueryBuilder.lt("age", 40))
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_4() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .and(
                        ESQueryBuilder.match("userName", "张三"))
                .or(
                        ESQueryBuilder.match("user", "张三_49"),
                        ESQueryBuilder.match("user", "张三_96"))
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_5() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .or(
                        ESQueryBuilder.match("user", "张三_49"),
                        ESQueryBuilder.match("user", "张三_96"))
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_6() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .and(
                        ESQueryBuilder.match("userName", "张三"))
                .not(
                        ESQueryBuilder.match("user", "张三_49"))
                .get();
        System.out.println("--->>>>>" + response);
    }

    @Test
    public void testQuery_7() {
        SearchResponse response = esClient.prepareSearch(INDEX_NAME, TYPE_NAME)
                .or(
                        ESQueryBuilder.match("user", "张三_49"),
                        ESQueryBuilder.match("user", "张三_96"),
                        ESQueryBuilder.lt("age",50))
                .get();
        System.out.println("--->>>>>" + response);
    }


//        SearchResponse response = esClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
//         .matchQuery('name','')
//        name='zhangsan' and (age=1 or sex=1) and a in []

//        SearchResponse response = esClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
//                .setQuery(QueryBuilders.boolQuery()
//                        .must(QueryBuilders.matchQuery("userName", "张三"))
//                        .must(QueryBuilders.matchQuery("desc", "数据库"))
//                        .must(QueryBuilders.boolQuery()
//                                .should(QueryBuilders.rangeQuery("age").lt(60))
//                                .should(QueryBuilders.matchQuery("title", "xxx"))))


//                        .must( QueryBuilders.rangeQuery("age").lt(60))
//                        .must(QueryBuilders.matchQuery("title","xxx")))
//                .addSort("age", SortOrder.DESC).setFrom(0).setSize(1).get();
//        System.out.println("--->>>>>" + response);


    // client.prepareSearch(INDEX_NAME,TYPE_NAME)
    //      .query(
    //          and("userName", QueryType.match, "张三")
    //          and("desc", QueryType.match, "数据库")
    //          .or(1,2).or("age",20).sort(age,desc).page(1,10).get();

//    }
}
