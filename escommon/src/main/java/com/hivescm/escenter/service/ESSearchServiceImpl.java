package com.hivescm.escenter.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.BatchDeleteESObject;
import com.hivescm.escenter.common.BatchSaveESObject;
import com.hivescm.escenter.common.BatchUpdateESObject;
import com.hivescm.escenter.common.ConditionDeleteESObject;
import com.hivescm.escenter.common.ConditionUpdateESObject;
import com.hivescm.escenter.common.DeleteESObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.QueryESObject;
import com.hivescm.escenter.common.SaveESObject;
import com.hivescm.escenter.common.UpdateESObject;
import com.hivescm.escenter.core.service.ESNestedSearchService;
import com.hivescm.escenter.core.validator.ESBatchDeleteValidator;
import com.hivescm.escenter.core.validator.ESBatchSaveValidator;
import com.hivescm.escenter.core.validator.ESBatchUpdateValidator;
import com.hivescm.escenter.core.validator.ESConditionDeleteValidator;
import com.hivescm.escenter.core.validator.ESConditionUpdateValidator;
import com.hivescm.escenter.core.validator.ESDeleteValidator;
import com.hivescm.escenter.core.validator.ESQueryValidator;
import com.hivescm.escenter.core.validator.ESSaveValidator;
import com.hivescm.escenter.core.validator.ESUpdateValidator;

/**
 * @Author ZHJ
 * @Date 2017/12/5
 */
@Component
public class ESSearchServiceImpl implements ESSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESSearchServiceImpl.class);

    @Autowired
    private ESSaveValidator esSaveValidator;

    @Autowired
    private ESQueryValidator esQueryValidator;

    @Autowired
    private ESDeleteValidator esDeleteValidator;

    @Autowired
    private ESUpdateValidator esUpdateValidator;

    @Autowired
    private ESBatchSaveValidator esBatchSaveValidator;

    @Autowired
    private ESBatchUpdateValidator esBatchUpdateValidator;

    @Autowired
    private ESBatchDeleteValidator esBatchDeleteValidator;

    @Autowired
    private ESConditionUpdateValidator esConditionUpdateValidator;

    @Autowired
    private ESConditionDeleteValidator esConditionDeleteValidator;

    @Resource
    private ESNestedSearchService esNestedSearchService;

    @Override
    public DataResult<Boolean> esSave(SaveESObject obj) {
        LOGGER.info("esclient index request param:{}.", obj);
        final DataResult<Boolean> dataResult = esSaveValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.save(obj);
    }

    @Override
    public DataResult<ESResponse> esQuery(QueryESObject obj) {
        LOGGER.info("esclient query req param:{}.", obj);
        final DataResult<Boolean> validateResult = esQueryValidator.validate(obj);
        if (validateResult.isFailed()) {
            LOGGER.warn("esclient query req param error, illegal param:{}.", obj);
            DataResult<ESResponse> dataResult = new DataResult<>();
            dataResult.setStatus(validateResult.getStatus());
            return dataResult;
        }
        return esNestedSearchService.query(obj);
    }

    @Override
    public DataResult<Boolean> esDelete(DeleteESObject obj) {
        LOGGER.info("es delete , param:{}.", obj);
        final DataResult<Boolean> dataResult = esDeleteValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.delete(obj);
    }

    @Override
    public DataResult<Boolean> esUpdate(UpdateESObject obj) {
        LOGGER.info("es update , param:{}.", obj);
        final DataResult<Boolean> dataResult = esUpdateValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.update(obj);
    }

    @Override
    public DataResult<Boolean> esBatchSave(BatchSaveESObject obj) {
        LOGGER.info("es batch save , param:{}.", obj);
        final DataResult<Boolean> dataResult = esBatchSaveValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.batchSave(obj);
    }

    @Override
    public DataResult<Boolean> esBatchUpdate(BatchUpdateESObject obj) {
        LOGGER.info("es batch update , param:{}.", obj);
        final DataResult<Boolean> dataResult = esBatchUpdateValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.batchUpdate(obj);
    }

    @Override
    public DataResult<Boolean> esBatchDelete(BatchDeleteESObject obj) {
        LOGGER.info("es batch delete , param:{}.", obj);
        final DataResult<Boolean> dataResult = esBatchDeleteValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.batchDelete(obj);
    }

    @Override
    public DataResult<Boolean> conditionUpdate(ConditionUpdateESObject obj) {
        LOGGER.info("es condition update , param:{}.", obj);
        final DataResult<Boolean> dataResult = esConditionUpdateValidator.validate(obj);
        if (dataResult.isFailed()) {
            return dataResult;
        }
        return esNestedSearchService.conditionUpdate(obj);
    }

    @Override
    public DataResult<Boolean> conditionDelete(ConditionDeleteESObject obj) {
        LOGGER.info("es condition delete , param:{}.", obj);
        final DataResult<Boolean> dataResultResult = esConditionDeleteValidator.validate(obj);
        if (dataResultResult.isFailed()) {
            return dataResultResult;
        }
        return esNestedSearchService.conditionDelete(obj);
    }
}
