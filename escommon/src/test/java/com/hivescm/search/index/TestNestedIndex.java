package com.hivescm.search.index;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.escenter.core.config.IndexUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestNestedIndex {
	@Autowired
	private Client client;

	/**
	 * 创建嵌套索引
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void test_create_index() throws InterruptedException, ExecutionException {
		IndexStruct struct = IndexStruct.make("a", "b");
		struct.addColumn("id", IndexField.make().setType(DataType.LONG));
		struct.addColumn("name", IndexField.make().setType(DataType.KEYWORD));
		struct.addColumn("desc", IndexField.make().setType(DataType.TEXT).setFieldKeyword());
		{
			Map<String, Object> properties = new HashMap<>();
			properties.put("name", IndexField.make().setType(DataType.TEXT).getResult());
			properties.put("time", IndexField.make().setType(DataType.DATE).getResult());
			properties.put("stars", IndexField.make().setType(DataType.INTEGER).getResult());
			struct.addColumn("comments", IndexField.make().setType(DataType.NESTED).put("properties", properties));
		}
		IndexUtils.deleteIndex(client, "a");
		Map<String, Object> map = GsonSerialize.INSTANCE.decode(struct.getPropertiesJson(), Map.class);
		boolean res = IndexUtils.createIndex(client, "a", "b", map);
		Assert.assertTrue(res);
	}

	/**
	 * 添加嵌套数据
	 */
	@Test
	public void test_nested() {
		for (int i = 0; i < 100; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put("id", i);
			String[] names = { "张三", "李四", "王洛", "王五", "李思思", "王老五" };
			data.put("name", names[RandomUtils.nextInt(0, names.length)]);
			String descriptions[] = { "中国人民银行", "中国银行", "美国信托基金", "计算机", "Go 语言", "Java 语言" };
			data.put("desc", descriptions[RandomUtils.nextInt(0, descriptions.length)]);
			{
				Map<String, Object> comments = new HashMap<>();
				String[] _names = { "秋天的风雨", "在路上", "人在路途", "相识一场", "人在囧途" };
				comments.put("name", _names[RandomUtils.nextInt(0, _names.length)]);
				comments.put("time", new Date());
				comments.put("stars", new Random().nextInt(100));
				data.put("comments", comments);
			}
			IndexResponse response = client.prepareIndex("a", "b").setId(String.valueOf(i)).setSource(data).get();
			System.out.println(response.getResult());
		}
	}

	/**
	 * 嵌套查询
	 */
	@Test
	public void test_nested_query() {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder
				.should(QueryBuilders.nestedQuery("comments", QueryBuilders.termQuery("comments.stars", "17"), ScoreMode.Max));
		queryBuilder
				.should(QueryBuilders.nestedQuery("comments", QueryBuilders.matchQuery("comments.name", "在路上"), ScoreMode.Max));
		SearchRequestBuilder builder = client.prepareSearch("a").setTypes("b").setQuery(queryBuilder);
		System.out.println(builder.toString());
		SearchResponse response = builder.get();
		Assert.assertTrue(response.getHits().getTotalHits() > 0);
		for (SearchHit hit : response.getHits()) {
			System.out.println(hit.getId() + "  " + hit.getSource());
		}
	}

}
