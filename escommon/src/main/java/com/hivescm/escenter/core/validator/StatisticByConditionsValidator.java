package com.hivescm.escenter.core.validator;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.common.conditions.FunctionCondition;

/**
 * Created by DongChunfu on 2017/8/31
 * <p>
 * 统计服务请求参数校验器
 */
@Component(value = "statisticByConditionsValidator")
public class StatisticByConditionsValidator {
    public DataResult<Map<String, Number>> validate(StatisticESObject obj) {
        DataResult<Map<String, Number>> dataResult = new DataResult<>();

        if (obj == null) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，请求参数不得为空"));
        }

        final String systemName = obj.getSystemName();
        if (StringUtils.isBlank(systemName)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【systemName】不得为空"));
        }

        final String indexName = obj.getIndexName();
        if (StringUtils.isBlank(indexName)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【indexName】不得为空"));
        }

        final List<FunctionCondition> functionConditions = obj.getFunctionConditions();
        if (CollectionUtils.isEmpty(functionConditions)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【functionConditions】不得为空"));
        }

        return dataResult;
    }
}
