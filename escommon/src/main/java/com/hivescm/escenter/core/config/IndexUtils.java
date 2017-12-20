package com.hivescm.escenter.core.config;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexUtils {
	static final Logger LOGGER = LoggerFactory.getLogger(IndexUtils.class);

	/**
	 * 获取索引元数据信息
	 * 
	 * @param index
	 * @param type
	 * @return
	 */
	public static MappingMetaData loadIndexMeta(Client client, String index, String type) {
		ClusterStateResponse response = client.admin().cluster().prepareState().execute().actionGet();
		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
		if (immutableOpenMap != null) {
			IndexMetaData metaData = immutableOpenMap.get(index);
			if (metaData != null) {
				ImmutableOpenMap<String, MappingMetaData> mappings = metaData.getMappings();
				if (mappings != null) {
					return mappings.get(type);
				}
			}
		}
		LOGGER.error("获取ES数据结构失败 index:" + index + "|type:" + type);
		return null;
	}

	public static boolean isExistsIndex(Client client, String index) throws InterruptedException, ExecutionException {
		IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index)).get();
		return response.isExists();
	}

	public static boolean deleteIndex(Client client, String index) throws InterruptedException, ExecutionException {
		if (isExistsIndex(client, index)) {
			DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).get();
			return deleteResponse.isAcknowledged();
		} else {
			return false;
		}
	}

	public static boolean createIndex(Client client, String index) throws InterruptedException, ExecutionException {
		CreateIndexResponse response = client.admin().indices().create(new CreateIndexRequest(index)).get();
		return response.isAcknowledged();
	}

	/**
	 * 创建索引
	 * 
	 * @param client
	 * @param index
	 * @param type
	 * @param data
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static boolean createIndex(Client client, String index, String type, Map<String, Object> data)
			throws InterruptedException, ExecutionException {
		CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(index).addMapping(type, data)
				.get();
		return createIndexResponse.isAcknowledged();
	}
}
