//package com.hivescm.escenter.validator;
//
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.escenter.common.QueryESObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * Created by DongChunfu on 2017/7/27
// * 查询请求参数校验器
// */
//@Component(value = "esQueryValidator")
//public class ESQueryValidator extends BaseValidator {
//
//	@Resource
//	private SearchConditionValidator searchConditionValidator;
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryValidator.class);
//
//	public DataResult<Boolean> validate(QueryESObject obj) {
//
//		final DataResult<Boolean> baseValidateResult = super.baseValidate(obj);
//
//		if (baseValidateResult.isFailed()) {
//			return baseValidateResult;
//		}
//
//		final DataResult<Boolean> searchConditionValidatorResult = searchConditionValidator
//				.validator(obj.getSearchConditions());
//
//		return searchConditionValidatorResult;
//	}
//}
