package com.hivescm.search.index;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hivescm.escenter.core.config.IndexAdmin;

/**
 * 更新TMS索引数据结构
 * 
 * @author SHOUSHEN LUAN
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/applicationContext.xml" })
public class UpdateTmsIndexOperator {
	@Autowired
	private TransportClient esClient;
	@Autowired
	private IndexAdmin indexAdmin;

	// @Test
	public void test_sync_index_data() throws InterruptedException, ExecutionException {
		String this_index = "tms-waybill", new_index = "tms-waybill_tmp";
		String type = "tms-waybill-list";
		// indexAdmin.deleteIndex(new_index);
		// MappingMetaData metaData = indexAdmin.loadIndexMeta(this_index, type);
		// try {
		// Map<String, Object> data = metaData.getSourceAsMap();
		// Map<String, Object> properties = (Map<String, Object>)
		// data.get("properties");
		// {
		// Map<String, Object> weight = (Map<String, Object>)
		// properties.get("weight");
		// weight.put("type", "double");
		// weight.remove("fielddata");
		// }
		// {
		// Map<String, Object> volume = (Map<String, Object>)
		// properties.get("volume");
		// volume.put("type", "double");
		// }
		// {
		// Map<String, Object> totalFee = (Map<String, Object>)
		// properties.get("totalFee");
		// totalFee.remove("fielddata");
		// totalFee.put("type", "double");
		// }
		// CreateIndexResponse createIndexResponse =
		// esClient.admin().indices().prepareCreate(new_index)
		// .addMapping(type, data).get();
		// Assert.assertTrue(createIndexResponse.isAcknowledged());
		// System.out.println(createIndexResponse.toString());
		// } catch (Exception e) {
		// e.printStackTrace();
		// Assert.fail("创建索引失败");
		// }

		ScanDocuments scanDocuments = new ScanDocuments(this_index, type, 100);
		scanDocuments.start(esClient);
		int total = 0;
		while (scanDocuments.hasNext()) {
			SearchHit hit = scanDocuments.next();
			// System.out.println(hit.getId() + "-" + hit.getSourceAsMap());
			SearchResponse response = esClient.prepareSearch(new_index).setTypes(type)
					.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id", hit.getId()))).get();
			// IndexResponse response = esClient.prepareIndex(new_index,
			// type).setId(hit.getId()).setSource(hit.getSource())
			// .get();

			if (response.getHits().totalHits == 1L) {
				// System.out.println(response.getHits().totalHits);
			} else {
				System.out.println(hit.getId() + "-" + hit.getSourceAsMap());
			}
			total++;
		}
	}
}
