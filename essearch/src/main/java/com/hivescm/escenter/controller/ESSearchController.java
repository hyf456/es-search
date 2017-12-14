package com.hivescm.escenter.controller;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.*;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import com.hivescm.escenter.core.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by dongchunfu on 2017/7/18.
 * <p>
 * es 结构化查询 控制器
 */
@RestController
public class ESSearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESSearchController.class);

	@Value(value = "${escenter.save.mq.exchange}")
	private String exchange;

	@Value(value = "${escenter.save.mq.routekey}")
	private String routekey = "common-es-save";

	@Resource
	private ESSaveValidator esSaveValidator;

	@Resource
	private ESQueryValidator esQueryValidator;

	@Resource
	private ESUpdateValidator esUpdateValidator;

	@Resource
	private ESDeleteValidator esDeleteValidator;

	@Resource
	private ESBatchUpdateValidator esBatchUpdateValidator;

	@Resource
	private ESBatchSaveValidator esBatchSaveValidator;

	@Resource
	private ESBatchDeleteValidator esBatchDeleteValidator;

	@Resource
	private ESConditionUpdateValidator esConditionUpdateValidator;

	@Resource
	private ESConditionDeleteValidator esConditionDeleteValidator;

	@Resource
	private ESNestedSearchService esNestedSearchService;

//	@Resource
//	private RedisService redisService;
//
//	@Resource
//	private MQService mqService;

	@RequestMapping(value = "/esSave", method = RequestMethod.POST)
	public DataResult<Boolean> esSave(@RequestBody SaveESObject obj) {
		LOGGER.info("esclient index request param:{}.", obj);
		final DataResult<Boolean> dataResult = esSaveValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.save(obj);

		//		final String esSaveRedisKye = ESUtil.redisRepetitionKey(obj);
		//		final boolean needSendMq = redisService.needSendMq(esSaveRedisKye, obj);
		//		if (!needSendMq) {
		//			LOGGER.info("esclient repetition index request param:{}.", obj);
		//			dataResult.setStatus(new Status(ESErrorCode.REPETITION_INDEX_ERROR_CODE, "重复索引请求"));
		//			return dataResult;
		//		}
		//
		//		String objJson;
		//		try {
		//			objJson = ESSearchConvertor.object2Json(obj);
		//		} catch (IOException ex) {
		//			LOGGER.error("esclient  index  obj serialize to json error,param:" + obj, ex);
		//			dataResult.setStatus(new Status(ESErrorCode.ESCENTER_ERROR_CODE, "参数序列化json错误" + ex.getMessage()));
		//			return dataResult;
		//		}
		//
		//		try {
		//			final boolean sendMsg = mqService.sendMsg(exchange, routekey, objJson);
		//			if (!sendMsg) {
		//				LOGGER.info("esclient index send mq failed, param:{}.", obj);
		//				dataResult.setStatus(new Status(ESErrorCode.INDEX_SEND_MQ_ERROR_CODE, "发生MQ消息失败"));
		//				// TODO 若消息发送失败，回滚redis判重信息
		//				//redisService.deleteJudgeRepetitiveMq(esSaveRedisKye, obj);// 若消息发送失败，回滚redis判重信息
		//				return dataResult;
		//			}
		//
		//			LOGGER.info("es save, send mq successed, param:{}.", obj);
		//			dataResult.setResult(Boolean.TRUE);
		//			return dataResult;
		//		} catch (Exception ex) {
		//			LOGGER.error("es save error, send mq failed,param:" + obj, ex);
		//			dataResult.setStatus(new Status(ESErrorCode.ESCENTER_ERROR_CODE, "搜索引擎异常"));
		//			// TODO 若消息发送失败，回滚redis判重信息
		//			//redisService.deleteJudgeRepetitiveMq(esSaveRedisKye, obj);// 若消息发送失败，回滚redis判重信息
		//			return dataResult;
		//		}
	}

	@RequestMapping(value = "/esQuery", method = RequestMethod.POST)
	public DataResult<ESResponse> esQuery(@RequestBody final QueryESObject obj) {
		LOGGER.info("esclient query req param:{}.", obj);
		DataResult<ESResponse> dataResult = new DataResult<>();

		final DataResult<Boolean> validateResult = esQueryValidator.validate(obj);
		if (validateResult.isFailed()) {
			LOGGER.warn("esclient query req param error, illegal param:{}.", obj);
			dataResult.setStatus(validateResult.getStatus());
			return dataResult;
		}
		return esNestedSearchService.query(obj);
	}

	@RequestMapping(value = "/esDelete", method = RequestMethod.DELETE)
	public DataResult<Boolean> esDelete(@RequestBody final DeleteESObject obj) throws IOException {
		LOGGER.info("es delete , param:{}.", obj);
		final DataResult<Boolean> dataResult = esDeleteValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.delete(obj);
	}

	/**
	 * ES 更新
	 *
	 * @param obj 更新es通用请求参数
	 * @return
	 */
	@RequestMapping(value = "/esUpdate", method = RequestMethod.PUT)
	public DataResult<Boolean> esUpdate(@RequestBody final UpdateESObject obj) {
		LOGGER.info("es update , param:{}.", obj);

		final DataResult<Boolean> dataResult = esUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.update(obj);
	}

	@RequestMapping(value = "/esBatchSave", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchSave(@RequestBody final BatchSaveESObject obj) {
		LOGGER.info("es batch save , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchSaveValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchSave(obj);
	}

	@RequestMapping(value = "/esBatchUpdate", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchUpdate(@RequestBody final BatchUpdateESObject obj) {
		LOGGER.info("es batch update , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchUpdate(obj);
	}

	@RequestMapping(value = "/esBatchDelete", method = RequestMethod.PUT)
	public DataResult<Boolean> esBatchDelete(@RequestBody final BatchDeleteESObject obj) {
		LOGGER.info("es batch delete , param:{}.", obj);
		final DataResult<Boolean> dataResult = esBatchDeleteValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}

		return esNestedSearchService.batchDelete(obj);
	}

	@RequestMapping(value = "/conditionUpdate", method = RequestMethod.PUT)
	public DataResult<Boolean> conditionUpdate(@RequestBody final ConditionUpdateESObject obj) {
		LOGGER.info("es condition update , param:{}.", obj);
		final DataResult<Boolean> dataResult = esConditionUpdateValidator.validate(obj);
		if (dataResult.isFailed()) {
			return dataResult;
		}
		return esNestedSearchService.conditionUpdate(obj);
	}

	@RequestMapping(value = "/conditionDelete", method = RequestMethod.DELETE)
	public DataResult<Boolean> conditionDelete(@RequestBody final ConditionDeleteESObject obj) {
		LOGGER.info("es condition delete , param:{}.", obj);
		final DataResult<Boolean> dataResultResult = esConditionDeleteValidator.validate(obj);
		if (dataResultResult.isFailed()) {
			return dataResultResult;
		}

		return esNestedSearchService.conditionDelete(obj);
	}
}
