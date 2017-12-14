package com.hivescm.search.index;

import org.junit.Assert;
import org.junit.Test;

public class IndexParserTest {
	@Test
	public void test_parseAndBuildUpdateIndex() {
		String json = "{\"index_name\":{\"mappings\":{\"employee\":{\"properties\":{\"name\":{\"type\":\"keyword\"},\"description\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_max_word\",\"include_in_all\":\"true\",\"boost\":8,\"fields\":{\"keyword\":{\"type\":\"keyword\",\"ignore_above\":256}}},\"age\":{\"type\":\"integer\"},\"test\":{\"type\":\"text\"}}}}}}";
		System.out.println(json);
		IndexStruct indexStruct = IndexParser.parseAndBuildUpdateIndex(json);
		Assert.assertEquals("index_name", indexStruct.getIndex());
		Assert.assertEquals("employee", indexStruct.getType());
		String expected = "{\"mappings\":{\"employee\":{\"properties\":{\"test\":{\"type\":\"text\",\"fields\":{\"raw\":{\"type\":\"keyword\"}}}}}}}";
		System.out.println(indexStruct.getIndexJson());
		Assert.assertEquals(expected, indexStruct.getIndexJson());
		expected = "{\"properties\":{\"test\":{\"type\":\"text\",\"fields\":{\"raw\":{\"type\":\"keyword\"}}}}}";
		Assert.assertEquals(expected, indexStruct.getPropertiesJson());
	}
}
