//package com.hivescm.escenter.core.service;
//
//import com.hivescm.escenter.common.ESResponse;
//import org.elasticsearch.action.index.IndexRequestBuilder;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.client.Client;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Created by DongChunfu on 2017/8/9
// * <p>
// * ES 父子数据类型 CRUD 操作
// */
//public class ESRelationSearchService {
//
//	@Autowired
//	private Client client;
//
//	public ESResponse query() {
//
//		final IndexRequestBuilder indexRequestBuilder = client.prepareIndex();
//		final SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
//
//		final UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate();
//
//
//		return null;
//	}
//}
