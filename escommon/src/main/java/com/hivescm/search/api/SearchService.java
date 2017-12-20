package com.hivescm.search.api;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

@Component
public class SearchService {
	@Resource
	private Client client;

	/**
	 * 统一索引中数据总数量
	 * 
	 * @param index
	 * @param type
	 * @return
	 */
	public long countAll(String index, String type) {
		SearchResponse response = client.prepareSearch(index).setTypes(type).setSize(1).get();
		return response.getHits().totalHits;
	}
}
