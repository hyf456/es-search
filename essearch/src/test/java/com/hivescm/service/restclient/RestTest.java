package com.hivescm.service.restclient;

import com.google.gson.Gson;
import com.hivescm.Application;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by DongChunfu on 2017/8/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RestTest {

	private static final Gson GSON = new Gson();
	private static final String SYSTEM_NAME = "escenter";
	private static final String INDEX_NAME = "test_ool";
	private static final String TYPE_NAME = "ool";

	@Autowired
	private ESNestedSearchService esSearchService;

	@Test
	public void testRestClient() throws IOException {
//		RestClient restClient = RestClient.builder(new HttpHost("47.93.8.246", 9200, "http")).build();
//
//		Map<String, String> param = new HashMap<>();
//		String script = "def f = ctx._source.student; f.books.findAll{ f.books +=listValue}";
//
//		Map<String, String> params = new HashMap<>();
//		Map<String, String> objectParam = new HashMap<>();
//		objectParam.put("name", "http");
//		objectParam.put("id", "15");
//		List<Map<String, String>> listParam = new ArrayList<>();
//		listParam.add(objectParam);
//
//		params.put("listValue", GSON.toJson(listParam));
//		params.put("objectValue", GSON.toJson(objectParam));
//
//		param.put("listValue", GSON.toJson(listParam));
//		param.put("objectValue", GSON.toJson(objectParam));
//
//		param.put("params", GSON.toJson(params));
//
//		StringBuilder dBuilder = new StringBuilder();
//		dBuilder.append("%20-d%20'").append(GSON.toJson(param)).append("'");
//
//		final Script groovyScript = new Script(script, ScriptService.ScriptType.INLINE, "groovy", params);
//		HttpEntity entity = new StringEntity(GSON.toJson(param), ContentType.APPLICATION_JSON);
//
//		final Response post = restClient.performRequest("POST", "/test_ool/ool/1/_create" /*+ dBuilder.toString()*/,
//				Collections.emptyMap(), entity);
//
//		System.out.println(post);
	}

}
