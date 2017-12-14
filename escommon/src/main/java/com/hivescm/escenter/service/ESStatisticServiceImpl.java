package com.hivescm.escenter.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.core.validator.CollapseQueryValidator;
import com.hivescm.escenter.core.validator.StatisticByConditionsValidator;

/**
 * @Author ZHJ
 * @Date 2017/12/8
 */
@Component
public class ESStatisticServiceImpl implements ESStatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESStatisticServiceImpl.class);

    @Autowired
    private StatisticByConditionsValidator statisticByConditionsValidator;

    @Autowired
    private CollapseQueryValidator collapseQueryValidator;

    @Autowired
    private com.hivescm.escenter.core.service.ESStatisticServiceImpl esStatisticService;

    @Override
    public DataResult<Map<String, Number>> statisticByConditions(StatisticESObject esObject) {
        LOGGER.info("statistic service request param:{}.", esObject);
        final DataResult<Map<String, Number>> validateResult = statisticByConditionsValidator.validate(esObject);
        if (validateResult.isFailed()) {
            return validateResult;
        }
        return esStatisticService.statisticByConditions(esObject);
    }

    @Override
    public DataResult<Map<String, ESResponse>> collapse(CollapseQueryObject esObject) {
        LOGGER.info("collapse service request param:{}.", esObject);

        final DataResult<Boolean> validateResult = collapseQueryValidator.validator(esObject);
        if (validateResult.isFailed()) {
            final DataResult<Map<String, ESResponse>> dataResult = new DataResult<>();
            dataResult.setStatus(validateResult.getStatus());
            return dataResult;
        }
        return esStatisticService.collapse(esObject);
    }
}
