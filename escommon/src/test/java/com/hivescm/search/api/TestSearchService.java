package com.hivescm.search.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestSearchService {
	@Autowired
	private SearchService searchService;

	@Test
	public void test_countAll() {
		Assert.assertTrue(searchService.countAll("my_index_v1", "employee") > 0);
	}
}
