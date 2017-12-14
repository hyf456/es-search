package com.hivescm.controller;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchSaveESObject;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.controller.ESSearchController;
import com.hivescm.escenter.service.ESSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ESSaveTest {

	private static final String INDEX_NAME = "escenter-1114";
	private static final String TYPE_NAME = "test";
	private static final String SYSTEM_NAME = "ES";

	@Resource
	private ESSearchController esSearchController;

	@Test // 单一新增
	public void saveTest() throws IOException {

		final DataResult<Boolean> dataResult = esSearchController.esSave(generateSaveESObject());

		if(dataResult.isSuccess()){
			System.out.println("------->"+dataResult.getResult());
		}else{
			System.out.println("------->"+dataResult.getStatus());
		}
	}

	@Test  // 批量新增
	public void batchSave() throws Exception {
		BatchSaveESObject batchSaveESObject = new BatchSaveESObject();;
		List<SaveESObject> saveESObjects = new ArrayList<>();
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		saveESObjects.add(generateSaveESObject());
		batchSaveESObject.setSaveDatas(saveESObjects);
		final DataResult<Boolean> dataResult = esSearchController.esBatchSave(batchSaveESObject);
		if(dataResult.isSuccess()){
			System.out.println("------->"+dataResult.getResult());
		}else{
			System.out.println("------->"+dataResult.getStatus());
		}

	}

	private SaveESObject generateSaveESObject() {
		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);
		saveESObject.setSystemName(SYSTEM_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		Random r = new Random();
		final int id = r.nextInt();

		ukMap.put("id", id);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("age", 23);
		dataMap.put("name", "大北京");
		// 数组
		dataMap.put("hobby", new String[] { "电影", "3C", "音乐" });
//
//		// 集合
//		List<Map<Object, Object>> list = new ArrayList<>();
//		Map<Object, Object> subMap1 = new HashMap<>();
//		subMap1.put("id", 1);
//		subMap1.put("name", "浙江");
//		subMap1.put("date", "2013-08-01");
//		list.add(subMap1);
//		Map<Object, Object> subMap2 = new HashMap<>();
//		subMap2.put("id", 5);
//		subMap2.put("name", "贵阳");
//		subMap2.put("date", "2012-07-01");
//		list.add(subMap2);

//		dataMap.put("list", list);
		saveESObject.setDataMap(dataMap);

		return saveESObject;
	}

}
