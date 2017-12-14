//package com.hivescm.escenter.core.service;
//
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.TermsQueryBuilder;
//import org.elasticsearch.search.aggregations.Aggregation;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
///**
// * Created by DongChunfu on 2017/8/9
// * <p>
// * 通用ESObject CRUD 操作
// */
//@Service(value = "fullTextSearchService")
//public class FullTextSearchService {
//	private static final Logger LOGGER = LoggerFactory.getLogger(FullTextSearchService.class);
//
//	@Autowired
//	private Client client;
//
//	public void groupAnalizier() {
//		final QueryBuilder queryBuilder = QueryBuilders.boolQuery();
//
//		final TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("", "");
//
//		final SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
//
//
//
//
//
//	}
//
//}
