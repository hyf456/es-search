package com.hivescm.escenter.core.validator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.SaveESObject;

/**
 * Created by DongChunfu on 2017/7/27
 * 索引或更新请求参数校验器
 */
@Component(value = "esSaveValidator")
public class ESSaveValidator extends BaseValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESSaveValidator.class);

	public DataResult<Boolean> validate(SaveESObject obj) {

		final DataResult<Boolean> baseValidateResult = super.baseValidate(obj);

		if (baseValidateResult.isFailed()) {
			return baseValidateResult;
		}

		final Map<?, ?> dataMap = obj.getDataMap();
		if (dataMap == null || dataMap.isEmpty()) {
			LOGGER.warn("save es object ,dataMap is null!");
			baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"存储ES, dataMap 为空"));
		}

		return baseValidateResult;
	}
}
