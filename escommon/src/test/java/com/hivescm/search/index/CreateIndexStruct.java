package com.hivescm.search.index;

import java.util.Iterator;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.common.serialize.api.json.JsonUtils;

public class CreateIndexStruct {
	private static RestTemplate restTemplate = new RestTemplate();

	public static void dump_index() {
		String url = "http://192.168.177.142:9200/_mapping";
		ResponseEntity<String> value = restTemplate.getForEntity(url, String.class);
		String data = value.getBody();
		ObjectNode objectNode = (ObjectNode) JsonUtils.parser(data);
		System.out.println(objectNode.size());
		Iterator<String> iterator = objectNode.fieldNames();
		while (iterator.hasNext()) {
			String indexName = iterator.next();
			System.out.println("index:\t" + indexName);
			url = "http://192.168.177.142:9200/" + indexName;
			if (!indexName.startsWith("zipkin-")) {
				ResponseEntity<String> indexStructResponse = restTemplate.getForEntity(url, String.class);
				String json = indexStructResponse.getBody();
				IndexStruct struct = IndexParser.parseAndBuildUpdateIndex(json);
				if (struct == null) {
					System.out.println("skip index:" + indexName);
					continue;
				}
				update(struct);
			}
		}
	}

	/**
	 * 更新字段结果（为type:text类型字段补充keyword支持）
	 * 
	 * @param struct
	 */
	private static void update(IndexStruct struct) {
		if (struct.getIndex().equals("my_index_v1")) {
			String url = "http://192.168.177.134:9200/" + struct.getIndex() + "/_mapping/" + struct.getType() + "?pretty";
			System.out.println(url);
			System.out.println(struct.getPropertiesJson());
			HttpEntity entity = new HttpEntity(struct.getPropertiesJson());
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
			System.out.println("更新索引结果:" + responseEntity.getBody());
		}
	}

	public static void main(String[] args) {
		// url="http://192.168.177.142:9200/my_index_v1";
		// ResponseEntity<String> indexStructResponse =
		// restTemplate.getForEntity(url, String.class);
		// String json = indexStructResponse.getBody();
		// System.out.println(json);
		// IndexParser parser = IndexParser.parse(json);
		// parser.getProperties().forEach((key,jsonObj)->{
		// System.out.println(key+"->"+jsonObj.toString());
		// });
		// System.out.println(IndexHelper.parse(json).isKeywordField("name"));
	}
	
	// {"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}}
	static boolean isSupportTextAndKeyword(Map<String, Object> map) {
		if ("text".equals(map.get("type"))) {
			Field field = (Field) map.get("fields");
			return "keyword".equals(field.raw.get("type"));
		}
		return false;
	}

	static void createIndex() {
		String url = "http://192.168.177.134:9200/my_index_v1?pretty";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String json = "{\"mappings\":{\"employee\":{\"properties\":{\"name\":{\"type\":\"keyword\"},\"description\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_max_word\",\"include_in_all\":\"true\",\"boost\":8,\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}},\"age\":{\"type\":\"integer\"},\"test\":{\"type\":\"text\"}}}}}";

		IndexStruct indexStruct = IndexStruct.make("my_index_v1", "employee")//
				.addColumn("name", IndexField.make().setField(Field.make().setType(DataType.KEYWORD)))//
				.addColumn("description", IndexField.make().setType(DataType.TEXT).setIKAnalyzer())
				.addColumn("age", IndexField.make().setType(DataType.INTEGER).setDocValues(true));
		System.out.println(indexStruct.getIndexJson());
		HttpEntity entity = new HttpEntity(indexStruct.getIndexJson(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		json = responseEntity.getBody();
		System.out.println(json);
	}

	public static void addFiled() {
		String url = "http://192.168.177.134:9200/my_index_v1/_mapping/employee?pretty";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String json = "{\"mappings\":{\"employee\":{\"properties\":{\"name\":{\"type\":\"keyword\"},\"description\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_max_word\",\"include_in_all\":\"true\",\"boost\":8,\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}},\"age\":{\"type\":\"integer\"},\"test\":{\"type\":\"text\"}}}}}";

		IndexStruct indexStruct = IndexStruct.make("my_index_v1", "employee")//
				.addColumn("name", IndexField.make().setField(Field.make().setType(DataType.KEYWORD)));
		System.out.println("添加字段:" + indexStruct.getPropertiesJson());
		HttpEntity entity = new HttpEntity(indexStruct.getPropertiesJson(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		json = responseEntity.getBody();
		System.out.println(json);
	}

}
