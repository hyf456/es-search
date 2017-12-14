package com.hivescm.escenter.core.condition;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.stereotype.Component;

import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.conditions.FunctionCondition;
import com.hivescm.escenter.common.conditions.GroupByCondition;
import com.hivescm.escenter.common.enums.SqlFunctionEnum;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * 分组条件 构建器
 */
@Component(value = "groupConditionBuilder")
public class GroupConditionBuilder {
	public SearchRequestBuilder build(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {

		final GroupByCondition groupByCondition = esObject.getGroupByCondition();
		if (groupByCondition == null) {
			return searchRequestBuilder;
		}
		final List<String> groupFields = groupByCondition.getGroupFields();
		if (groupFields == null) {
			return searchRequestBuilder;
		}

		AggregationBuilder rootTermsBuilder = null;// 根聚合嵌套使用
		AggregationBuilder termTermsBuilder = null;// 最底层嵌套聚合
		for (int i = 0; i < groupFields.size(); i++) {
			if (rootTermsBuilder == null) {
				rootTermsBuilder = AggregationBuilders.terms(groupFields.get(i))
						.field(groupFields.get(i));
			} else {
				if (null == termTermsBuilder || termTermsBuilder == rootTermsBuilder) {
					termTermsBuilder = AggregationBuilders.terms(groupFields.get(i)).field(groupFields.get(i));
					rootTermsBuilder.subAggregation(termTermsBuilder);
				} else {
					final AggregationBuilder currentTermsBuilder = AggregationBuilders.terms(groupFields.get(i))
							.field(groupFields.get(i))
							.minDocCount(0);
					termTermsBuilder.subAggregation(currentTermsBuilder);
					termTermsBuilder = currentTermsBuilder;
				}
			}
		}

		aggregationFunction(rootTermsBuilder, termTermsBuilder, groupByCondition.getFunctionConditions(),
				searchRequestBuilder);

		return searchRequestBuilder;
	}

	/**
	 * 封装查询请求的聚合条件
	 *
	 * @param rootTermsBuilder     聚合根
	 * @param termsBuilder         当前聚合
	 * @param functions            聚合函数集
	 * @param searchRequestBuilder 聚合功能查询请求
	 */
	private void aggregationFunction(AggregationBuilder rootTermsBuilder, AggregationBuilder termsBuilder,
			final List<FunctionCondition> functions,
			final SearchRequestBuilder searchRequestBuilder) {

		for (FunctionCondition functionCondition : functions) {

			AbstractAggregationBuilder aggregation=getAggregationFunction(functionCondition);

			if (termsBuilder != null) {
				termsBuilder.subAggregation(aggregation);
			} else {
				searchRequestBuilder.addAggregation(aggregation);
			}
		}
		if (termsBuilder != null) {
			searchRequestBuilder.addAggregation(rootTermsBuilder);
		}
	}

	public AbstractAggregationBuilder getAggregationFunction(FunctionCondition functionCondition){
		final String field = functionCondition.getField();
		final SqlFunctionEnum sqlFunction = functionCondition.getFunction();
		final String functionName = functionCondition.getFunctionName();
		AbstractAggregationBuilder aggregation;
		switch (sqlFunction) {
		case AVG:
			aggregation = AggregationBuilders.avg(functionName).field(field);
			break;
		case MAX:
			aggregation = AggregationBuilders.max(functionName).field(field);
			break;
		case MIN:
			aggregation = AggregationBuilders.min(functionName).field(field);
			break;
		case SUM:
			aggregation = AggregationBuilders.sum(functionName).field(field);
			break;
		case COUNT:
			aggregation = AggregationBuilders.count(functionName).field(field);
			break;
		default:
			throw new RuntimeException("不支持的聚合类型");
		}
		return aggregation;
	}
}
