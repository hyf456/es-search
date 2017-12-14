package com.hivescm.controller;

import com.google.common.collect.Lists;
import com.hivescm.Application;
import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.*;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.common.enums.ConditionExpressionEnum;
import com.hivescm.escenter.common.enums.OperateTypeEnum;
import com.hivescm.escenter.controller.ESSearchController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.*;

/**
 * Created by DongChunfu on 2017/8/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ESDeleteTest {

    private static final String SYSTEM_NAME = "ES";
    private static final String INDEX_NAME = "escenter";
    private static final String TYPE_NAME = "test";
    private DeleteESObject deleteESObject = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
    @Autowired
    private ESSearchController esSearchController;

    @Test
    public void test() {
        SaveESObject object = new SaveESObject();
        object.setSystemName(SYSTEM_NAME);
        object.setIndexName(INDEX_NAME);
        object.setTypeName(TYPE_NAME);

        Map<Object, Object> ukMap = new HashMap<>();
        Random r = new Random();
        ukMap.put("id", 1000);
        object.setUkMap(ukMap);

        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("age", 23);
        dataMap.put("name", "大北京");
        dataMap.put("hobby", new String[]{"电影", "3C", "音乐"});
        object.setDataMap(dataMap);
        DataResult<Boolean> dataResult = esSearchController.esSave(object);
        if (dataResult.isSuccess()) {
            {
                QueryESObject queryESObject = new QueryESObject();
                queryESObject.setIndexName(INDEX_NAME);
                queryESObject.setSystemName(SYSTEM_NAME);
                queryESObject.setTypeName(TYPE_NAME);
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setFieldName("age");
                searchCondition.setSingleValue(23);
                searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
                List<SearchCondition> list = Lists.newArrayList(searchCondition);
                queryESObject.setSearchConditions(list);

                DataResult<ESResponse> result = esSearchController.esQuery(queryESObject);
                List<ESDocument> documents = result.getResult().getEsDocuments();
                for (int i = 0; i < documents.size(); i++) {
                    System.out.println("--->>>>>" + documents.get(i).toString());
                }
            }

            Assert.assertTrue(true);
            UpdateESObject updateESObject = new UpdateESObject();
            updateESObject.setSystemName(SYSTEM_NAME);
            updateESObject.setTypeName(TYPE_NAME);
            updateESObject.setIndexName(INDEX_NAME);
            updateESObject.setNestedOperateType(OperateTypeEnum.UPDATE);
            updateESObject.setUkMap(ukMap);
            updateESObject.setDataMap(dataMap);
            DataResult<Boolean> result = esSearchController.esUpdate(updateESObject);
            System.out.println(result.isSuccess() + "-->" + result.getResult());

            DeleteESObject deleteESObject = new DeleteESObject();
            deleteESObject.setIndexName(INDEX_NAME);
            deleteESObject.setSystemName(SYSTEM_NAME);
            deleteESObject.setTypeName(TYPE_NAME);
            try {
                deleteESObject.setUkMap(ukMap);
                DataResult<Boolean> delRes = esSearchController.esDelete(deleteESObject);
                System.out.println(delRes.isSuccess() + "---->>>>" + delRes.getResult());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Assert.fail(dataResult.getStatus().getStatusCode() + ":" + dataResult.getStatus().getStatusReason());
        }
    }

    @Test // 单一删除
    public void deleteTest() throws Exception {
        Map<Object, Object> objectObjectMap = new HashMap<>();
        objectObjectMap.put("id", -881143142);
        deleteESObject.setUkMap(objectObjectMap);
        final DataResult<Boolean> dataResult = esSearchController.esDelete(deleteESObject);

        System.out.println("------->" + dataResult.getResult());
    }

    @Test // 批量删除
    public void batchDeleteTest() {
        BatchDeleteESObject batchDeleteESObject = new BatchDeleteESObject();
        List<DeleteESObject> deleteESObjects = new ArrayList<>();

        DeleteESObject deleteESObject1 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
        Map<Object, Object> objectObjectMap1 = new HashMap<>();
        objectObjectMap1.put("id", 2085151053);
        deleteESObject1.setUkMap(objectObjectMap1);
        deleteESObjects.add(deleteESObject1);

        DeleteESObject deleteESObject2 = new DeleteESObject(SYSTEM_NAME, INDEX_NAME, TYPE_NAME);
        Map<Object, Object> objectObjectMap2 = new HashMap<>();
        objectObjectMap2.put("id", -668283549);
        deleteESObject2.setUkMap(objectObjectMap2);
        deleteESObjects.add(deleteESObject2);

        batchDeleteESObject.setDeleteDatas(deleteESObjects);

        final DataResult<Boolean> dataResult = esSearchController.esBatchDelete(batchDeleteESObject);
        System.out.println("------->" + dataResult.getResult());
    }

    @Test // 根据条件删除
    public void conditionDeleteTest() throws Exception {
        ConditionDeleteESObject conditionDeleteESObject = new ConditionDeleteESObject();
        List<SearchCondition> searchConditions = new ArrayList<>();
        SearchCondition searchCondition = new SearchCondition.Builder().setFieldName("age")
                .setConditionExpression(ConditionExpressionEnum.EQUAL).setSingleValue("35")
                .build();
        searchConditions.add(searchCondition);
        conditionDeleteESObject.setConditions(searchConditions);

        conditionDeleteESObject.setIndexName(INDEX_NAME);
        conditionDeleteESObject.setTypeName(TYPE_NAME);
        conditionDeleteESObject.setSystemName(SYSTEM_NAME);

        final DataResult<Boolean> dataResult = esSearchController.conditionDelete(conditionDeleteESObject);
        System.out.println("------->" + dataResult.getStatus());
    }

    @Test
    public void testQuery() {
        QueryESObject queryESObject = new QueryESObject();
        queryESObject.setIndexName("tms-waybill");
        queryESObject.setSystemName(SYSTEM_NAME);
        queryESObject.setTypeName("tms-waybill-list");
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setFieldName("driverName");
        searchCondition.setSingleValue("否");
        searchCondition.setConditionExpression(ConditionExpressionEnum.EQUAL);
        List<SearchCondition> list = Lists.newArrayList(searchCondition);
        queryESObject.setSearchConditions(list);

        DataResult<ESResponse> result = esSearchController.esQuery(queryESObject);
        List<ESDocument> documents = result.getResult().getEsDocuments();
        for (int i = 0; i < documents.size(); i++) {
            System.out.println("--->>>>>" + documents.get(i).toString());
        }
    }

}
