package com.hivescm.escenter.core.validator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.NestedESObject;
import com.hivescm.escenter.common.UpdateESObject;
import com.hivescm.escenter.common.enums.OperateTypeEnum;

/**
 * Created by DongChunfu on 2017/8/19
 */
@Component(value = "baseUpdateValidator")
public class BaseUpdateValidator extends BaseValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUpdateValidator.class);

    public DataResult<Boolean> validate(UpdateESObject obj) {

        final DataResult<Boolean> baseValidateResult = super.baseValidate(obj);
        if (baseValidateResult.isFailed()) {
            LOGGER.warn("es base request param error ,systemName or indexName or typeName should not null!");
            return baseValidateResult;
        }

        final Map<Object, Object> dataMap = obj.getDataMap();
        if (dataMap == null) {
            LOGGER.warn("update es object ,dataMap should not null!");
            baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【dataMap】为空"));
        }

        final NestedESObject nestedESObject = obj.getNestedESObject();
        final OperateTypeEnum nestedOperateType = obj.getNestedOperateType();

        if (nestedESObject != null) {
            final String fieldName = nestedESObject.getFieldName();
            if (fieldName == null) {
                LOGGER.warn("nested update es object ,fieldName should not null!");
                baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "嵌套更新请求参数【fieldName】为空"));
            }

            if (nestedOperateType == null) {
                LOGGER.warn("nested update es object ,operate type should not null!");
                baseValidateResult
                        .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "嵌套更新请求参数【nestedOperateType】为空"));
            }

            if (!nestedESObject.isList()) {
                final NestedESObject nextNestedESObject = nestedESObject.getNextNestedESObject();
                if (nextNestedESObject == null) {
                    LOGGER.warn("nested update es ool object ,nextNestedESObject should not null!");
                    baseValidateResult
                            .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                    "嵌套更新顶级非集合请求参数【nextNestedESObject】为空"));
                }
            }

        }
        return baseValidateResult;
    }
}
