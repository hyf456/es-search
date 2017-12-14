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
import com.hivescm.escenter.common.BatchDeleteESObject;
import com.hivescm.escenter.common.DeleteESObject;

/**
 * Created by DongChunfu on 2017/8/16
 * <p>
 * 批量删除请求参数校验器
 */
@Component(value = "esBatchDeleteValidator")
public class ESBatchDeleteValidator extends BaseValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESBatchDeleteValidator.class);

	@Resource
	private ESDeleteValidator esDeleteValidator;

	public DataResult<Boolean> validate(BatchDeleteESObject obj) {
		DataResult<Boolean> dataResult = new DataResult<>();
		if (null == obj) {
			LOGGER.warn("批量删除请求参数为空.");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量删除请求参数为空"));
		}
		final List<DeleteESObject> deleteDatas = obj.getDeleteDatas();
		if (CollectionUtils.isEmpty(deleteDatas)) {
			LOGGER.warn("批量删除[deleteDatas]数据为空.");
			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量删除请求参数【deleteDatas】为空"));
		}

		for (DeleteESObject deleteData : deleteDatas) {
			final DataResult<Boolean> validateResult = esDeleteValidator.validate(deleteData);
			if (validateResult.isFailed()) {
				return validateResult;
			}
		}
		return dataResult;
	}
}
