package com.hivescm.escenter.core.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.common.conditions.FunctionCondition;
import com.hivescm.escenter.core.condition.CollapseConditonBuilder;
import com.hivescm.escenter.core.condition.GroupConditionBuilder;
import com.hivescm.escenter.core.condition.QueryConditionBuilder;
import com.hivescm.escenter.core.condition.SerchSourceBuilder;
import com.hivescm.escenter.core.handler.CollapseResponseHandler;
import com.hivescm.escenter.core.handler.ESQueryResponseHandler;
import com.hivescm.escenter.core.handler.ESStatisticResponseHandler;

/**
 * Created by DongChunfu on 2017/8/30
 * <p>
 * ES 聚合服务实现
 */
@Component(value = "esStatisticService")
public class ESStatisticServiceImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESStatisticServiceImpl.class);
	@Resource
	private QueryConditionBuilder queryConditionBuilder;

	@Resource
	private GroupConditionBuilder groupConditionBuilder;

	@Resource
	private ESStatisticResponseHandler esStatisticResponseHandler;

	@Resource
	private SerchSourceBuilder serchSourceBuilder;

	@Resource
	private CollapseConditonBuilder collapseConditonBuilder;

	@Resource
	private CollapseResponseHandler collapseResponseHandler;

	@Resource
	private ESQueryResponseHandler esQueryResponseHandler;

	@Resource
	private TransportClient client;

	public DataResult<Map<String, Number>> statisticByConditions(StatisticESObject esObject) {
		DataResult<Map<String, Number>> dataResult = new DataResult<>();

		final SearchRequestBuilder searchRequestBuilder = client.prepareSearch(esObject.getIndexName());
		// 构建查询条件
		searchRequestBuilder.setQuery(queryConditionBuilder.innerBuilder(esObject.getSearchConditions(), 1,
				esObject.getIndexName(), esObject.getTypeName()));

		// 设置聚合条件
		for (FunctionCondition functionCondition : esObject.getFunctionConditions()) {
			final AbstractAggregationBuilder aggregationFunction = groupConditionBuilder
					.getAggregationFunction(functionCondition);
			searchRequestBuilder.addAggregation(aggregationFunction);
		}

		SearchResponse searchResponse;
		try {
			LOGGER.debug("elastic statistic req param:{}.", searchRequestBuilder);
			searchResponse = searchRequestBuilder.execute().get();
			LOGGER.debug("elastic statistic ,rep param:{}, response:{}.", searchRequestBuilder, searchResponse);
		} catch (Exception ex) {
			LOGGER.error("elastic statistic error,msg :" + ex.getMessage(), ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "elastic error:" + ex.getMessage());
		}

		try {
			final Map<String, Number> statisticResult = esStatisticResponseHandler.handler(searchResponse.getAggregations());
			LOGGER.debug("escenter statistic response:{}.", statisticResult);
			dataResult.setResult(statisticResult);
			return dataResult;
		} catch (Exception ex) {
			LOGGER.error("escenter statistic error,msg :" + ex.getMessage(), ex);
			return DataResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "escenter error:" + ex.getMessage());
		}
	}

	public DataResult<Map<String, ESResponse>> collapse(final CollapseQueryObject esObject) {
		DataResult<Map<String, ESResponse>> dataResult = new DataResult<>();

		final SearchRequestBuilder searchRequestBuilder = client.prepareSearch().setIndices(esObject.getIndexName());
		if (StringUtils.isNotBlank(esObject.getTypeName())) {
			searchRequestBuilder.setTypes(esObject.getTypeName());
		}

		final SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
		serchSourceBuilder.page(esObject.getPageCondition(), searchSourceBuilder);
		serchSourceBuilder.sort(esObject.getOrderConditions(), searchSourceBuilder);
		searchSourceBuilder.fetchSource(Boolean.TRUE);
		searchRequestBuilder.setSource(searchSourceBuilder);

		// 需要在 SearchSourceBuilder 之后添加
		final BoolQueryBuilder queryBuilder = queryConditionBuilder.innerBuilder(esObject.getSearchConditions(),
				esObject.getVersion(), esObject.getIndexName(), esObject.getTypeName());
		searchRequestBuilder.setQuery(queryBuilder);

		final CollapseBuilder collapseBuilder = collapseConditonBuilder.builde(searchRequestBuilder, esObject);
		searchRequestBuilder.setCollapse(collapseBuilder);

		SearchResponse searchResponse;
		try {
			LOGGER.debug("elastic collapse ,rep param:{}.", searchRequestBuilder);
			searchResponse = searchRequestBuilder.execute().get();
			LOGGER.debug("elastic collapse ,rep param:{}, response:{}.", searchRequestBuilder, searchResponse);
		} catch (Exception ex) {
			LOGGER.error("elastic collapse error,msg :" + ex.getMessage(), ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "elastic error:" + ex.getMessage());
		}

		try {
			final ESResponse handlerResponse = esQueryResponseHandler.handler(esObject, searchResponse);
			final Map<String, ESResponse> handlerResult = collapseResponseHandler.handler(searchResponse, esObject);
			handlerResult.put("searchResult", handlerResponse);
			dataResult.setResult(handlerResult);
			LOGGER.debug("escenter collapse response , req param:{}, response:{}.", esObject, dataResult);
			return dataResult;
		} catch (Exception ex) {
			LOGGER.error("escenter collapse response handle error,msg :" + ex.getMessage(), ex);
			return DataResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "escenter error:" + ex.getMessage());
		}
	}
}
