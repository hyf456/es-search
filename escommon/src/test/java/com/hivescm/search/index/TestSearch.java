package com.hivescm.search.index;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.escenter.core.config.IndexAdmin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestSearch {
	@Autowired
	private TransportClient esClient;
	@Autowired
	private IndexAdmin indexAdmin;

	@Test
	public void test_dump_es_index() throws UnknownHostException, InterruptedException, ExecutionException {
		indexAdmin.dump_es_index();
	}

	@Test
	public void test1() {
		SearchResponse response = esClient.prepareSearch("tms-order").setTypes("tms-order-list")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.wildcardQuery("orderCode.keyword", "*L2017*"))).get();
		System.out.println(response.getHits().totalHits);
	}

	@Test
	public void test() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put("age", i * 10);
			String[] names = { "张三", "李四", "王洛", "王五", "李思思", "王老五" };
			data.put("name", names[RandomUtils.nextInt(0, names.length)]);
			String descriptions[] = { "中国人民银行", "中国银行", "美国信托基金", "计算机", "Go 语言", "Java 语言" };
			data.put("description", descriptions[RandomUtils.nextInt(0, descriptions.length)]);
			IndexResponse response = esClient.prepareIndex("my_index_v1", "employee").setId(String.valueOf(i)).setSource(data)
					.get();
			System.out.println(response.getResult());
		}
		for (int i = 11; i < 12; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put("age", i * 10);
			data.put("name", "张三丰-003");
			data.put("description", "XTD2017013");
			IndexResponse response = esClient.prepareIndex("my_index_v1", "employee")//
					.setId(String.valueOf(i)).setSource(data).get();
			System.out.println(response.getResult());
		}
	}

	@Test
	public void test_save_and_get() {
		{
			Map<String, Object> data = new HashMap<>();
			data.put("age", 201);
			data.put("name", "赵本山2");
			data.put("description", "测试");
			IndexResponse response = esClient.prepareIndex("my_index_v1", "employee")
					.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL).setId("20").setSource(data).get();
			System.out.println(response.getResult());
		}
		{
			SearchResponse response = esClient.prepareSearch("my_index_v1").setTypes("employee")
					.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("name", "赵本山2"))).get();
			SearchHits hits = response.getHits();
			System.out.println(hits.getTotalHits());
			for (int i = 0; i < hits.totalHits; i++) {
				System.out.println(hits.getHits()[i].getSourceAsString());
			}
		}
	}

	@Test
	public void test_search() {
		SearchResponse response = esClient.prepareSearch("my_index_v1").setTypes("employee")
				.setQuery(QueryBuilders.boolQuery().must(
						// QueryBuilders.termQuery("description","D2017013"))
						// QueryBuilders.termQuery("age",20))
						QueryBuilders.wildcardQuery("description.keyword", "*TD20*"))
				// QueryBuilders.boolQuery()
				// .must(QueryBuilders.termQuery("description","中国"))
				// .must()
				).get();
		SearchHits hits = response.getHits();
		System.out.println(hits.getTotalHits());
		for (int i = 0; i < hits.totalHits; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}
	}

	@Test
	public void test_sync_index_data() {
		String new_index = "my_index_v2";
		test_del_index(new_index);
		MappingMetaData metaData = indexAdmin.loadIndexMeta("my_index_v1", "employee");
		try {
			String type = metaData.type();
			Map<String, Object> data = metaData.getSourceAsMap();
			Map<String, Object> properties = (Map<String, Object>) data.get("properties");
			Map<String, Object> ageStruct = (Map<String, Object>) properties.get("age");
			ageStruct.put("type", "keyword");
			CreateIndexResponse createIndexResponse = esClient.admin().indices().prepareCreate(new_index)
					.addMapping(type, data).get();
			Assert.assertTrue(createIndexResponse.isAcknowledged());
			System.out.println(createIndexResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("创建索引失败");
		}

		ScanDocuments scanDocuments = new ScanDocuments("my_index_v1", "employee", 3);
		scanDocuments.start(esClient);
		while (scanDocuments.hasNext()) {
			SearchHit hit = scanDocuments.next();
			System.out.println(hit.getId() + "-" + hit.getSourceAsMap());
			IndexResponse response = esClient.prepareIndex(new_index, "employee").setId(hit.getId())
					.setSource(hit.getSource()).get();
			System.out.println(response.getResult());
		}
	}

	private void test_del_index(String index) {
		try {
			indexAdmin.deleteIndex(index);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("删除索引出错");
		}
	}
}
