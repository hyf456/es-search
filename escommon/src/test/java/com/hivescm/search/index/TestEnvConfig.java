package com.hivescm.search.index;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

public class TestEnvConfig {
	private static TransportClient client = null;
	static {
		init_test_env_client();
	}

	/**
	 * 初始化测试环境ES client
	 */
	private static void init_test_env_client() {
		try {
			final Settings settings = Settings.builder().put("cluster.name", "real-test")
					.put("xpack.security.user", "elastic:changeme").build();
			TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
			final String[] addresses = "192.168.177.11:9300,192.168.177.226:9300".split(",");
			for (String address : addresses) {
				final String[] hostAndPort = address.split(":");
				final InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
						InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
				transportClient.addTransportAddress(inetSocketTransportAddress);
			}
			client = transportClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取测试环境ES client
	 * 
	 * @return
	 */
	public static TransportClient getTestEnvEsClient() {
		return client;
	}

	/**
	 * 测试环境创建索引
	 * 
	 * @param index
	 * @param cursor
	 * @throws IOException
	 */
	public static void createIndex(String index, ObjectObjectCursor<String, MappingMetaData> cursor) {
		try {
			String type = cursor.key;
			String mapping = cursor.value.source().toString();
			System.out.println("index:" + cursor + "\ttype:" + type + "\t-> mapping:" + mapping);
			if ("_default_".equals(type)) {
				return;
			}
			CreateIndexResponse createIndexResponse = TestEnvConfig.getTestEnvEsClient().admin().indices()
					.prepareCreate(index).addMapping(type, cursor.value.getSourceAsMap()).get();
			System.out.println(createIndexResponse.isAcknowledged());
			System.out.println(createIndexResponse.toString());
			System.out.println("创建 index:" + index + " type:" + type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
