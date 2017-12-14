package com.hivescm.service.nested;

import com.google.gson.Gson;
import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.*;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.OperateTypeEnum;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

/**
 * Created by DongChunfu on 2017/8/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class OOLTest {

	private static final Gson GSON = new Gson();
	private static final String SYSTEM_NAME = "escenter";
	private static final String INDEX_NAME = "test_ool";
	private static final String TYPE_NAME = "ool";

	@Autowired
	private ESNestedSearchService esSearchService;

	@Test // 新增OOL
	public void saveTest() throws IOException {
		esSearchService.save(generateSaveESObject());
	}

	@Test // 删除(测试通过)
	public void deleteTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", 2043865209);
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setFieldName("student");

		NestedESObject nextNestedESObject = new NestedESObject();
		nextNestedESObject.setFieldName("books");
		nestedESObject.setNextNestedESObject(nextNestedESObject);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 1);
		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.DELETE);
		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}

	@Test // 新增
	public void addTagTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", 2);
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setFieldName("student");
		nestedESObject.setList(Boolean.FALSE);

		NestedESObject nextNestedESObject = new NestedESObject();
		nextNestedESObject.setFieldName("books");
		nestedESObject.setNextNestedESObject(nextNestedESObject);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("student.books.id", 4);
		dataMap.put("student.books.name", "redis");
		dataMap.put("student.books.size", 2017);

		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.ADD);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}

	@Test // 更新 测试通过
	public void updateTagTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "733432282");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setFieldName("student");

		NestedESObject nextNestedESObject = new NestedESObject();
		nextNestedESObject.setFieldName("books");
		nestedESObject.setNextNestedESObject(nextNestedESObject);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 2);
		dataMap.put("name", "冰封王座");
		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.UPDATE);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}


	@Test
	public void queryTest() throws IOException {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("id")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("1635640515").build();
		searchConditions.add(sc1);

		queryOne.setSearchConditions(searchConditions);
		String[] needFields = new String[]{"student.books.id","student.books.name"};
		queryOne.setNeedFields(Arrays.asList(needFields));

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

	private SaveESObject generateSaveESObject() {
		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setSystemName(SYSTEM_NAME);
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		Random r = new Random();
		final int id = Math.abs(r.nextInt());
		ukMap.put("id", id);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", id);
		dataMap.put("logol", "thinkpad");

		Map<Object, Object> student = new HashMap<>();
		student.put("id", 1);
		student.put("name", "dongchunfu");

		List<Map<Object, Object>> books = new ArrayList<>();
		Map<Object, Object> book1 = new HashMap<>();
		book1.put("id", 1);
		book1.put("name", "西游记");
		books.add(book1);

		Map<Object, Object> book2 = new HashMap<>();
		book2.put("id", 2);
		book2.put("name", "水浒传");
		books.add(book2);

		student.put("books", books);

		dataMap.put("student", student);

		saveESObject.setDataMap(dataMap);

		return saveESObject;
	}
}
