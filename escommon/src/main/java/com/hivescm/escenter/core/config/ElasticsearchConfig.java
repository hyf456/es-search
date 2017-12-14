package com.hivescm.escenter.core.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DongChunfu on 2017/8/15
 */
@Configuration
public class ElasticsearchConfig {

	@Value("${es.cluster-nodes}")
	private String clusterNodes;

	@Value("${es.cluster-name}")
	private String clusterName;

	@Value("${es.username}")
	private String username;

	@Value("${es.password}")
	private String password;

	@Bean
	public TransportClient clientFactory() throws UnknownHostException {
		final Settings settings = Settings.builder().put("cluster.name", clusterName)
				.put("xpack.security.user", username + ":" + password).build();

		// TransportClient transportClient = new PreBuiltTransportClient(settings);
		TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
		final String[] addresses = clusterNodes.split(",");
		for (String address : addresses) {
			final String[] hostAndPort = address.split(":");
			final InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
					InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
			transportClient.addTransportAddress(inetSocketTransportAddress);
		}
		return transportClient;
	}
}
