//package com.hivescm.escenter.validator;
//
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.escenter.ESErrorCode;
//import com.hivescm.escenter.common.conditions.SearchCondition;
//import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
//import com.hivescm.escenter.util.CollectionUtil;
//import com.hivescm.escenter.util.StringUtil;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * Created by DongChunfu on 2017/9/5
// * <p>
// * 检索条件校验器
// */
//@Component(value = "searchConditionValidator")
//public class SearchConditionValidator {
//	public DataResult<Boolean> validator(final List<SearchCondition> searchConditions) {
//		if (CollectionUtil.isEmpty(searchConditions)) {
//			return DataResult.success(Boolean.TRUE, Boolean.class);
//		}
//
//		for (SearchCondition searchCondition : searchConditions) {
//			final String[] fieldValues = searchCondition.getFieldValues();
//			final String singleValue = searchCondition.getSingleValue();
//
//			final String minValue = searchCondition.getMinValue();
//			final String maxValue = searchCondition.getMaxValue();
//
//			final ConditionExpressionEnum conditionExpression = searchCondition.getConditionExpression();
//
//			switch (conditionExpression) {
//			case NOT_IN:
//				if (fieldValues == null || fieldValues.length == 0) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【NOT_IN】时【fieldValues】不得为空");
//				}
//				break;
//			case IN:
//				if (fieldValues == null || fieldValues.length == 0) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【IN】时【fieldValues】不得为空");
//				}
//				break;
//			case LIKE:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【LIKE】时【singleValue】不得为空");
//				}
//				break;
//			case LESSER:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【LESSER】时【singleValue】不得为空");
//				}
//				break;
//			case UNEQUAL:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【UNEQUAL】时【singleValue】不得为空");
//				}
//				break;
//			case GREATER:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【GREATER】时【singleValue】不得为空");
//				}
//				break;
//			case LESSER_OR_EQUAL:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【LESSER_OR_EQUAL】时【singleValue】不得为空");
//				}
//				break;
//			case GREATER_OR_EQUAL:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【GREATER_OR_EQUAL】时【singleValue】不得为空");
//				}
//				break;
//			case EQUAL:
//				if (StringUtil.emptyString(singleValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【EQUAL】时【singleValue】不得为空");
//				}
//				break;
//			case BETWEEN_RIGHR:
//				if (StringUtil.emptyString(minValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_RIGHR】时【minValue】不得为空");
//				}
//				if (StringUtil.emptyString(maxValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_RIGHR】时【maxValue】不得为空");
//				}
//				break;
//			case BETWEEN:
//				if (StringUtil.emptyString(minValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN】时【minValue】不得为空");
//				}
//				if (StringUtil.emptyString(maxValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN】时【maxValue】不得为空");
//				}
//				break;
//			case BETWEEN_LEFT:
//				if (StringUtil.emptyString(minValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_LEFT】时【minValue】不得为空");
//				}
//				if (StringUtil.emptyString(maxValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_LEFT】时【maxValue】不得为空");
//				}
//				break;
//			case BETWEEN_AND:
//				if (StringUtil.emptyString(minValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_AND】时【minValue】不得为空");
//				}
//				if (StringUtil.emptyString(maxValue)) {
//					return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//							"查询请求参数为【BETWEEN_AND】时【maxValue】不得为空");
//				}
//				break;
//			case NULL:
//				break;
//			case NOT_NULL:
//				break;
//			default:
//				return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
//						"搜索表达式暂不支持");
//			}
//		}
//		return DataResult.success(Boolean.TRUE, Boolean.class);
//	}
//}
