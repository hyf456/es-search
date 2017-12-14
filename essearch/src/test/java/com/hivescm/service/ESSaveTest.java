package com.hivescm.service;

import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchSaveESObject;
import com.hivescm.escenter.common.SaveESObject;
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
public class ESSaveTest {

	private static final String INDEX_NAME = "test";
	private static final String TYPE_NAME = "test";

	@Resource
	private ESNestedSearchService esSearchService;

	@Test // 单一新增
	public void saveTest() throws IOException {
		esSearchService.save(generateSaveESObject());
	}

	@Test  // 批量新增
	public void batchSave() throws Exception {
		for (int i = 0; i < 100000; i++) {
			BatchSaveESObject batchSaveESObject = new BatchSaveESObject();
			List<SaveESObject> saveESObjects = new ArrayList<>();

			for (int j = 0; j < 15; j++) {
				saveESObjects.add(generateSaveESObject());
			}

			batchSaveESObject.setSaveDatas(saveESObjects);
			final DataResult<Boolean> dataResult = esSearchService.batchSave(batchSaveESObject);
		}
	}

	private SaveESObject generateSaveESObject() {
		SaveESObject saveESObject = new SaveESObject();
		saveESObject.setIndexName(INDEX_NAME);
		saveESObject.setTypeName(TYPE_NAME);

		Map<Object, Object> ukMap = new HashMap<>();
		Random r = new Random();
		final int id = r.nextInt();
		ukMap.put("id", id);
		saveESObject.setUkMap(ukMap);

		Map<Object, Object> dataMap = new HashMap<>();
		dataMap.put("batchCode", "text222"+id);
		dataMap.put("billType", id+id);
		dataMap.put("type", id);
		dataMap.put("companyId", id);
		dataMap.put("costTypeId", id+id+id);
		dataMap.put("cost", "keyword2222"+id);
		dataMap.put("createTime", +id);
		dataMap.put("createUser", +id);
		dataMap.put("dispatcherTime", +id);
		dataMap.put("dispatcherType", +id);
		dataMap.put("updateUser", +id);
		dataMap.put("volLoading", "text2");
		dataMap.put("volWeight", +id+"text2");

		saveESObject.setDataMap(dataMap);
		return saveESObject;
	}

}
