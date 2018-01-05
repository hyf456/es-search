package com.hivescm.search.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForOffsetDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.search.utils.ModifyIndexFactory;
import com.hivescm.search.utils.ModifyIndexFactory.UpdateProperties;

/**
 * 更新TMS索引数据结构
 * 
 * @author SHOUSHEN LUAN
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestModifyIndex {
	@Autowired
	private ModifyIndexFactory modifyIndexFactory;

	/**
	 * 测试更新字段类型（重建索引+会删除所有数据）
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	@Test
	public void update_field_() {
		try {
			String index = "release-goods-3", type = "goods";
			boolean success = modifyIndexFactory.reindex(index, type, new UpdateProperties() {
				@Override
				public Map<String, Object> adjustField(Map<String, Object> properties) {
					for (Map.Entry<String, Object> entry : properties.entrySet()) {
						String name = entry.getKey();
						Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
						if (valueMap.get("type").equals("text")) {
							properties.put(name,
									IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setFieldKeyword().getResult());
						}
					}
					String json = GsonSerialize.INSTANCE.encode(properties);
					System.out.println(json);
					return GsonSerialize.INSTANCE.decode(json, Map.class);
				}
			}, "shelvesTime", SortOrder.ASC);
			Assert.assertTrue(success);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("失败了");
		}
	}

	@Autowired
	private Client client;

	@Test
	public void test() throws InterruptedException, ExecutionException {
		// IndexUtils.deleteIndex(client, "my_index_v1");
		// IndexUtils.createIndex(client, "my_index_v1");
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
