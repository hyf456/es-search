package com.hivescm.escenter.core.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;

/**
 * Created by DongChunfu on 2017/9/5
 * <p>
 * 检索条件校验器
 */
@Component(value = "searchConditionValidator")
public class SearchConditionValidator {
    public DataResult<Boolean> validator(final List<SearchCondition> searchConditions) {
        if (CollectionUtils.isEmpty(searchConditions)) {
            return DataResult.success(Boolean.TRUE, Boolean.class);
        }

        for (SearchCondition searchCondition : searchConditions) {
            final Object[] fieldValues = searchCondition.getFieldValues();
            final String singleValue =
                    searchCondition.getSingleValue() == null ? "" : String.valueOf(searchCondition.getSingleValue());

            final String minValue = searchCondition.getMinValue();
            final String maxValue = searchCondition.getMaxValue();

            final ConditionExpressionEnum conditionExpression = searchCondition.getConditionExpression();

            switch (conditionExpression) {
                case NOT_IN:
                    if (fieldValues == null || fieldValues.length == 0) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【NOT_IN】时【fieldValues】不得为空");
                    }
                    break;
                case IN:
                    if (fieldValues == null || fieldValues.length == 0) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【IN】时【fieldValues】不得为空");
                    }
                    break;
                case LIKE:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LIKE】时【singleValue】不得为空");
                    }
                    break;
                case LESSER:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LESSER】时【singleValue】不得为空");
                    }
                    break;
                case UNEQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【UNEQUAL】时【singleValue】不得为空");
                    }
                    break;
                case GREATER:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【GREATER】时【singleValue】不得为空");
                    }
                    break;
                case LESSER_OR_EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LESSER_OR_EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【GREATER_OR_EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case BETWEEN_RIGHR:
                    if (StringUtils.isBlank(minValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_RIGHR】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_RIGHR】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN:
                    if (StringUtils.isBlank(minValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN_LEFT:
                    if (StringUtils.isBlank(minValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_LEFT】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_LEFT】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN_AND:
                    if (StringUtils.isBlank(minValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_AND】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_AND】时【maxValue】不得为空");
                    }
                    break;
                case NULL:
                    break;
                case NOT_NULL:
                    break;
                case MATCH:
                    if (StringUtils.isBlank(singleValue)) {
                        return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【MATCH】时【singleValue】不得为空");
                    }
                    break;
                default:
                    return DataResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                            "搜索表达式暂不支持");
            }
        }
        return DataResult.success(Boolean.TRUE, Boolean.class);
    }
}
