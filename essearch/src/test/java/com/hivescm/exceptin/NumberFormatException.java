package com.hivescm.exceptin;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.ESDocument;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by DongChunfu on 2017/8/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class NumberFormatException {
	private static final String INDEX_NAME = "escenter";
	private static final String TYPE_NAME = "test_nfe";

	@Resource
	private ESNestedSearchService esSearchService;
	@Test // 保存，自动映射为long类型
	public void saveTest(){
		esSearchService.save(generateSaveESObject());
	}

	@Test // 保存null值
	public void saveNullTest() {
		esSearchService.save(generateNullESObject());
	}

	@Test // 查询 long 的 null值
	public void queryNullTest() {
		QueryESObject queryESObject = new QueryESObject();
		queryESObject.setIndexName(INDEX_NAME);
		queryESObject.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("name")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("hushenghua").build();
		searchConditions.add(searchCondition);

		queryESObject.setSearchConditions(searchConditions);

		queryESObject.setNeedFields(Arrays.asList(new String[]{"age"}));

		final DataResult<ESResponse> query = esSearchService.query(queryESObject);

		System.out.println("------->");
		final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
		for (ESDocument esDocument : esDocuments) {
			System.out.println("------->" + esDocument);
		}

		QueryBuilders.boolQuery().should(QueryBuilders.termQuery("fieldName","fieldValue")).should(QueryBuilders.termQuery("fieldName","fieldValue"));

	}

	private SaveESObject generateNullESObject() {
		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", 5);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
//		dataMap.put("age", null);
//		dataMap.put("age", null);
		dataMap.put("name", "hushenghua");
		saveESObject.setDataMap(dataMap);
		return saveESObject;
	}
	private SaveESObject generateSaveESObject() {
		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", 5);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("age", 1);
		dataMap.put("name", "dongchunfu");
		saveESObject.setDataMap(dataMap);
		return saveESObject;
	}

	/***************上述场景复现失败***********/
	/**
	 * 1:验证了es在存储时，对于没有给定key-value的键值对是不进行存储的；
	 * 2：即使在自动映射的时候，是long类型，仍然可以给null作为值，返回时会为null；
	 */

	/***************使用TMS 数据复现 NumberFormatException**********/
 //serialVersionUID, id, companyId, companyName, dispatcherId, batchCode, waybillId, driverId, cost, driverName, vehicleId, vehicleName, fleetId, fleetName, billType, billTypeName, receiptAddress, invoiceAddress, status, dotId, dotName, volume, weight, totalSingular, createUser, createUserName, createTime, updateUser, updateUserName, updateTime], pageCondition=PageCondition


	@Test // 查询 long 的 null值
	public void queryTmsTest() {
		QueryESObject queryESObject = new QueryESObject();
		queryESObject.setSystemName("TMS");
		queryESObject.setIndexName("tms-distribution");
		queryESObject.setTypeName("tms-distribution-list");

		List<SearchCondition> searchConditions = new ArrayList<>();

//		final SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("name")
//				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("hushenghua").build();
//		searchConditions.add(searchCondition);

		queryESObject.setSearchConditions(searchConditions);

		queryESObject.setNeedFields(Arrays.asList(new String[]{"serialVersionUID","id", "companyId", "companyName", "dispatcherId", "batchCode","waybillId", "driverId", "cost", "driverName", "vehicleId", "vehicleName", "fleetId", "fleetName", "billType", "billTypeName", "receiptAddress", "invoiceAddress", "status", "dotId", "dotName", "volume", "weight", "totalSingular", "createUser", "createUserName", "createTime", "updateUser", "updateUserName", "updateTime"}));


		final DataResult<ESResponse> query = esSearchService.query(queryESObject);
		System.out.println("------->");
		final List<ESDocument> esDocuments = query.getResult().getEsDocuments();
		for (ESDocument esDocument : esDocuments) {
			System.out.println("------->" + esDocument);
		}

		QueryBuilders.boolQuery().should(QueryBuilders.termQuery("fieldName","fieldValue")).should(QueryBuilders.termQuery("fieldName","fieldValue"));

	}
}
