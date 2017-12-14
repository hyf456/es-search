package com.hivescm.escenter.core.validator;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.BatchUpdateESObject;
import com.hivescm.escenter.common.UpdateESObject;

/**
 * Created by DongChunfu on 2017/8/16
 * <p>
 * 批量更新请求参数校验器
 */
@Component(value = "esBatchUpdateValidator")
public class ESBatchUpdateValidator extends BaseValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESBatchUpdateValidator.class);

    @Resource
    private ESUpdateValidator esUpdateValidator;

    public DataResult<Boolean> validate(BatchUpdateESObject obj) {
        DataResult<Boolean> dataResult = new DataResult<>();
        if (null == obj) {
            LOGGER.warn("批量更新请求参数为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量更新请求参数为空"));
        }
        final List<UpdateESObject> updateDatas = obj.getUpdateDatas();
        if (CollectionUtils.isEmpty(updateDatas)) {
            LOGGER.warn("批量更新[updateDatas]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量更新请求参数【updateDatas】为空"));
        }

        if (dataResult.isFailed()) {
            return dataResult;
        }

        for (UpdateESObject updateData : updateDatas) {
            final DataResult<Boolean> validateResult = esUpdateValidator.validate(updateData);
            if (validateResult.isFailed()) {
                return validateResult;
            }
        }

        return dataResult;
    }
}
