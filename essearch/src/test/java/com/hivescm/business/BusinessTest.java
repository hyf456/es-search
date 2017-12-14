package com.hivescm.business;

import com.hivescm.Application;
import com.hivescm.escenter.common.ConditionUpdateESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.controller.ESSearchController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class BusinessTest {
	private static final String SYSTEM_NAME = "TMS";
	private static final String INDEX_NAME = "tms-distribution";
	private static final String TYPE_NAME = "tms-distribution-list";

	@Autowired
	private ESSearchController esSearchController;

	@Test
	public void businessTest() {
		ConditionUpdateESObject conditionUpdateESObject = new ConditionUpdateESObject();
		conditionUpdateESObject.setIndexName(INDEX_NAME);
		conditionUpdateESObject.setTypeName(TYPE_NAME);
		conditionUpdateESObject.setSystemName(SYSTEM_NAME);

		List<SearchCondition> conditions = new ArrayList<>();
		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("dispatcherId")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("60068").build();
		conditions.add(sc1);

		conditionUpdateESObject.setConditions(conditions);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("status", 3);
		conditionUpdateESObject.setDataMap(dataMap);

		esSearchController.conditionUpdate(conditionUpdateESObject);
	}
}
