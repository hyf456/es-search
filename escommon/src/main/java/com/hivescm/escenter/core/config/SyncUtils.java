package com.hivescm.escenter.core.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.hivescm.search.index.ScanDocuments;

/**
 * ES 同步工具
 * 
 * @author SHOUSHEN LUAN
 */
public class SyncUtils {
	/**
	 * 创建ES client
	 * 
	 * @param nodes 127.0.0.1:1234,127.0.0.1:1234
	 * @param user 用户名
	 * @param pwd 密码
	 * @param clusterName 集群名称
	 * @return
	 * @throws NumberFormatException
	 * @throws UnknownHostException
	 */
	public static TransportClient createEsClient(String nodes, String user, String pwd, String clusterName)
			throws NumberFormatException, UnknownHostException {
		final Settings settings = Settings.builder().put("cluster.name", clusterName)
				.put("xpack.security.user", user + ":" + pwd).build();
		TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
		final String[] addresses = nodes.split(",");
		for (String address : addresses) {
			final String[] hostAndPort = address.split(":");
			final InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
					InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
			transportClient.addTransportAddress(inetSocketTransportAddress);
		}
		return transportClient;
	}

	/**
	 * dump ES索引结构
	 * 
	 * @throws UnknownHostException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void sync_index_struct(TransportClient srcClient, TransportClient destClient, SyncIndex syncIndex)
			throws UnknownHostException, InterruptedException, ExecutionException {
		System.out.println("******************dump es index document******************");
		ClusterStateResponse response = srcClient.admin().cluster().prepareState().execute().actionGet();
		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
		immutableOpenMap.forEach((entity) -> {
			final String index = entity.key;
			Map<String, Map<String, Object>> types = getIndexTypes(entity.value.getMappings(), syncIndex, index, destClient);
			if (types.size() > 0) {
				CreateIndexRequestBuilder indexRequestBuilder = destClient.admin().indices().prepareCreate(index);
				System.out.println("同步索引--->>>>>>index:" + index);
				for (Map.Entry<String, Map<String, Object>> entry : types.entrySet()) {
					indexRequestBuilder.addMapping(entry.getKey(), entry.getValue());
					System.out.println("\t\ttype:" + entry.getKey() + "\t-> mapping:" + entry.getValue());
				}
				CreateIndexResponse createIndexResponse = indexRequestBuilder.get();
				System.out.println(createIndexResponse.isAcknowledged());
			}
		});
		System.out.println("******************dump es index document******************");
	}

	/**
	 * 获取索引下面的所有类型
	 * 
	 * @param immutableOpenMap
	 * @param syncIndex
	 * @param index
	 * @param destClient
	 * @return
	 */
	private static Map<String, Map<String, Object>> getIndexTypes(
			ImmutableOpenMap<String, MappingMetaData> immutableOpenMap, SyncIndex syncIndex, String index,
			TransportClient destClient) {
		Map<String, Map<String, Object>> types = new HashMap<>();
		immutableOpenMap.forEach((cursor) -> {
			try {
				String type = cursor.key;
				if (syncIndex.isSync(index, type)) {
					if (isExistsIndex(destClient, index)) {
						IndexOperator indexOperator = syncIndex.ifExist(index, type);
						if (indexOperator == IndexOperator.SKIP) {
							return;
						} else if (indexOperator == IndexOperator.REMOVE_AND_CREATE) {
							deleteIndex(destClient, index);
						}
					}
					types.put(type, cursor.value.getSourceAsMap());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return types;
	}

	/**
	 * 同步数据
	 * 
	 * @param srcClient
	 * @param destClient
	 * @param syncIndex
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void sync_data(TransportClient srcClient, TransportClient destClient, SyncData syncData)
			throws UnknownHostException, InterruptedException, ExecutionException {
		System.out.println("******************dump es index document******************");
		ClusterStateResponse response = srcClient.admin().cluster().prepareState().execute().actionGet();
		ImmutableOpenMap<String, IndexMetaData> immutableOpenMap = response.getState().getMetaData().getIndices();
		immutableOpenMap.forEach((entity) -> {
			final String index = entity.key;
			entity.value.getMappings().forEach((cursor) -> {
				String type = cursor.key;
				if (syncData.isSync(index, type)) {
					String field = syncData.getSortField(index);
					sync_data(srcClient, index, destClient, index, type, field, SortOrder.ASC);
				}
			});
		});
		System.out.println("******************dump es index document******************");
	}

	/**
	 * 创建索引
	 * 
	 * @param index
	 * @param cursor
	 * @throws IOException
	 */
	public static void createIndex(Client client, String index, ObjectObjectCursor<String, MappingMetaData> cursor) {
		try {
			String type = cursor.key;
			String mapping = cursor.value.source().toString();
			System.out.println("index:" + cursor + "\ttype:" + type + "\t-> mapping:" + mapping);
			if ("_default_".equals(type)) {
				return;
			}
			CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(index)
					.addMapping(type, cursor.value.getSourceAsMap()).get();
			System.out.println(createIndexResponse.isAcknowledged());
			System.out.println(createIndexResponse.toString());
			System.out.println("创建 index:" + index + " type:" + type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否存在索引
	 * 
	 * @param client
	 * @param index
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static boolean isExistsIndex(Client client, String index) throws InterruptedException, ExecutionException {
		IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index)).get();
		return response.isExists();
	}

	/**
	 * 存在并删除索引
	 * 
	 * @param client
	 * @param index
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static boolean deleteIndex(Client client, String index) throws InterruptedException, ExecutionException {
		if (isExistsIndex(client, index)) {
			DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).get();
			return deleteResponse.isAcknowledged();
		} else {
			return false;
		}
	}

	/**
	 * 同一个ES服务中拷贝数据
	 * 
	 * @param srcIndex 原索引
	 * @param dectIndex 目标索引
	 * @param type 索引类型
	 * @param client
	 */
	public static void sync_data(Client srcClient, String srcIndex, Client dectClient, String dectIndex, String type,
			String sortName, SortOrder order) {
		int batch = 100;
		ScanDocuments scanDocuments = new ScanDocuments(srcIndex, type, batch);
		scanDocuments.start(srcClient, sortName, order);
		int count = 0;
		List<IndexRequestBuilder> batchList = new ArrayList<>(batch);
		while (scanDocuments.hasNext()) {
			SearchHit hit = scanDocuments.next();
			IndexRequestBuilder indexRequestBuilder = dectClient.prepareIndex(dectIndex, type).setId(hit.getId())
					.setSource(hit.getSource());
			batchList.add(indexRequestBuilder);
			if (batchList.size() == batch) {
				boolean success = batchSave(batchList, dectClient);
				System.out.println("批量提交" + batch + "->endIndex:" + count + " 状态:" + success);
			}
			count++;
		}
		if (batchList.size() > 0) {
			boolean success = batchSave(batchList, dectClient);
			System.out.println("批量提交" + batch + "->endIndex:" + count + " 状态:" + success);
		}
		System.out.println("迁移数据:" + count);
	}

	/**
	 * 批量保存
	 * 
	 * @param batchList
	 * @return
	 */
	private static boolean batchSave(List<IndexRequestBuilder> batchList, Client client) {
		if (batchList.size() > 0) {
			BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
			for (IndexRequestBuilder requestBuilder : batchList) {
				bulkRequestBuilder.add(requestBuilder);
			}
			return !bulkRequestBuilder.get().hasFailures();
		} else {
			return false;
		}
	}

	public static interface SyncIndex {
		/**
		 * 是否同步
		 * 
		 * @param index
		 * @param type
		 * @return
		 */
		boolean isSync(String index, String type);

		/**
		 * 针对目标ES已存在该索引和类型时处理手段
		 * 
		 * @param index
		 * @param type
		 * @return
		 */
		default IndexOperator ifExist(String index, String type) {
			return IndexOperator.SKIP;
		}
	}

	public static interface SyncData {
		/**
		 * 是否同步
		 * 
		 * @param index
		 * @param type
		 * @return
		 */
		boolean isSync(String index, String type);

		/**
		 * 排序字段(建议使用唯一的Number类型的字段作为排序字段，例如:id)
		 * 
		 * @param index
		 * @return
		 */
		String getSortField(String index);
	}

	public static enum IndexOperator {
		SKIP, // 跳过处理
		REMOVE_AND_CREATE;// 移除并从新创建索引
	}
}
