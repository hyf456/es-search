package com.hivescm.controller;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BaseESObject;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.common.conditions.*;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.SortEnum;
import com.hivescm.escenter.common.enums.SqlFunctionEnum;
import com.hivescm.escenter.controller.ESStatisticController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESStasticControllerTest {

	private String systemName = "B2B";
	private String indexName = "release-goods-1";
	private String typeName = "goods";

	@Resource
	private ESStatisticController esStatisticController;

	@Test
	public void stasticTest() {
		StatisticESObject statisticESObject = new StatisticESObject();
		statisticESObject.setSystemName(systemName);
		statisticESObject.setIndexName(indexName);

		List<FunctionCondition> functionConditions = new ArrayList<>();

		FunctionCondition sumCondition = new FunctionCondition();
		sumCondition.setFunctionName("test-sum");
		sumCondition.setFunction(SqlFunctionEnum.SUM);
		sumCondition.setField("companyId");
		functionConditions.add(sumCondition);

		FunctionCondition countCondition = new FunctionCondition();
		countCondition.setFunctionName("test-count");
		countCondition.setFunction(SqlFunctionEnum.COUNT);
		countCondition.setField("companyId");
		functionConditions.add(countCondition);

		statisticESObject.setFunctionConditions(functionConditions);

		final DataResult<Map<String, Number>> dataResult = esStatisticController.statisticByConditions(statisticESObject);
		if (dataResult.isFailed()) {
			System.out.println(dataResult.getStatus());
		} else {
			System.out.println(dataResult.getResult());
		}
	}
	@Test
	public void collatorTest() {
		CollapseQueryObject collapseQueryObject = new CollapseQueryObject();
		collapseQueryObject.setSystemName(systemName);
		collapseQueryObject.setIndexName(indexName);
		collapseQueryObject.setTypeName(typeName);

		collapseQueryObject.setFieldName("goodsId");
		collapseQueryObject.setVersion(2);

		PageCondition pageCondition = new PageCondition();
		pageCondition.setPageSize(100);
		pageCondition.setCurrentPage(1);
		collapseQueryObject.setPageCondition(pageCondition);

		// 排序条件
		List<OrderCondition> orderConditions = new ArrayList<>();
		OrderCondition shelvesTimeCondition = new OrderCondition();
		shelvesTimeCondition.setFieldName("shelvesTime");
		shelvesTimeCondition.setOrderCondition(SortEnum.DESC);
		orderConditions.add(shelvesTimeCondition);
		collapseQueryObject.setOrderConditions(orderConditions);

		List<SearchCondition> searchConditions = new ArrayList<>();

		SearchCondition condition1 = new SearchCondition();
		condition1.setFieldName("saletypes.thirdSaletypeName");
		condition1.setConditionExpression(ConditionExpressionEnum.EQUAL);
		condition1.setSingleValue("三级");
		condition1.setMultipleValue(Boolean.TRUE);

		SearchCondition condition1_1 = new SearchCondition();
		condition1_1.setFieldName("goodsName");
		condition1_1.setConditionExpression(ConditionExpressionEnum.EQUAL);
		condition1_1.setSingleValue("商品");
		condition1.setNextAndCondition(condition1_1);
		searchConditions.add(condition1);

		SearchCondition condition2 = new SearchCondition.Builder().setMultipleValue(Boolean.TRUE).setFieldName("saletypes.thirdSaletypeName").setSingleValue("下").setConditionExpression(ConditionExpressionEnum.EQUAL).build();
		SearchCondition condition2_1 = new SearchCondition();
		condition2_1.setFieldName("brandName");
		condition2_1.setConditionExpression(ConditionExpressionEnum.EQUAL);
		condition2_1.setSingleValue("测");
		condition1.setNextAndCondition(condition1_1);
		condition2.setNextAndCondition(condition2_1);

		searchConditions.add(condition2);


		collapseQueryObject.setSearchConditions(searchConditions);

		List<InnerHitsCondition> innerHitsConditions = new ArrayList<>();
		InnerHitsCondition brandNameCondition = new InnerHitsCondition();
		brandNameCondition.setFieldNames(Arrays.asList(new String[] { "brandName","goodsId" }));
		brandNameCondition.setHitName("brandName");
		innerHitsConditions.add(brandNameCondition);

		InnerHitsCondition goodsCategoryNameCondition = new InnerHitsCondition();
		goodsCategoryNameCondition.setFieldNames(Arrays.asList(new String[] { "goodsCategoryName" ,"goodsId"}));
		goodsCategoryNameCondition.setHitName("goodsCategoryName");
		innerHitsConditions.add(goodsCategoryNameCondition);

		InnerHitsCondition thirdSaletypesCondition = new InnerHitsCondition();
		thirdSaletypesCondition.setFieldNames(Arrays.asList(new String[] { "saletypes.thirdSaletypeName","goodsId" }));
		thirdSaletypesCondition.setHitName("thirdSaletypeName");
		innerHitsConditions.add(thirdSaletypesCondition);
		collapseQueryObject.setInnerHitsConditions(innerHitsConditions);

		final DataResult<Map<String, ESResponse>> collapse = esStatisticController.collapse(collapseQueryObject);
		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		if (collapse.isSuccess()) {
			System.out.println(collapse.getResult());
		} else {
			System.out.println(collapse.getStatus());
		}
	}
}
