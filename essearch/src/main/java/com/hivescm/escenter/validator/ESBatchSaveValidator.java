//package com.hivescm.escenter.validator;
//
//import com.hivescm.common.domain.DataResult;
//import com.hivescm.common.domain.Status;
//import com.hivescm.escenter.ESErrorCode;
//import com.hivescm.escenter.common.BatchSaveESObject;
//import com.hivescm.escenter.common.SaveESObject;
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
// * 批量新增请求参数校验器
// */
//@Component(value = "esBatchSaveValidator")
//public class ESBatchSaveValidator extends BaseValidator {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ESBatchSaveValidator.class);
//
//	@Resource
//	private ESSaveValidator esSaveValidator;
//
//	public DataResult<Boolean> validate(BatchSaveESObject obj) {
//		DataResult<Boolean> dataResult = new DataResult<>();
//		if (null == obj) {
//			LOGGER.warn("批量新增请求参数为空.");
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量新增请求参数为空"));
//		}
//		final List<SaveESObject> saveDatas = obj.getSaveDatas();
//		if (CollectionUtil.isEmpty(saveDatas)) {
//			LOGGER.warn("批量新增[saveDatas]数据为空.");
//			dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量新增请求参数【saveDatas】为空"));
//		}
//
//		if (dataResult.isFailed()) {
//			return dataResult;
//		}
//
//		for (SaveESObject saveData : saveDatas) {
//			final DataResult<Boolean> validateResult = esSaveValidator.validate(saveData);
//			if (validateResult.isFailed()) {
//				return validateResult;
//			}
//		}
//
//		return dataResult;
//	}
//}
