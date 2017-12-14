package com.hivescm.service.nested;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.NestedESObject;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.common.UpdateESObject;
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
public class OLTest {
	private static final String SYSTEM_NAME = "ESCENTER";
	private static final String INDEX_NAME = "test_ol";
	private static final String TYPE_NAME = "ol";

	@Resource
	private ESNestedSearchService esSearchService;

	@Test // 存储
	public void saveTest() throws IOException {
		esSearchService.save(generateSaveESObject());
	}

	@Test // 删除 （测试通过）
	public void deleteTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "2128314025");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setList(Boolean.TRUE);
		nestedESObject.setFieldName("books");

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 1);

		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.DELETE);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
	}

	@Test // 更新
	public void updateTagTest() throws Exception {
		UpdateESObject updateESObject = new UpdateESObject();
		updateESObject.setSystemName(SYSTEM_NAME);
		updateESObject.setIndexName(INDEX_NAME);
		updateESObject.setTypeName(TYPE_NAME);

		updateESObject.setNestedOperateType(OperateTypeEnum.UPDATE);

		Map<Object, Object> ukMap = new HashMap<>();
		ukMap.put("id", "1140818518");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setList(Boolean.TRUE);
		nestedESObject.setFieldName("books");

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 5);
		dataMap.put("name", "冰封王座");
		updateESObject.setDataMap(dataMap);
		updateESObject.setNestedESObject(nestedESObject);

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
		ukMap.put("id", "1140818518");
		updateESObject.setUkMap(ukMap);

		NestedESObject nestedESObject = new NestedESObject();
		nestedESObject.setList(Boolean.TRUE);
		nestedESObject.setFieldName("books");

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("id", 1);
		dataMap.put("name", "多啦A梦");
		updateESObject.setDataMap(dataMap);

		updateESObject.setNestedESObject(nestedESObject);
		updateESObject.setNestedOperateType(OperateTypeEnum.ADD);

		final DataResult<Boolean> update = esSearchService.update(updateESObject);
		System.out.println(update);
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
		dataMap.put("id",id);
		dataMap.put("logol", "thinkpad");

		List<Map<Object, Object>> books = new ArrayList<>();
		Map<Object, Object> book1 = new HashMap<>();
		book1.put("id", 1);
		book1.put("name", "浙江");
		books.add(book1);

		Map<Object, Object> book2 = new HashMap<>();
		book2.put("id", 5);
		book2.put("name", "贵阳");
		books.add(book2);

		dataMap.put("books", books);
		saveESObject.setDataMap(dataMap);

		return saveESObject;
	}
}
