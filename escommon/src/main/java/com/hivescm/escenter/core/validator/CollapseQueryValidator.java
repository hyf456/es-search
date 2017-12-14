package com.hivescm.escenter.core.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.conditions.InnerHitsCondition;

/**
 * Created by DongChunfu on 2017/9/7
 * <p>
 * 瓦解查询请求参数校验器
 */
@Component(value = "collapseQueryValidator")
public class CollapseQueryValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollapseQueryValidator.class);

    public DataResult<Boolean> validator(CollapseQueryObject esObject) {
        DataResult<Boolean> dataResult = new DataResult<>();

        if (null == esObject) {
            LOGGER.warn("瓦解查询请求参数为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数为空"));
        }

        final String systemName = esObject.getSystemName();
        if (StringUtils.isBlank(systemName)) {
            LOGGER.warn("瓦解查询请求参数【systemName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【systemName】为空"));
        }

        final String indexName = esObject.getIndexName();
        if (StringUtils.isBlank(indexName)) {
            LOGGER.warn("瓦解查询请求参数【indexName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空"));
        }

        final String fieldName = esObject.getFieldName();
        if (StringUtils.isBlank(fieldName)) {
            LOGGER.warn("瓦解查询请求参数【fieldName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【fieldName】为空"));
        }

        final List<InnerHitsCondition> innerHitsConditions = esObject.getInnerHitsConditions();
        if (CollectionUtils.isEmpty(innerHitsConditions)) {
            LOGGER.warn("瓦解查询请求参数【innerHitsConditions】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【innerHitsConditions】为空"));
        }

        for (InnerHitsCondition innerHitsCondition : innerHitsConditions) {
            final String hitName = innerHitsCondition.getHitName();
            if (StringUtils.isBlank(hitName)) {
                LOGGER.warn("瓦解查询请求参数【InnerHitsCondition#hitName】为空");
                dataResult
                        .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【InnerHitsCondition#hitName】为空"));
            }
        }

        return dataResult;
    }
}
