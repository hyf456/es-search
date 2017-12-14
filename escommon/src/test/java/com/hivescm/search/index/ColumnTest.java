package com.hivescm.search.index;

import org.junit.Assert;
import org.junit.Test;

public class ColumnTest {
	@Test
	public void test_build_column() {
		IndexField column = IndexField.make().setType(DataType.TEXT).setIKAnalyzer().setIncludeInAll(true).setFieldKeyword();
		String expected = "{\"include_in_all\":true,\"analyzer\":\"ik_max_word\",\"type\":\"text\",\"fields\":{\"raw\":{\"type\":\"keyword\"}}}";
		Assert.assertEquals(expected, column.getResultAsJson());
		expected = "{\"type\":\"integer\"}";
		column = IndexField.make().setType(DataType.INTEGER);
		Assert.assertEquals(expected, column.getResultAsJson());
		column = IndexField.make().setType(DataType.KEYWORD).setDocValues(true);
		expected = "{\"type\":\"keyword\",\"doc_values\":true}";
		Assert.assertEquals(expected, column.getResultAsJson());
	}
}
