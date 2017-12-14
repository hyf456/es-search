//package com.hivescm.escenter.validator;
//
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.common.domain.Status;
//import com.hivescm.escenter.ESErrorCode;
//import com.hivescm.escenter.common.StatisticESObject;
//import com.hivescm.escenter.common.UpdateESObject;
//import com.hivescm.escenter.common.conditions.FunctionCondition;
//import com.hivescm.escenter.util.CollectionUtil;
//import com.hivescm.escenter.util.StringUtil;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by DongChunfu on 2017/8/31
// *
// * 统计服务请求参数校验器
// */
//@Component(value = "statisticByConditionsValidator")
//public class StatisticByConditionsValidator {
//	public DataResult<Map<String, Number>> validate(StatisticESObject obj) {
//		DataResult<Map<String, Number>> dataResult = new DataResult<>();
//
//		if (obj == null) {
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"统计服务，请求参数不得为空"));
//		}
//
//		final String systemName = obj.getSystemName();
//		if (StringUtil.emptyString(systemName)) {
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"统计服务，【systemName】不得为空"));
//		}
//
//		final String indexName = obj.getIndexName();
//		if (StringUtil.emptyString(indexName)) {
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"统计服务，【indexName】不得为空"));
//		}
//
//		final List<FunctionCondition> functionConditions = obj.getFunctionConditions();
//		if (CollectionUtil.isEmpty(functionConditions)) {
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,"统计服务，【functionConditions】不得为空"));
//		}
//
//		return dataResult;
//	}
//}
