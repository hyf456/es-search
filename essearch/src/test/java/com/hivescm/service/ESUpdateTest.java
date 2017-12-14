package com.hivescm.service;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchUpdateESObject;
import com.hivescm.escenter.common.ConditionUpdateESObject;
import com.hivescm.escenter.common.UpdateESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESUpdateTest {

	private static final String INDEX_NAME = "escenter";
	private static final String TYPE_NAME = "test";

	@Resource
	private ESNestedSearchService esSearchService;

	@Test // 单一更新操作
	public void updateTest() throws Exception {
		final UpdateESObject updateESObject = generateUpdateESObject(120107);
		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update.getResult());
	}

	@Test // 批量更新
	public void batchUpdateTest() throws Exception {
		BatchUpdateESObject batchUpdateESObject = new BatchUpdateESObject();
		List<UpdateESObject> updateESObjects = new ArrayList<>();
		updateESObjects.add(generateUpdateESObject(1488723343));
		updateESObjects.add(generateUpdateESObject(-439660627));
		updateESObjects.add(generateUpdateESObject(2034334349));
		updateESObjects.add(generateUpdateESObject(1887885183));
		batchUpdateESObject.setUpdateDatas(updateESObjects);
		final DataResult<Boolean> dataResult = esSearchService.batchUpdate(batchUpdateESObject);
		System.out.println("------->" + dataResult);
	}

	@Test // 按条件更新
	public void conditionUpdateTest() throws Exception {
		ConditionUpdateESObject conditionUpdateESObject = new ConditionUpdateESObject();
		conditionUpdateESObject.setIndexName(INDEX_NAME);
		conditionUpdateESObject.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();
		//		SearchCondition searchCondition1 = new SearchCondition.Builder().setFieldName("list.id")
		//				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("13").setMultipleValue(Boolean
		// .TRUE)
		//				.build();
		SearchCondition searchCondition2 = new SearchCondition.Builder().setFieldName("age")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("23").build();
		//		searchConditions.add(searchCondition1);
		searchConditions.add(searchCondition2);
		conditionUpdateESObject.setConditions(searchConditions);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("age", 35);
		dataMap.put("name", "董春雨");
		// 数组
		dataMap.put("hobby", new String[] { "电影", "音乐" });

		// 集合
		List<Map<Object, Object>> list = new ArrayList<>();
		Map<Object, Object> subMap1 = new HashMap<>();
		subMap1.put("id", 23);
		subMap1.put("name", "威海");
		subMap1.put("date", "2016-06-06");
		list.add(subMap1);
		dataMap.put("list", list);

		conditionUpdateESObject.setDataMap(dataMap);

		final DataResult<Boolean> dataResult = esSearchService.conditionUpdate(conditionUpdateESObject);
		System.out.println("------->" + dataResult);
	}

	private UpdateESObject generateUpdateESObject(int id) {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", id);
		updateESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("specialRequire", "董春福");
		//
		//		// 集合
		//		List<Map<Object, Object>> list = new ArrayList<>();
		//		Map<Object, Object> subMap1 = new HashMap<>();
		//		subMap1.put("id", 23);
		//		subMap1.put("name", "威海");
		//		subMap1.put("date", "2016-06-06");
		//		list.add(subMap1);
		//		Map<Object, Object> subMap2 = new HashMap<>();
		//		subMap2.put("id", 2);
		//		subMap2.put("name", "贵阳");
		//		subMap2.put("date", "2012-07-01");
		//		//list.add(subMap2);
		//
		//		dataMap.put("list", list);
		updateESObject.setDataMap(dataMap);

		return updateESObject;
	}
}
