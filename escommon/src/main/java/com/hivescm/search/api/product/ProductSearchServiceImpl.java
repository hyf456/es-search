package com.hivescm.search.api.product;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.PageCondition;
import com.hivescm.escenter.common.enums.SortEnum;
import com.hivescm.escenter.core.handler.ESQueryResponseHandler;
import com.hivescm.search.log.SearchLogger;

@Component
public class ProductSearchServiceImpl implements ProductSearchService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchService.class);
	@Value(value = "${escenter.excute.explain:false}")
	private boolean explain;
	@Resource
	private TransportClient esClient;
	@Resource
	private ESQueryResponseHandler esQueryResponseHandler;

	@Override
	public DataResult<ESResponse> esInfoProductQuery(Map<String, Object> queryMap) {
		LOGGER.info("scm-info-productgoods1=" + queryMap);
		String esSystemName = (String) queryMap.get("esSystemName");
		String esIndexName = (String) queryMap.get("esIndexName");
		String esTypeName = (String) queryMap.get("esTypeName");
		if (StringUtils.isBlank(esSystemName)) {
			LOGGER.warn("ES 基础请求参数 esSystemName 为空。");
			return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【systemName】为空");
		}
		if (StringUtils.isBlank(esIndexName)) {
			LOGGER.warn("ES 基础请求参数 esIndexName 为空。");
			return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空");
		}
		if (StringUtils.isBlank(esTypeName)) {
			LOGGER.warn("ES 基础请求参数 esTypeName 为空。");
			return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【typeName】为空");
		}
		String name = (String) queryMap.get("name");
		String[] goodsTags = (String[]) queryMap.get("goodsTags");
		String[] goodsIds = (String[]) queryMap.get("goodsIds");
		String[] managementCategoryIds = (String[]) queryMap.get("managementCategoryIds");
		String[] brandIds = (String[]) queryMap.get("brandIds");
		Integer companyId = (Integer) queryMap.get("companyId");
		String[] state = (String[]) queryMap.get("state");
		Integer pageSize = (Integer) queryMap.get("pageSize");
		Integer pageNo = (Integer) queryMap.get("pageNo");
		String[] goodsState = (String[]) queryMap.get("goodsState");
		Map<String, SortEnum> sortMap = (Map<String, SortEnum>) queryMap.get("sortMap");
		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.isNoneBlank(name)) {
			QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("goods.goodsName.keyword", "*" + name + "*");
			boolQueryBuilder.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
			QueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("goods.goodsCode.keyword", "*" + name + "*");
			boolQueryBuilder1.must(QueryBuilders.nestedQuery("goods", queryBuilder1, ScoreMode.None));
			QueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery("goods.mnemonicCode.keyword", "*" + name + "*");
			boolQueryBuilder2.must(QueryBuilders.nestedQuery("goods", queryBuilder2, ScoreMode.None));
		}
		if (goodsTags != null && goodsTags.length != 0) {
			QueryBuilder queryBuilder = QueryBuilders.termsQuery("goods.goodsTags.keyword", goodsTags);
			boolQueryBuilder.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
			boolQueryBuilder1.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
			boolQueryBuilder2.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
		}
		if (goodsIds != null && goodsIds.length != 0) {
			QueryBuilder queryBuilder = QueryBuilders.termsQuery("goods.goodsIdStr.keyword", goodsIds);
			boolQueryBuilder.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
			boolQueryBuilder1.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
			boolQueryBuilder2.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
		}
		if (managementCategoryIds != null && managementCategoryIds.length != 0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery("managementCategoryId3", managementCategoryIds));
			boolQueryBuilder1.must(QueryBuilders.termsQuery("managementCategoryId3", managementCategoryIds));
			boolQueryBuilder2.must(QueryBuilders.termsQuery("managementCategoryId3", managementCategoryIds));
		}
		if (brandIds != null && brandIds.length != 0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery("brandIds", brandIds));
			boolQueryBuilder1.must(QueryBuilders.termsQuery("brandIds", brandIds));
			boolQueryBuilder2.must(QueryBuilders.termsQuery("brandIds", brandIds));
		}
		if (state != null && state.length != 0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery("state", state));
			boolQueryBuilder1.must(QueryBuilders.termsQuery("state", state));
			boolQueryBuilder2.must(QueryBuilders.termsQuery("state", state));
		}
		if (companyId != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery("companyId", companyId));
			boolQueryBuilder1.must(QueryBuilders.termQuery("companyId", companyId));
			boolQueryBuilder2.must(QueryBuilders.termQuery("companyId", companyId));
		}
		if (goodsState != null && goodsState.length != 0) {
		   QueryBuilder queryBuilder = QueryBuilders.termsQuery("goods.state", goodsState);
		   boolQueryBuilder.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
		   boolQueryBuilder1.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
		   boolQueryBuilder2.must(QueryBuilders.nestedQuery("goods", queryBuilder, ScoreMode.None));
		  }
		BoolQueryBuilder boolQueryBuilderAll = new BoolQueryBuilder();
		boolQueryBuilderAll.should(boolQueryBuilder);
		boolQueryBuilderAll.should(boolQueryBuilder1);
		boolQueryBuilderAll.should(boolQueryBuilder2);

		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch().setIndices(esIndexName).setTypes(esTypeName)
				.setExplain(explain);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if (sortMap != null && sortMap.size() > 0) {
			for (Map.Entry<String, SortEnum> entry : sortMap.entrySet()) {
				String fieldName = entry.getKey();
				SortEnum sort = entry.getValue();
				FieldSortBuilder order;
				switch (sort) {
				case ASC:
					order = SortBuilders.fieldSort(fieldName).order(SortOrder.ASC);
					break;
				case DESC:
					order = SortBuilders.fieldSort(fieldName).order(SortOrder.DESC);
					break;
				default:
					order = null;
				}
				searchSourceBuilder.sort(order);
			}
		} else {
			searchSourceBuilder.sort(SortBuilders.fieldSort("productId").order(SortOrder.ASC));
		}
		if (pageNo == null || pageNo <= 0) {
			pageNo = 1;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = 20;
		}
		searchRequestBuilder.setSource(searchSourceBuilder);
		searchRequestBuilder.setQuery(boolQueryBuilderAll);
		searchRequestBuilder.setFrom((pageNo - 1) * pageSize);
		searchRequestBuilder.setSize(pageSize);
		searchRequestBuilder.setExplain(true);
		SearchResponse response;
		try {
			LOGGER.info(esIndexName + "search: " + searchRequestBuilder);
			response = searchRequestBuilder.execute().actionGet();
		} catch (IndexNotFoundException infex) {
			SearchLogger.error("query", infex);
			return DataResult.faild(ESErrorCode.INDEX_NOT_EXIST_ERROR_CODE, "索引不存在");
		} catch (SearchPhaseExecutionException spex) {
			SearchLogger.error("query", spex);
			return DataResult.faild(ESErrorCode.QUERY_PHASE_ERROR_CODE, "搜索语法异常");
		} catch (Exception ex) {
			SearchLogger.error("query", ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "搜索引擎异常");
		}
		try {
			QueryESObject esObject = new QueryESObject();
			esObject.setSystemName(esSystemName);
			esObject.setIndexName(esIndexName);
			esObject.setTypeName(esTypeName);
			PageCondition pageCondition = new PageCondition();
			pageCondition.setCurrentPage(pageNo);
			pageCondition.setPageSize(pageSize);
			esObject.setPageCondition(pageCondition);
			final ESResponse handlerResponse = esQueryResponseHandler.handler(esObject, response);
			SearchLogger.log(handlerResponse);
			return DataResult.success(handlerResponse, ESResponse.class);
		} catch (Exception ex) {
			SearchLogger.error("query", ex);
			return DataResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "esMsg:" + ex.getMessage());
		}
	}

}
