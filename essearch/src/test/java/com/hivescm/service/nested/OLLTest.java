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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class OLLTest {
	private static final Gson JSON = new Gson();

	private static final String SYSTEM_NAME = "escenter";
	private static final String INDEX_NAME = "test_oll";
	private static final String TYPE_NAME = "oll";

	@Resource
	private ESNestedSearchService esSearchService;

	@Test // 保存
	public void saveTest() throws IOException {
		esSearchService.save(generateSaveESObject());
	}

	@Test // 删除
	public void deleteTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "1738268428");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setIdValues(new String[] { "1" });
		nestedESObject.setFieldName("students");
		nestedESObject.setList(Boolean.TRUE);

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

	@Test // 更新
	public void updateTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "1738268428");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setIdValues(new String[] { "1" });
		nestedESObject.setFieldName("students");
		nestedESObject.setList(Boolean.TRUE);

		NestedESObject nextNestedESObject = new NestedESObject();
		nextNestedESObject.setFieldName("books");
		nestedESObject.setNextNestedESObject(nextNestedESObject);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 5);
		dataMap.put("name", "三国杀");
		dataMap.put("size", 2019);
		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.UPDATE);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}

	@Test // 新增
	public void addTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);
		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "-1407333374");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setIdValues(new String[] { "1" });
		nestedESObject.setFieldName("students");
		nestedESObject.setList(Boolean.TRUE);

		NestedESObject nextNestedESObject = new NestedESObject();
		nextNestedESObject.setFieldName("books");
		nestedESObject.setNextNestedESObject(nextNestedESObject);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 78);
		dataMap.put("name", "groovy in action");
		dataMap.put("size", 2017);
		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.ADD);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}

	@Test
	public void queryTest() {
		QueryESObject queryOne = new QueryESObject();
		queryOne.setIndexName(INDEX_NAME);
		queryOne.setTypeName(TYPE_NAME);

		List<SearchCondition> searchConditions = new ArrayList<>();

		final SearchCondition sc1 = new SearchCondition.Builder().setFieldName("logol")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("thinkpad").build();

		final SearchCondition sc2 = new SearchCondition.Builder().setFieldName("students.books.id")
				.setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("1").setMultipleValue(Boolean.TRUE).build();

		searchConditions.add(sc1);
		searchConditions.add(sc2);
		queryOne.setSearchConditions(searchConditions);

		queryOne.setNeedFields(Arrays.asList(new String[] { "students.books.name", "students.books.id","logol"}));

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
		final int id = r.nextInt();
		ukMap.put("id", id);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("logol", "thinkpad");
		dataMap.put("id", id);

		Map<Object, Object> student1 = new HashMap<>();
		student1.put("id", 1);
		student1.put("name", "dongchunfu");

		List<Map<Object, Object>> books = new ArrayList<>();
		Map<Object, Object> book1 = new HashMap<>();
		book1.put("id", 1);
		book1.put("name", "红楼梦");
		books.add(book1);

		Map<Object, Object> book2 = new HashMap<>();
		book2.put("id", 5);
		book2.put("name", "贵阳");
		books.add(book2);

		student1.put("books", books);

		Map<Object, Object> student2 = new HashMap<>();
		student2.put("id", 1);
		student2.put("name", "dongchunfu");
		student2.put("books", books);

		List<Map<Object, Object>> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);

		dataMap.put("students", students);
		saveESObject.setDataMap(dataMap);

		return saveESObject;
	}

}
