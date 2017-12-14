package com.hivescm.escenter.controller;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.StatisticESObject;
import com.hivescm.escenter.core.validator.CollapseQueryValidator;
import com.hivescm.escenter.core.validator.StatisticByConditionsValidator;
import com.hivescm.escenter.core.service.ESStatisticServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by DongChunfu on 2017/8/30
 * <p>
 * ES 统计服务控制器
 */
@RestController
public class ESStatisticController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESStatisticController.class);

    @Resource
    private StatisticByConditionsValidator statisticByConditionsValidator;

    @Resource
    private CollapseQueryValidator collapseQueryValidator;

    @Resource
    private ESStatisticServiceImpl esStatisticService;

    @RequestMapping(value = "/statisticByConditions", method = RequestMethod.POST)
    public DataResult<Map<String, Number>> statisticByConditions(@RequestBody StatisticESObject esObject) {
        LOGGER.info("statistic service request param:{}.", esObject);

        final DataResult<Map<String, Number>> validateResult = statisticByConditionsValidator.validate(esObject);
        if (validateResult.isFailed()) {
            return validateResult;
        }

        return esStatisticService.statisticByConditions(esObject);
    }

    @RequestMapping(value = "/collapse", method = RequestMethod.POST)
    public DataResult<Map<String, ESResponse>> collapse(@RequestBody CollapseQueryObject esObject) {
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
