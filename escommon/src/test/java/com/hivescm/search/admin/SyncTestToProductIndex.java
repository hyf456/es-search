package com.hivescm.search.admin;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.escenter.core.config.SyncUtils;
import com.hivescm.escenter.core.config.SyncUtils.IndexOperator;
import com.hivescm.escenter.core.config.SyncUtils.SyncIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class SyncTestToProductIndex {
	TransportClient testClient = null;
	TransportClient productClient = null;
	{
		try {
			testClient = SyncUtils.createEsClient("192.168.177.11:9300,192.168.177.226:9300", "elastic", "changeme",
					"real-test");
			// productClient = SyncUtils.createEsClient("139.199.97.199:9300",
			// "elastic", "changeme", "ES");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生产环境请不要随意处理
	 */
	// @Test
	public void test_sync_index() {
		try {
			SyncUtils.sync_index_struct(testClient, productClient, new SyncIndex() {
				@Override
				public boolean isSync(String index, String type) {
					if (index.startsWith("tms-") && !type.equals("_default_")) {
						System.out.println(index + "->" + type);
						return true;
					}
					if (index.equals("pay_transaction")) {
						System.out.println(index + "-->" + type);
						return true;
					}
					return false;
				}

				@Override
				public IndexOperator ifExist(String index, String type) {
					return IndexOperator.REMOVE_AND_CREATE;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
