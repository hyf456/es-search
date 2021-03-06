package com.hivescm.escenter.core.config;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hivescm.search.index.IndexParser;

@Component
public class IndexAdmin {
	@Autowired
	private Client client;
	private static final Map<String, IndexHelper> CACHE = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexAdmin.class);

	@PostConstruct
	private void init() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {// 异步定时更新索引结构
					for (Map.Entry<String, IndexHelper> entry : IndexAdmin.this.CACHE.entrySet()) {
						String keyVal[] = entry.getKey().split(":");
						String index = keyVal[0];
						String type = keyVal[1];
						String mapping = loadIndexStruct(index, type);
						register(index, type, mapping);
					}
				} catch (Exception e) {
					LOGGER.error("refresh.index.ERROR:" + e.getMessage());
				}
			}
		}, 10000, 5000);
	}

	public IndexHelper getIndexHelper(String index, String type) {
		String key = generatedKey(index, type);
		if (!CACHE.containsKey(key)) {
			String mapping = loadIndexStruct(index, type);
			register(index, type, mapping);
		}
		return CACHE.get(key);
	}

	/**
	 * 装载索引数据结构
	 * 
	 * @param index
	 * @param type
	 * @return
	 */
	private String loadIndexStruct(String index, String type) {
		ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();
		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
		if (immutableOpenMap != null) {
			IndexMetaData metaData = immutableOpenMap.get(index);
			if (metaData != null) {
				ImmutableOpenMap<String, MappingMetaData> mappings = metaData.getMappings();
				if (mappings != null) {
					MappingMetaData mappingMetaData = mappings.get(type);
					if (mappingMetaData != null) {
						CompressedXContent content = mappingMetaData.source();
						if (content != null) {
							return content.toString();
						}
					}
				}
			}
		}
		LOGGER.error("获取ES数据结构失败 index:" + index + "|type:" + type);
		return null;
	}

	private String generatedKey(String index, String type) {
		return index + ":" + type;
	}

//	/**
//	 * dump ES索引结构
//	 * 
//	 * @throws UnknownHostException
//	 * @throws ExecutionException
//	 * @throws InterruptedException
//	 */
//	public void dump_es_index() throws UnknownHostException, InterruptedException, ExecutionException {
//		System.out.println("******************dump es index document******************");
//		ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();
//		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
//		immutableOpenMap.forEach((entity) -> {
//			String index = entity.key;
//			if (index.startsWith("tms")) {// 只迁移tms开头的索引
//				entity.value.getMappings().forEach((cursor) -> {
//					// TestEnvConfig.createIndex(index, cursor);
//				});
//			}
//		});
//		System.out.println("******************dump es index document******************");
//	}

	private void register(String index, String type, String mapping) {
		if (mapping != null) {
			String key = generatedKey(index, type);
			IndexParser parser = IndexParser.parseFromIndexType(mapping, type);
			IndexHelper helper = new IndexHelper(parser.getProperties());
			CACHE.put(key, helper);
			LOGGER.info("register or update index:" + index + "|type:" + type);
		}
	}
}
