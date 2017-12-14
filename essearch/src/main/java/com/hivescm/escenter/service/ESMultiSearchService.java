package com.hivescm.escenter.service;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by DongChunfu on 2017/8/14
 * <p>
 * 多索引，多类型查询
 * <p>
 * 具有相同域信息的索引或类型，因语言不同，导致分析处理不同
 */
public class ESMultiSearchService {

	@Autowired
	private Client client;

	public void multiSearch() {
		final MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
				.multiMatchQuery("query_text", "fieldName_1", "fieldName_2");

		final MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();

		SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);
		searchRequestBuilder.setIndices("");
		searchRequestBuilder.setTypes("");

		IndicesOptions indicesOptions = null;

		final SearchResponse searchResponse = searchRequestBuilder.get();
		multiSearchRequestBuilder.setIndicesOptions(indicesOptions);

	}
}
