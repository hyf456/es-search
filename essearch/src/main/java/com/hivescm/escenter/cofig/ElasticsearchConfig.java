//package com.hivescm.escenter.cofig;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * Created by DongChunfu on 2017/8/15
// */
//@Configuration
//@Configurable
//public class ElasticsearchConfig {
//
//    @Value("${spring.data.elasticsearch.cluster-nodes}")
//    private String clusterNodes;
//    @Value("${spring.data.elasticsearch.cluster-name}")
//    private String clusterName;
//
//    @Value("${spring.data.elasticsearch.username}")
//    private String username;
//    @Value("${spring.data.elasticsearch.password}")
//    private String password;
//
//    @Bean
//    public TransportClient clientFactory() throws UnknownHostException {
//        final Settings settings = Settings.builder()
//                .put("cluster.name", clusterName)
//                .put("xpack.security.user", username + ":" + password)
//                .build();
//
////        TransportClient transportClient = new PreBuiltTransportClient(settings);
//        TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
//        final String[] addresses = clusterNodes.split(",");
//        for (String address : addresses) {
//            final String[] hostAndPort = address.split(":");
//            final InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
//                    InetAddress.getByName(hostAndPort[0]), Integer.valueOf(hostAndPort[1]));
//            transportClient.addTransportAddress(inetSocketTransportAddress);
//        }
//        return transportClient;
//
//    }
//}
