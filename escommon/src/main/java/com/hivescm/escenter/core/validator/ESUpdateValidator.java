package com.hivescm.escenter.core.validator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.UpdateESObject;

/**
 * Created by DongChunfu on 2017/7/27
 * <p>
 * 更新请求参数校验器
 */
@Component(value = "esUpdateValidator")
public class ESUpdateValidator extends BaseUpdateValidator {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESUpdateValidator.class);
	public DataResult<Boolean> validate(UpdateESObject obj) {
		final DataResult<Boolean> superValidateResult = super.validate(obj);

		if (superValidateResult.isFailed()) {
			return superValidateResult;
		}

		final Map<Object, Object> ukMap = obj.getUkMap();
		if (ukMap == null || ukMap.isEmpty()) {
			LOGGER.warn("update es object ,ukMap should not null!");
			superValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"更新ES,ukMap为空"));
		}
		return superValidateResult;
	}
}
