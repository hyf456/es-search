package com.hivescm.service;

import com.google.gson.Gson;
import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.ESDocument;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.FunctionCondition;
import com.hivescm.escenter.common.conditions.GroupByCondition;
import com.hivescm.escenter.common.conditions.PageCondition;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.SqlFunctionEnum;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESQueryTest {
	private static final Gson GSON = new Gson();
	private static final String INDEX_NAME = "tms-waybill-stock";
	private static final String TYPE_NAME = "tms-waybill-stock-list";

	@Autowired
	private ESNestedSearchService esSearchService;

	@Test
	public void objectDatatypeTest() throws IOException {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("waybillCode")
				.setConditionExpression(ConditionExpressionEnum.LIKE).setSingleValue("132442").build();

		searchConditions.add(sc1);

		queryOne.setSearchConditions(searchConditions);

		final DataResult<ESResponse> query = esSearchService.query(queryOne);

		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
		if (null != esDocuments) {
			for (ESDocument esDocument : esDocuments) {
				System.out.println("------>" + esDocument);
			}
		}
		System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}

	@Test
	public void groupTest() throws IOException {
		QueryESObject esObject = new QueryESObject();


		esObject.setIndexName(INDEX_NAME);
		esObject.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("age").setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("23").build();
		searchConditions.add(searchCondition);
		esObject.setSearchConditions(searchConditions);

		PageCondition pageCondition = new PageCondition();
		pageCondition.setCurrentPage(1);
		pageCondition.setPageSize(10);
		esObject.setPageCondition(pageCondition);


		GroupByCondition groupByCondition = new GroupByCondition();
		groupByCondition.setGroupFields(Arrays.asList(new String[]{"age"}));

		FunctionCondition functionCondition = new FunctionCondition();
		functionCondition.setField("age");
		functionCondition.setFunctionName("maxprice");
		functionCondition.setFunction(SqlFunctionEnum.MAX);

//		FunctionCondition functionCondition2 = new FunctionCondition();
//		functionCondition2.setField("name");
//		functionCondition2.setFunctionName("minprice");
//		functionCondition2.setFunction(SqlFunctionEnum.MIN);

		/*FunctionCondition functionCondition3 = new FunctionCondition();
		functionCondition3.setField("price");
		functionCondition3.setFunctionName("sumprice");
		functionCondition3.setFunction(SqlFunctionEnum.SUM);

		FunctionCondition functionCondition4 = new FunctionCondition();
		functionCondition4.setField("price");
		functionCondition4.setFunctionName("count");
		functionCondition4.setFunction(SqlFunctionEnum.COUNT);*/

		groupByCondition.setFunctionConditions(Arrays.asList(new FunctionCondition[]{functionCondition}));

		esObject.setGroupByCondition(groupByCondition);
		esObject.setGroupAndNeedSource(true);
		final DataResult<ESResponse> query = esSearchService.query(esObject);

		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
		if (null != esDocuments) {
			for (ESDocument esDocument : esDocuments) {
				System.out.println("------>" + esDocument);
			}
		}

		System.out.println(GSON.toJson(query));
		System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}
}
