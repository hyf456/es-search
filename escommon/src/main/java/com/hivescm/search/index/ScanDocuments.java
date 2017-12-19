package com.hivescm.search.index;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class ScanDocuments implements Iterator<SearchHit> {
	private String index;
	private String type;
	private int size;
	private SearchHits hits;
	private AtomicInteger next_pos = new AtomicInteger(0);
	private int tmp_index = 0;
	private TransportClient esClient;
	long totalHits = 0;

	public ScanDocuments(String index, String type, int size) {
		this.index = index;
		this.type = type;
		this.size = size;

	}

	@Override
	public boolean hasNext() {
		if (hits.getHits().length > tmp_index) {
			return true;
		}
		SearchResponse response = esClient.prepareSearch(index).setTypes(type).setFrom(next_pos.get()).setSize(size).get();
		hits = response.getHits();
		next_pos.getAndAdd(size);
		tmp_index = 0;
		return hits.getHits().length > 0;
	}

	@Override
	public SearchHit next() {
		if (hits.getHits().length > tmp_index) {
			try {
				return hits.getAt(tmp_index);
			} finally {
				tmp_index++;
			}
		} else {
			return null;
		}
	}

	public void start(TransportClient esClient) {
		this.esClient = esClient;
		// SearchResponse response =
		// esClient.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.matchAllQuery())
		// .setSearchType(SearchType.DEFAULT).setScroll(timeValue).setSize(size).get();
		SearchResponse response = esClient.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.matchAllQuery())
				.setFrom(next_pos.get()).setSize(size).get();
		this.hits = response.getHits();
		System.out.println(totalHits = hits.totalHits);
		next_pos.getAndAdd(size);
		// this.scrollId = response.getScrollId();
	}

}
