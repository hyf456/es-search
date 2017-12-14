//package com.hivescm.escenter.validator;
//
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.common.domain.Status;
//import com.hivescm.escenter.ESErrorCode;
//import com.hivescm.escenter.common.ConditionDeleteESObject;
//import com.hivescm.escenter.common.conditions.SearchCondition;
//import com.hivescm.escenter.util.CollectionUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * Created by DongChunfu on 2017/8/16
// * <p>
// * ES 按条件删除请求参数校验器
// */
//@Component(value = "esConditionDeleteValidator")
//public class ESConditionDeleteValidator {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ESConditionUpdateValidator.class);
//
//	@Resource
//	private ESDeleteValidator esDeleteValidator;
//
//	public DataResult<Boolean> validate(ConditionDeleteESObject obj) {
//		DataResult<Boolean> dataResult = new DataResult<>();
//		if (null == obj) {
//			LOGGER.warn("条件删除请求参数为空.");
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件删除请求参数为空"));
//		}
//
//		final List<SearchCondition> conditions = obj.getConditions();
//		if (CollectionUtil.isEmpty(conditions)) {
//			LOGGER.warn("条件删除[conditions]数据为空.");
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件删除请求参数【conditions】为空"));
//		}
//
//		if (dataResult.isFailed()) {
//			return dataResult;
//		}
//
//		return esDeleteValidator.validate(obj);
//	}
//}
