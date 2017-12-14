package com.hivescm.escenter.core.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hivescm.common.serialize.api.json.GsonSerialize;
import com.hivescm.search.index.IndexParser;
import com.hivescm.search.index.TestEnvConfig;

@Component
public class IndexAdmin {
	@Autowired
	private Client client;
	private static final Map<String, IndexHelper> CACHE = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexAdmin.class);

	public TransportClient createTestClient() throws UnknownHostException {
		final Settings settings = Settings.builder().put("cluster.name", "hivescm-test")
				.put("xpack.security.user", "elastic:changeme").build();

		// TransportClient transportClient = new PreBuiltTransportClient(settings);
		TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
		final String[] addresses = "192.168.177.11:9300,192.168.177.226:9300".split(",");
		for (String address : addresses) {
			final String[] hostAndPort = address.split(":");
			final InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
					InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
			transportClient.addTransportAddress(inetSocketTransportAddress);
		}
		return transportClient;
	}

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
		client.admin().indices().prepareCreate("tms-back-warehouse-detail");
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

	/**
	 * dump ES索引结构
	 * 
	 * @throws UnknownHostException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void dump_es_index() throws UnknownHostException, InterruptedException, ExecutionException {
		System.out.println("******************dump es index document******************");
		ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();
		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
		immutableOpenMap.forEach((entity) -> {
			String index = entity.key;
			if (index.startsWith("tms")) {// 只迁移tms开头的索引
				entity.value.getMappings().forEach((cursor) -> {
//					TestEnvConfig.createIndex(index, cursor);
				});
			}
		});
		System.out.println("******************dump es index document******************");
	}

	public boolean isExistsIndex(String index) throws InterruptedException, ExecutionException {
		IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index)).get();
		return response.isExists();
	}

	/***
	 * 如果索引存在则删除
	 * 
	 * @param index
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private boolean existsAndDelete(String index) throws InterruptedException, ExecutionException {
		if (isExistsIndex(index)) {
			return deleteIndex(index);
		}
		return false;
	}

	public boolean deleteIndex(String index) throws InterruptedException, ExecutionException {
		DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).get();
		return deleteResponse.isAcknowledged();
	}

	public boolean createIndex(String index) throws InterruptedException, ExecutionException {
		CreateIndexResponse response = client.admin().indices().create(new CreateIndexRequest(index)).get();
		return response.isAcknowledged();
	}

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
