package com.hivescm.escenter.core.condition;

import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.core.config.IndexAdmin;
import com.hivescm.escenter.core.config.IndexHelper;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * 检索条件构建器
 */
@Component(value = "queryConditionBuilder")
public class QueryConditionBuilder {

	@Resource(name = "searchConditionBuilder")
	private SearchConditionBuilder searchConditionBuilder;

	@Resource(name = "relationConditionBuilder")
	private RelationConditionBuilder relationConditionBuilder;
	@Autowired
	private IndexAdmin indexAdmin;

	public void builde(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {
		BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

		QueryESObject termQuery = esObject;
		do {
			final BoolQueryBuilder currentQuery = innerBuilder(termQuery.getSearchConditions(), 1, esObject.getIndexName(),
					esObject.getTypeName());

			relationConditionBuilder.builder(esObject.groupCondition(), currentQuery, rootQuery);

			termQuery = termQuery.getNextGroupQuery();
		} while (termQuery != null);

		searchRequestBuilder.setQuery(rootQuery);
	}

	/**
	 * 根据查询条件构建 QueryBuilder
	 *
	 * @param currentConditions 检索条件
	 * @param version 查询版本
	 * @return 查询条件
	 */
	public BoolQueryBuilder innerBuilder(final List<SearchCondition> currentConditions, Integer version, String indexName,
			String typeName) {
		final BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();
		IndexHelper helper = indexAdmin.getIndexHelper(indexName, typeName);
		if (CollectionUtils.isEmpty(currentConditions)) {
			return rootQuery;
		}

		if (version == 1) {
			for (SearchCondition currentCondition : currentConditions) {
				QueryBuilder currentQuery = searchConditionBuilder.builder(currentCondition, helper);
				relationConditionBuilder.builder(Boolean.FALSE, currentQuery, rootQuery);// 同一条件集合内均为
																																									// and
																																									// 链接
			}
		}

		if (version == 2) {
			for (SearchCondition currentCondition : currentConditions) {
				QueryBuilder query = searchConditionBuilder.builderVersion2(currentCondition, QueryBuilders.boolQuery(),
						helper);
				relationConditionBuilder.builder(Boolean.TRUE, query, rootQuery);// 同一条件集合内均为
																																					// and
																																					// 链接
			}
		}
		return rootQuery;
	}
}
