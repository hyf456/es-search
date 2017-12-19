package com.hivescm.escenter.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yaml.snakeyaml.extensions.compactnotation.PackageCompactConstructor;

import com.google.common.collect.Lists;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchDeleteESObject;
import com.hivescm.escenter.common.BatchSaveESObject;
import com.hivescm.escenter.common.BatchUpdateESObject;
import com.hivescm.escenter.common.ConditionDeleteESObject;
import com.hivescm.escenter.common.ConditionUpdateESObject;
import com.hivescm.escenter.common.DeleteESObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.common.UpdateESObject;
import com.hivescm.escenter.common.conditions.FunctionCondition;
import com.hivescm.escenter.common.conditions.GroupByCondition;
import com.hivescm.escenter.common.conditions.OrderCondition;
import com.hivescm.escenter.common.conditions.PageCondition;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.OperateTypeEnum;
import com.hivescm.escenter.common.enums.SortEnum;
import com.hivescm.escenter.common.enums.SqlFunctionEnum;
import com.hivescm.escenter.service.ESSearchService;
import com.hivescm.escenter.service.ESStatisticService;

/**
 * @Author ZHJ
 * @Date 2017/12/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestES {

	private static final String SYSTEM_NAME = "ES";
	private static final String INDEX_NAME = "test_accounts";
	private static final String TYPE_NAME = "person";

	@Autowired
	private ESSearchService esSearchService;

	@Autowired
	private ESStatisticService esStatisticService;

	@Test
	public void test_id_update() {
		UpdateESObject obj = new UpdateESObject("TMS", "tms-global-config", "tms-global-config-list");
		obj.setId(3040026);
		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 3040026);
		dataMap.put("cityId", 110100000000L);
		dataMap.put("iautoCapacity", false);
		dataMap.put("createTime", 1513072397167L);
		obj.setDataMap(dataMap);
		DataResult<Boolean> result = esSearchService.esUpdate(obj);
		System.out.println(result.toJSON());
		Assert.assertTrue(result.getResult());
	}

	@Test
	public void test_condition_update() {
		ConditionUpdateESObject updateESObject = new ConditionUpdateESObject("TMS", "tms-global-config",
				"tms-global-config-list");
		SearchCondition conditions = new SearchCondition();
		conditions.setFieldName("cityId");
		// conditions.setSingleValue("110100000000");
		conditions.setConditionExpression(ConditionExpressionEnum.IN);
		conditions.setFieldValues(new String[] { "110100000000" });
		updateESObject.setConditions(Lists.newArrayList(conditions));
		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 3040026);
		dataMap.put("cityId", 110100000000L);
		dataMap.put("iautoCapacity", false);
		dataMap.put("createTime", 1513072397167L);
		updateESObject.setDataMap(dataMap);
		DataResult<Boolean> result = esSearchService.conditionUpdate(updateESObject);
		System.out.println(result.toJSON());
		Assert.assertTrue(result.getResult());
	}

	@Test
	public void testSave() {
		SaveESObject esObject = newSaveESObject();
		DataResult<Boolean> dataResult = esSearchService.esSave(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testDelete() {
		{
			SaveESObject esObject = newSaveESObject();
			esObject.setId("AWAwJEIvh370ZR06SGpM");
			DataResult<Boolean> dataResult = esSearchService.esSave(esObject);
			Assert.assertTrue(dataResult != null && dataResult.getResult());
		}
		{
			DeleteESObject esObject = newDeleteESObject("AWAwJEIvh370ZR06SGpM");
			DataResult<Boolean> dataResult = esSearchService.esDelete(esObject);
			Assert.assertTrue(dataResult != null && dataResult.getResult());
		}
	}

	@Test
	public void testUpdate() {
		UpdateESObject esObject = newUpdateESObject("1512961965436");
		DataResult<Boolean> dataResult = esSearchService.esUpdate(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testBatchSave() {
		BatchSaveESObject esObject = new BatchSaveESObject();
		List<SaveESObject> list = Lists.newArrayList(newSaveESObject(), newSaveESObject());
		esObject.setSaveDatas(list);

		DataResult<Boolean> dataResult = esSearchService.esBatchSave(esObject);
		System.out.println(dataResult.toJSON());
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testBatchUpdate() {
		BatchUpdateESObject esObject = new BatchUpdateESObject();
		List<UpdateESObject> list = Lists.newArrayList(newUpdateESObject("1512961965436"));
		esObject.setUpdateDatas(list);

		DataResult<Boolean> dataResult = esSearchService.esBatchUpdate(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testBatchDelete() {
		BatchDeleteESObject esObject = new BatchDeleteESObject();
		List<DeleteESObject> list = Lists.newArrayList(newDeleteESObject("AWAwEFM8ZkJGtoP4D5mg"),
				newDeleteESObject("AWAwD9yoh370ZR06SGo0"));
		esObject.setDeleteDatas(list);

		DataResult<Boolean> dataResult = esSearchService.esBatchDelete(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testConditionUpdate() {
		ConditionUpdateESObject esObject = new ConditionUpdateESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// condition
		SearchCondition condition = new SearchCondition("age", ConditionExpressionEnum.LESSER, "50");
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setConditions(list);
		// dataMap
		esObject.setDataMap(Collections.singletonMap("desc", "php开发工程师"));

		DataResult<Boolean> dataResult = esSearchService.conditionUpdate(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testConditionDelete() {
		ConditionDeleteESObject esObject = new ConditionDeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// condition
		SearchCondition condition = new SearchCondition("age", ConditionExpressionEnum.LESSER, "50");
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setConditions(list);

		DataResult<Boolean> dataResult = esSearchService.conditionDelete(esObject);
		Assert.assertTrue(dataResult != null && dataResult.getResult());
	}

	@Test
	public void testQuery_1() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("desc", ConditionExpressionEnum.EQUAL, "Python开发工程师");
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_2() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("desc", ConditionExpressionEnum.LIKE, "工程");
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_3() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("desc", ConditionExpressionEnum.LIKE, "Java");
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_4() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("age", ConditionExpressionEnum.EQUAL, 79);
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_5() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("age", ConditionExpressionEnum.IN, 79, 56);
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_6() {
		QueryESObject esObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// 搜索条件1
		SearchCondition condition = new SearchCondition("desc", ConditionExpressionEnum.NOT_IN,
				new String[] { "Java开发工程师" });
		List<SearchCondition> list = Lists.newArrayList(condition);
		esObject.setSearchConditions(list);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testQuery_7() {
		QueryESObject esObject = new QueryESObject("pay_center", "pay_transaction", "refund_flow");
		// 搜索条件1
		SearchCondition searchCondition = new SearchCondition("orderNo", ConditionExpressionEnum.EQUAL,
				"TRAN_T_1_20171207205135_AZN6");
		esObject.setSearchConditions(Lists.newArrayList(searchCondition));
		// 排序
		OrderCondition orderCondition = new OrderCondition("createTime", SortEnum.DESC);
		esObject.setOrderConditions(Lists.newArrayList(orderCondition));
		// 字段
		esObject.setNeedFields(Lists.newArrayList("id", "orderNo"));
		// 分页
		PageCondition page = new PageCondition();
		page.setPageSize(1000);
		page.setCurrentPage(1078);
		esObject.setPageCondition(page);

		DataResult<ESResponse> result = esSearchService.esQuery(esObject);
		System.out.println("--->>>>>" + result);
	}

	@Test
	public void testStatistic() {
		StatisticESObject esObject = new StatisticESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// functions
		List<FunctionCondition> functionConditions = Lists
				.newArrayList(new FunctionCondition("age", SqlFunctionEnum.SUM, "test-sum"));
		esObject.setFunctionConditions(functionConditions);

		DataResult<Map<String, Number>> result = esStatisticService.statisticByConditions(esObject);
		System.out.println("--->>>>>" + result);
		System.out.println(result.getResult());
	}

	@Test
	public void test_not_in() {
		QueryESObject queryESObject = new QueryESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		List<SearchCondition> conditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setFieldName("age");
		searchCondition.setConditionExpression(ConditionExpressionEnum.NOT_IN);
		searchCondition.setFieldValues(new Object[] { 90, 88, 55 });
		conditions.add(searchCondition);
		queryESObject.setSearchConditions(conditions);
		DataResult<ESResponse> dataResult = esSearchService.esQuery(queryESObject);
		System.out.println(dataResult.toJSON());
	}

	@Test
	public void test_between() {
		QueryESObject queryESObject = new QueryESObject(SYSTEM_NAME, "tms-dispatcher", "tms-dispatcher-list");
		List<SearchCondition> conditions = new ArrayList<>();
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setFieldName("createTime");
		searchCondition.setConditionExpression(ConditionExpressionEnum.BETWEEN);
		searchCondition.setMinValue("1513148665190");
		searchCondition.setMaxValue("1513148665199");
		conditions.add(searchCondition);
		queryESObject.setSearchConditions(conditions);
		DataResult<ESResponse> dataResult = esSearchService.esQuery(queryESObject);
		Assert.assertTrue(dataResult.isSuccess());
		System.out.println(dataResult.getResult().toString());
	}

	@Test
	public void test_tms_api() {
		QueryESObject queryESObject = new QueryESObject(SYSTEM_NAME, "tms-waybill", "tms-waybill-list");
		List<SearchCondition> conditions = new ArrayList<>();
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("createTime");
			searchCondition.setConditionExpression(ConditionExpressionEnum.BETWEEN);
			searchCondition.setMinValue("1513075779030");
			searchCondition.setMaxValue("1513075779033");
			conditions.add(searchCondition);
		}
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("companyId");
			searchCondition.setSingleValue(200);
			searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
			conditions.add(searchCondition);
		}
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("invoiceCompany");
			searchCondition.setFieldValues(new String[] { "可口可乐1" });
			searchCondition.setConditionExpression(ConditionExpressionEnum.NOT_IN);
			conditions.add(searchCondition);
		}
		queryESObject.setSearchConditions(conditions);
		DataResult<ESResponse> dataResult = esSearchService.esQuery(queryESObject);
		System.out.println(dataResult);
		Assert.assertTrue(dataResult.isSuccess());
		System.out.println(dataResult.getResult().toString());
	}

	private SaveESObject newSaveESObject() {
		long now = System.currentTimeMillis();
		int r = RandomUtils.nextInt(1, 100);
		String name = "王五_" + r;

		SaveESObject esObject = new SaveESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// id
		esObject.setId(now);
		// 数据
		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("user", name);
		dataMap.put("userName", name);
		dataMap.put("age", r);
		dataMap.put("desc", "php工程师");
		esObject.setDataMap(dataMap);
		return esObject;
	}

	private UpdateESObject newUpdateESObject(Object id) {
		UpdateESObject esObject = new UpdateESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// id
		esObject.setId(id);
		esObject.setNestedOperateType(OperateTypeEnum.UPDATE);
		// 数据
		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("desc", "c++开发工程师");
		esObject.setDataMap(dataMap);
		return esObject;
	}

	private DeleteESObject newDeleteESObject(Object id) {
		DeleteESObject esObject = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
		// id
		esObject.setId(id);
		return esObject;
	}

	@Test
	public void test_sum() {
		QueryESObject queryESObject = new QueryESObject(SYSTEM_NAME, "tms-waybill_tmp", "tms-waybill-list");
		List<SearchCondition> conditions = new ArrayList<>();
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("status");
			searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
			searchCondition.setSingleValue(1);
			conditions.add(searchCondition);
		}
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("truckOrdered");
			searchCondition.setSingleValue(false);
			searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
			conditions.add(searchCondition);
		}
		{
			SearchCondition searchCondition = new SearchCondition();
			searchCondition.setFieldName("companyId");
			searchCondition.setSingleValue(220);
			searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
			conditions.add(searchCondition);
		}
		queryESObject.setSearchConditions(conditions);
		{
			GroupByCondition groupByCondition = new GroupByCondition();
			List<FunctionCondition>list= Lists.newArrayList();
			{
				FunctionCondition condition = new FunctionCondition();
				condition.setField("totalFee");
				condition.setFunction(SqlFunctionEnum.SUM);
				condition.setFunctionName("totalFee");
				list.add(condition);
			}
			{
				FunctionCondition condition1 = new FunctionCondition();
				condition1.setField("volume");
				condition1.setFunction(SqlFunctionEnum.SUM);
				condition1.setFunctionName("volume");
				list.add(condition1);
			}
			{
				FunctionCondition condition2 = new FunctionCondition();
				condition2.setField("weight");
				condition2.setFunction(SqlFunctionEnum.SUM);
				condition2.setFunctionName("weight");
				list.add(condition2);
			}
			groupByCondition.setFunctionConditions(list);
			groupByCondition.setGroupFields(Lists.newArrayList("weight","totalFee","volume"));
			queryESObject.setGroupByCondition(groupByCondition);
		}
		PageCondition pageCondition=new PageCondition();
		pageCondition.setCurrentPage(1);
		pageCondition.setPageSize(1);
		queryESObject.setPageCondition(pageCondition);
		DataResult<ESResponse> dataResult = esSearchService.esQuery(queryESObject);
		System.out.println(dataResult);
		Assert.assertTrue(dataResult.isSuccess());
		System.out.println(dataResult.toJSON());
	}
}
