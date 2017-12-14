package com.hivescm.escenter.evo;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ZHJ
 * @Date 2017/12/6
 */
@Component
public class ESClient {

    @Autowired
    private TransportClient client;

    public ESSearchRequestBuilder prepareSearch(String index, String type) {
        return new ESSearchRequestBuilder(client.prepareSearch(index).setTypes(type));
    }
}
