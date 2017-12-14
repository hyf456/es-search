package com.hivescm.controller;

import com.google.gson.Gson;
import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.ESDocument;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.*;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.SortEnum;
import com.hivescm.escenter.common.enums.SqlFunctionEnum;
import com.hivescm.escenter.controller.ESSearchController;
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

	private static final String SYSTEM_NAME = "TMS";
	private static final String INDEX_NAME = "tms-distribution";
	private static final String TYPE_NAME = "tms-distribution-list";

	@Autowired
	private ESSearchController esSearchController;

	@Test
	public void objectDatatypeTest() throws IOException {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);
		queryOne.setSystemName(SYSTEM_NAME);
		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition sc4 = new SearchCondition.Builder().setFieldName("lineName")
				.setConditionExpression(ConditionExpressionEnum.LIKE).setSingleValue("未").build();

		final SearchCondition sc3 = new SearchCondition.Builder().setFieldName("id")
				.setConditionExpression(ConditionExpressionEnum.IN).setFeldValues(new String[] { "120131", "120133" }).build();

		searchConditions.add(sc3);

		queryOne.setNeedFields(Arrays.asList(new String[] { "id", "companyName", "destName" }));

		queryOne.setSearchConditions(searchConditions);

		final DataResult<ESResponse> query = esSearchController.esQuery(queryOne);

		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		if (query.isSuccess()) {
			final ESResponse result = query.getResult();
			if (result != null) {
				final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
				if (null != esDocuments) {
					for (ESDocument esDocument : esDocuments) {
						System.out.println("------>" + esDocument);
					}
				}
			}
		} else {
			System.out.println(query.getStatus());
		}
		System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}

	@Test
	public void groupTest() throws IOException {
		QueryESObject esObject = new QueryESObject();

		esObject.setIndexName("cars");
		esObject.setTypeName("transactions");

		List<SearchCondition> searchConditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("price")
				.setConditionExpression(ConditionExpressionEnum.GREATER).setSingleValue("10").build();
		searchConditions.add(searchCondition);
		esObject.setSearchConditions(searchConditions);

		PageCondition pageCondition = new PageCondition();
		pageCondition.setCurrentPage(1);
		pageCondition.setPageSize(2);
		esObject.setPageCondition(pageCondition);

		GroupByCondition groupByCondition = new GroupByCondition();
		groupByCondition.setGroupFields(Arrays.asList(new String[] { "color", "make" }));

		FunctionCondition functionCondition = new FunctionCondition();
		functionCondition.setField("price");
		functionCondition.setFunctionName("maxprice");
		functionCondition.setFunction(SqlFunctionEnum.MAX);

		FunctionCondition functionCondition2 = new FunctionCondition();
		functionCondition2.setField("price");
		functionCondition2.setFunctionName("minprice");
		functionCondition2.setFunction(SqlFunctionEnum.MIN);

		FunctionCondition functionCondition3 = new FunctionCondition();
		functionCondition3.setField("price");
		functionCondition3.setFunctionName("sumprice");
		functionCondition3.setFunction(SqlFunctionEnum.SUM);

		FunctionCondition functionCondition4 = new FunctionCondition();
		functionCondition4.setField("price");
		functionCondition4.setFunctionName("count");
		functionCondition4.setFunction(SqlFunctionEnum.COUNT);

		groupByCondition.setFunctionConditions(Arrays.asList(
				new FunctionCondition[] { functionCondition, functionCondition2, functionCondition3, functionCondition4 }));

		esObject.setGroupByCondition(groupByCondition);
		esObject.setGroupAndNeedSource(true);
		final DataResult<ESResponse> query = esSearchController.esQuery(esObject);

		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		if (query.isSuccess()) {
			final ESResponse result = query.getResult();
			if (result != null) {
				final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
				if (null != esDocuments) {
					for (ESDocument esDocument : esDocuments) {
						System.out.println("------>" + esDocument);
					}
				}
			}
		} else {
			System.out.println(query.getStatus());
		}
		System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}

	@Test
	public void pageQueryTest() {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);
		queryOne.setSystemName(SYSTEM_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("companyId")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("2").build();

		final SearchCondition sc2 = new SearchCondition.Builder().setFieldName("driverId")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("1").build();

		searchConditions.add(sc1);
		searchConditions.add(sc2);
		queryOne.setSearchConditions(searchConditions);

		queryOne.setNeedFields(Arrays.asList(new String[] { "waybillId", "companyId", "companyName" }));

		PageCondition pageCondition = new PageCondition();
		pageCondition.setCurrentPage(1);
		pageCondition.setPageSize(1);
		queryOne.setPageCondition(pageCondition);

		List<OrderCondition> orderConditions = new ArrayList<>();
		OrderCondition orderCondition = new OrderCondition();
		orderCondition.setFieldName("id");
		orderCondition.setOrderCondition(SortEnum.DESC);
		orderConditions.add(orderCondition);

		queryOne.setOrderConditions(orderConditions);
		final DataResult<ESResponse> query = esSearchController.esQuery(queryOne);

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
	public void conditionErrorTest() {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);
		queryOne.setSystemName(SYSTEM_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();
		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("companyId")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("dcf").build();
		searchConditions.add(sc1);
		queryOne.setSearchConditions(searchConditions);

		final DataResult<ESResponse> query = esSearchController.esQuery(queryOne);
		System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		if (query.isSuccess()) {
			final ESResponse result = query.getResult();
			if (result != null) {
				final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
				if (null != esDocuments) {
					for (ESDocument esDocument : esDocuments) {
						System.out.println("------>" + esDocument);
					}
				}
			}
		} else {
			System.out.println(query.getStatus());
		}
		System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}
}
