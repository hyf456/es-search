package com.hivescm.controller;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchUpdateESObject;
import com.hivescm.escenter.common.ConditionUpdateESObject;
import com.hivescm.escenter.common.UpdateESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.controller.ESSearchController;
import com.hivescm.escenter.service.ESSearchService;
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

	private static final String SYSTEM_NAME="TMS";
	private static final String INDEX_NAME = "tms-waybill";
	private static final String TYPE_NAME = "tms-waybill-list";

	@Resource
	private ESSearchController esSearchController;


	@Test // 单一更新操作
	public void updateTest() throws Exception {
		final UpdateESObject updateESObject = generateUpdateESObject(423433099);
		final DataResult<Boolean> dataResult = esSearchController.esUpdate(updateESObject);

		if (dataResult.isSuccess()) {
			System.out.println("------->" + dataResult.getResult());
		} else {
			System.out.println("------->" + dataResult.getStatus());
		}
	}

	@Test // 批量更新
	public void batchUpdateTest() throws Exception {
		BatchUpdateESObject batchUpdateESObject = new BatchUpdateESObject();
		List<UpdateESObject> updateESObjects = new ArrayList<>();
		updateESObjects.add(generateUpdateESObject(519866599));
		updateESObjects.add(generateUpdateESObject(-943268792));
		updateESObjects.add(generateUpdateESObject(-1224490893));
		updateESObjects.add(generateUpdateESObject(-532059376));
		batchUpdateESObject.setUpdateDatas(updateESObjects);
		final DataResult<Boolean> dataResult = esSearchController.esBatchUpdate(batchUpdateESObject);

		if (dataResult.isSuccess()) {
			System.out.println("------->" + dataResult.getResult());
		} else {
			System.out.println("------->" + dataResult.getStatus());
		}

	}

	@Test // 按条件更新
	public void conditionUpdateTest() throws Exception {
		ConditionUpdateESObject conditionUpdateESObject = new ConditionUpdateESObject();
		conditionUpdateESObject.setIndexName(INDEX_NAME);
		conditionUpdateESObject.setTypeName(TYPE_NAME);
		conditionUpdateESObject.setSystemName(SYSTEM_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition1 = new SearchCondition.Builder().setFieldName("list.id")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("1").setMultipleValue(Boolean.TRUE)
				.build();
		SearchCondition searchCondition2 = new SearchCondition.Builder().setFieldName("age")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("23").build();
		searchConditions.add(searchCondition1);
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

		final DataResult<Boolean> dataResult = esSearchController.conditionUpdate(conditionUpdateESObject);

		if (dataResult.isSuccess()) {
			System.out.println("------->" + dataResult.getResult());
		} else {
			System.out.println("------->" + dataResult.getStatus());
		}

	}

	private UpdateESObject generateUpdateESObject(int id) {

		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		updateESObject.setSystemName(SYSTEM_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", 2420156);
		updateESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("updateUserName", "董春福");
		// 数组
		//dataMap.put("hobby", new String[] {  "电影", "音乐" });

		// 集合
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
		//list.add(subMap2);

//		dataMap.put("list", list);
		updateESObject.setDataMap(dataMap);

		return updateESObject;
	}
}
