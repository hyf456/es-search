package com.hivescm.search.admin;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.escenter.core.config.SyncUtils;
import com.hivescm.escenter.core.config.SyncUtils.IndexOperator;
import com.hivescm.escenter.core.config.SyncUtils.SyncData;
import com.hivescm.escenter.core.config.SyncUtils.SyncIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class TestSyncUtils {
	TransportClient devClient = null;
	TransportClient testClient = null;
	{
		try {
			devClient = SyncUtils.createEsClient("192.168.177.132:9300,192.168.177.142:9300,192.168.177.134:9300", "elastic",
					"changeme", "escenter_dev");
			testClient = SyncUtils.createEsClient("192.168.177.11:9300,192.168.177.226:9300", "elastic", "changeme",
					"real-test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_sync_index() {
		try {
			SyncUtils.sync_index_struct(devClient, testClient, new SyncIndex() {

				@Override
				public boolean isSync(String index, String type) {
					if ("my_index_v1".equals(index)) {
						return true;
					} else {
						return "my_index_v2".equals(index);
					}
				}

				@Override
				public IndexOperator ifExist(String index, String type) {
					return IndexOperator.REMOVE_AND_CREATE;
				}
			});
			// 同步索引
			SyncUtils.sync_data(devClient, testClient, new SyncData() {

				@Override
				public boolean isSync(String index, String type) {
					if ("my_index_v1".equals(index)) {
						return true;
					} else {
						return "my_index_v2".equals(index);
					}
				}

				@Override
				public String getSortField(String index) {
					if ("my_index_v2".equals(index)) {
						return "name";
					} else {
						return "id";
					}
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void test_sync_data_one() {
		// 同步数据
		SyncUtils.sync_data(devClient, "my_index_v1", testClient, "my_index_v1", "employee", "id", SortOrder.ASC);
		SyncUtils.sync_data(devClient, "my_index_v2", testClient, "my_index_v2", "employee", "name", SortOrder.ASC);

	}
}
