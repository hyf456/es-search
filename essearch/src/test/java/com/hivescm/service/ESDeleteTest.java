package com.hivescm.service;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchDeleteESObject;
import com.hivescm.escenter.common.ConditionDeleteESObject;
import com.hivescm.escenter.common.DeleteESObject;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
@Configuration
@ComponentScan(basePackages = { "com.hivescm" })
public class ESDeleteTest {

	private static final String SYSTEM_NAME = "escenter";
	private static final String INDEX_NAME = "escenter";
	private static final String TYPE_NAME = "test";
	private DeleteESObject deleteESObject = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);

	@Autowired
	private ESNestedSearchService esSearchService;

	@Test // 单一删除
	public void deleteTest() throws Exception {
		Map<Object, Object> objectObjectMap = new HashMap<>();
		objectObjectMap.put("id", 157224545);
		deleteESObject.setUkMap(objectObjectMap);
		final DataResult<Boolean> delete = esSearchService.delete(deleteESObject);
		System.out.println(delete);

		SearchRequestBuilder searchRequestBuilder = null;
		QueryESObject queryESObject = null;
	}

	@Test // 批量删除
	public void batchDeleteTest() {
		BatchDeleteESObject batchDeleteESObject = new BatchDeleteESObject();
		List<DeleteESObject> deleteESObjects = new ArrayList<>();

		DeleteESObject deleteESObject1 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		Map<Object, Object> objectObjectMap1 = new HashMap<>();
		objectObjectMap1.put("id", 2085151053);
		deleteESObject1.setUkMap(objectObjectMap1);
		deleteESObjects.add(deleteESObject1);

		DeleteESObject deleteESObject2 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		Map<Object, Object> objectObjectMap2 = new HashMap<>();
		objectObjectMap2.put("id", -668283549);
		deleteESObject2.setUkMap(objectObjectMap2);
		deleteESObjects.add(deleteESObject2);

		batchDeleteESObject.setDeleteDatas(deleteESObjects);
		esSearchService.batchDelete(batchDeleteESObject);
	}

	@Test // 根据条件删除
	public void conditionDeleteTest() throws Exception {
		ConditionDeleteESObject conditionDeleteESObject = new ConditionDeleteESObject();
		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("list.id")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("3").setMultipleValue(Boolean.TRUE)
				.build();
		searchConditions.add(searchCondition);
		conditionDeleteESObject.setConditions(searchConditions);

		conditionDeleteESObject.setIndexName(INDEX_NAME);
		conditionDeleteESObject.setTypeName(TYPE_NAME);

		final DataResult<Boolean> dataResult = esSearchService.conditionDelete(conditionDeleteESObject);
		System.out.println(dataResult);
	}
}
