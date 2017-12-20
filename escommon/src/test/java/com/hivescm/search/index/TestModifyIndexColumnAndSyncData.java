package com.hivescm.search.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.escenter.core.config.IndexUtils;

/**
 * 更新TMS索引数据结构
 * 
 * @author SHOUSHEN LUAN
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestModifyIndexColumnAndSyncData {
	/**
	 * 测试更新字段类型（重建索引+会删除所有数据）
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	@Test
	public void update_column() {
		try {
			String index = "my_index_v1", type = "employee";
			String tmp_index = index + "_tmp";
			Client client = TestEnvConfig.getTestEnvEsClient();
			MappingMetaData metaData = IndexUtils.loadIndexMeta(client, index, type);
			Map<String, Object> data = metaData.getSourceAsMap();
			Map<String, Object> properties = (Map<String, Object>) data.get("properties");
			{
				Map<String, Object> weight = (Map<String, Object>) properties.get("age");
				weight.put("type", "integer");
			}
			IndexUtils.deleteIndex(client, tmp_index);
			Assert.assertTrue(IndexUtils.createIndex(client, tmp_index, type, data));
			/* 将数据拷贝到临时索引 */
			copy_data(index, tmp_index, type, client);
			/* 删除主索引 */
			IndexUtils.deleteIndex(client, index);
			/* 重建主索引 */
			Assert.assertTrue(IndexUtils.createIndex(client, index, type, data));
			/* 从临时索引中拷贝到主索引中 */
			copy_data(tmp_index, index, type, client);
			/* 删除临时索引 */
			IndexUtils.deleteIndex(client, tmp_index);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("失败了");
		}
	}

	/**
	 * 同一个ES服务中拷贝数据
	 * 
	 * @param srcIndex 原索引
	 * @param dectIndex 目标索引
	 * @param type 索引类型
	 * @param client
	 */
	private void copy_data(String srcIndex, String dectIndex, String type, Client client) {
		ScanDocuments scanDocuments = new ScanDocuments(srcIndex, type, 10);
		scanDocuments.start(client, "id", SortOrder.ASC);
		int count = 0;
		while (scanDocuments.hasNext()) {
			SearchHit hit = scanDocuments.next();
			IndexResponse response = client.prepareIndex(dectIndex, type).setId(hit.getId()).setSource(hit.getSource()).get();
			System.out.println(count + "--" + response.getId() + "->" + response.getResult());
			count++;
		}
	}

	@Test
	public void test() {
		Client client = TestEnvConfig.getTestEnvEsClient();
		for (int i = 0; i < 100; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put("id", i);
			data.put("age", i * 10);
			String[] names = { "张三", "李四", "王洛", "王五", "李思思", "王老五" };
			data.put("name", names[RandomUtils.nextInt(0, names.length)]);
			String descriptions[] = { "中国人民银行", "中国银行", "美国信托基金", "计算机", "Go 语言", "Java 语言" };
			data.put("description", descriptions[RandomUtils.nextInt(0, descriptions.length)]);
			IndexResponse response = client.prepareIndex("my_index_v1", "employee").setId(String.valueOf(i)).setSource(data)
					.get();
			System.out.println(response.getResult());
		}
		for (int i = 100; i < 150; i++) {
			Map<String, Object> data = new HashMap<>();
			data.put("id", i);
			data.put("age", i * 10);
			data.put("name", "张三丰-003");
			data.put("description", "XTD2017013");
			IndexResponse response = client.prepareIndex("my_index_v1", "employee")//
					.setId(String.valueOf(i)).setSource(data).get();
			System.out.println(response.getResult());
		}
	}
}
