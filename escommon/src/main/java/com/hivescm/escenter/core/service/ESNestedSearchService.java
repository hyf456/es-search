package com.hivescm.escenter.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivescm.common.domain.DataResult;
import com.hivescm.common.domain.Status;
import com.hivescm.escenter.ESErrorCode;
import com.hivescm.escenter.common.*;
import com.hivescm.escenter.common.conditions.SearchCondition;
import com.hivescm.escenter.core.condition.GroupConditionBuilder;
import com.hivescm.escenter.core.condition.QueryConditionBuilder;
import com.hivescm.escenter.core.condition.SerchSourceBuilder;
import com.hivescm.escenter.core.groovy.NestedUpdateGroovyScritpBuilder;
import com.hivescm.escenter.core.handler.ESQueryResponseHandler;
import com.hivescm.search.log.SearchLogger;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by DongChunfu on 2017/7/26
 * <p>
 * ES 嵌套数据类型 CRUD 操作
 */
@Service(value = "esNestedSearchService")
public class ESNestedSearchService {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Value(value = "${escenter.excute.explain:false}")
	private boolean explain;
	@Value(value = "${escenter.doc.as.upsert:false}")
	private boolean docAsUpsert;
	@Value(value = "${escenter.search.scroll.seconds:60}")
	private long scrollTime;
	@Value(value = "${escenter.search.scroll.size:20}")
	private int perShardSize;
	@Value(value = "${escenter.search.bulk.size:20}")
	private int bulkSize;

	@Resource
	private TransportClient esClient;

	@Resource
	private ESQueryResponseHandler esQueryResponseHandler;

	@Resource
	private QueryConditionBuilder queryConditionBuilder;

	@Resource
	private GroupConditionBuilder groupConditionBuilder;

	@Resource
	private NestedUpdateGroovyScritpBuilder nestedUpdateGroovyScritpBuilder;

	@Resource
	private SerchSourceBuilder serchSourceBuilder;

	/**
	 * 存储通用对象
	 *
	 * @param esObject es通用存储数据结构
	 * @return <code>true</code> 保存成功，<code>false</code>保存失败;
	 */
	public DataResult<Boolean> save(SaveESObject esObject) {
		try {
			final IndexRequestBuilder indexRequest = getIndexRequest(esObject);
			SearchLogger.log(indexRequest);
			if (esObject.isRefresh()) {
				indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
			}
			IndexResponse indexResponse = indexRequest.execute().get();
			SearchLogger.log(indexResponse);
			return DataResult.success(DocWriteResponse.Result.CREATED == indexResponse.getResult(), Boolean.class);
		} catch (Exception ex) {
			SearchLogger.error("save", ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage());
		}
	}

	/**
	 * 更新操作
	 *
	 * @param esObject es通用更新请求参数
	 * @return <code>true</code> 保存或更新成功; 否则更新失败
	 */
	public DataResult<Boolean> update(UpdateESObject esObject) {
		final UpdateRequestBuilder updateRequest = esObject.nestedUpdate() ? getNestedListUpdateRequest(esObject)
				: getUpdateRequest(esObject);
		SearchLogger.log(updateRequest);
		try {
			updateRequest.setDetectNoop(false);
			if (esObject.isRefresh()) {
				updateRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
			}
			UpdateResponse updateResponse = updateRequest.execute().get();
			SearchLogger.log(updateResponse);
			final DocWriteResponse.Result result = updateResponse.getResult();
			return DataResult.success(DocWriteResponse.Result.UPDATED == result, Boolean.class);
		} catch (Exception ex) {
			SearchLogger.error("update", ex);
			final String message = ex.getMessage();
			if (message != null && message.contains("document missing")) {
				return DataResult.faild(ESErrorCode.DOC_NOT_EXIST_ERROR_CODE, "更新文档不存在");
			}
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + message);
		}
	}

	/**
	 * 基础查询，根据field进行and 或 or 查询
	 *
	 * @param esObject 基础查询参数
	 */
	public DataResult<ESResponse> query(final QueryESObject esObject) {
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch().setIndices(esObject.getIndexName())
				.setTypes(esObject.getTypeName()).setExplain(explain);

		SearchResponse searchResponse;
		try {
			serchSourceBuilder.builde(searchRequestBuilder, esObject);

			queryConditionBuilder.builde(searchRequestBuilder, esObject);

			groupConditionBuilder.build(searchRequestBuilder, esObject);
			// searchRequestBuilder 使用 Gson 回环
			SearchLogger.log(searchRequestBuilder);
			searchResponse = searchRequestBuilder.execute().actionGet();
			SearchLogger.log(searchResponse);

		} catch (IndexNotFoundException infex) {
			SearchLogger.error("query", infex);
			return DataResult.faild(ESErrorCode.INDEX_NOT_EXIST_ERROR_CODE, "索引不存在");
		} catch (SearchPhaseExecutionException spex) {
			SearchLogger.error("query", spex);
			return DataResult.faild(ESErrorCode.QUERY_PHASE_ERROR_CODE, "搜索语法异常");
		} catch (Exception ex) {
			SearchLogger.error("query", ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "搜索引擎异常");
		}

		try {
			final ESResponse handlerResponse = esQueryResponseHandler.handler(esObject, searchResponse);
			SearchLogger.log(handlerResponse);
			return DataResult.success(handlerResponse, ESResponse.class);
		} catch (Exception ex) {
			SearchLogger.error("query", ex);
			return DataResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "ecMsg:" + ex.getMessage());
		}
	}

	/**
	 * 根据文档唯一Id删除指定的文档
	 *
	 * @param esObject 删除参数
	 * @return <code>true</code>文档删除成功,<code>true</code>未找到对应文档.
	 */
	public DataResult<Boolean> delete(final DeleteESObject esObject) {
		final DeleteRequestBuilder deleteRequest = getDeleteRequest(esObject);
		try {
			SearchLogger.log(deleteRequest);
			if(esObject.isRefresh()){
				deleteRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
			}
			DeleteResponse deleteResponse = deleteRequest.execute().actionGet();
			SearchLogger.log(deleteRequest);
			if (DocWriteResponse.Result.DELETED == deleteResponse.getResult()) {
				return DataResult.success(Boolean.TRUE, Boolean.class);
			}
			if (DocWriteResponse.Result.NOT_FOUND == deleteResponse.getResult()) {
				return DataResult.success(Boolean.FALSE, Boolean.class);
			}
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "ES返回结果不在预期范围内");
		} catch (Exception ex) {
			SearchLogger.error("delete", ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage());
		}

	}

	/**
	 * 批量更新服务
	 *
	 * @param obj 批量新增请求参数
	 * @return <code>true</code>全部更新成功,<code>false</code>部分更新失败.
	 * @throws Exception es 执行异常
	 */
	public DataResult<Boolean> batchSave(final BatchSaveESObject obj) {
		final BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
		final List<SaveESObject> saveDatas = obj.getSaveDatas();
		for (SaveESObject esObject : saveDatas) {
			bulkRequestBuilder.add(getIndexRequest(esObject));
		}
		if (obj.isRefresh()) {
			bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
		}
		try {
			SearchLogger.log(bulkRequestBuilder);
			BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
			SearchLogger.log(bulkResponse);
			return DataResult.success(!bulkResponse.hasFailures(), Boolean.class);
		} catch (Exception ex) {
			SearchLogger.error("batchSave", ex);
			return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage());
		}
	}

	/**
	 * 批量更新服务
	 *
	 * @param obj 批量更新请求参数
	 * @return <code>true</code>全部更新成功,<code>false</code>部分更新失败.
	 * @throws Exception es 执行异常
	 */
	public DataResult<Boolean> batchUpdate(final BatchUpdateESObject obj) {
		BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
		final List<UpdateESObject> updateDatas = obj.getUpdateDatas();
		boolean result = true;
		final int size = updateDatas.size();
		for (int i = 0; i < size; i++) {
			try {
				bulkRequestBuilder.add(getUpdateRequest(updateDatas.get(i)));
				if (i % bulkSize == 0 || (i + 1) == size) {
					SearchLogger.log(bulkRequestBuilder);
					if (obj.isRefresh()) {
						bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
					}
					final BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
					SearchLogger.log(bulkResponse);
					if (bulkResponse.hasFailures()) {
						result = false;
					}
					// bulkRequestBuilder = esClient.prepareBulk();
				}
			} catch (Exception ex) {
				SearchLogger.error("batchUpdate", ex);
				return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage());
			}
		}
		return DataResult.success(result, Boolean.class);
	}

	/**
	 * 批量更新服务
	 *
	 * @param obj 批量更新请求参数
	 * @return <code>true</code>全部更新成功,<code>false</code>部分更新失败.
	 */
	public DataResult<Boolean> batchDelete(final BatchDeleteESObject obj) {
		BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
		List<DeleteESObject> deleteDatas = obj.getDeleteDatas();
		boolean result = true;
		final int size = deleteDatas.size();
		for (int i = 0; i < size; i++) {
			try {
				bulkRequestBuilder.add(getDeleteRequest(deleteDatas.get(i)));
				if (i % bulkSize == 0 || (i + 1) == size) {
					SearchLogger.log(bulkRequestBuilder);
					if (obj.isRefresh()) {
						bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
					}
					final BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
					SearchLogger.log(bulkResponse);
					if (bulkResponse.hasFailures()) {
						result = false;
					}
					// bulkRequestBuilder = esClient.prepareBulk();
				}
			} catch (Exception ex) {
				SearchLogger.error("batchDelete", ex);
				return DataResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage());
			}

		}
		return DataResult.success(result, Boolean.class);
	}

	/**
	 * 按条件更新文档
	 *
	 * @param esObject 更新请求参数
	 * @return <code>true</code>全部更新成功,<code>false</code>部分更新失败.
	 */
	public DataResult<Boolean> conditionUpdate(final ConditionUpdateESObject esObject) {
		DataResult<Boolean> dataResult = new DataResult<>();
		BulkResponse bulkResponse;
		final BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
		try {
			final List<String> docIds = getAccordConditionDocIds(esObject.getConditions(), esObject);
			if (CollectionUtils.isEmpty(docIds)) {
				SearchLogger.log(bulkRequestBuilder);
				dataResult.setResult(Boolean.TRUE);
				return dataResult;
			}

			for (String docId : docIds) {
				final UpdateRequestBuilder updateRequestBuilder = esClient.prepareUpdate(esObject.getIndexName(),
						esObject.getTypeName(), docId);
				updateRequestBuilder.setDocAsUpsert(docAsUpsert);
				updateRequestBuilder.setDoc(esObject.getDataMap());
				bulkRequestBuilder.add(updateRequestBuilder);
			}
			if (esObject.isRefresh()) {
				bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
			}
			SearchLogger.log(bulkRequestBuilder);
			bulkResponse = bulkRequestBuilder.execute().get();
			SearchLogger.log(bulkResponse);
		} catch (Exception ex) {
			SearchLogger.error("conditionUpdate", ex);
			dataResult.setStatus(new Status(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage()));
			return dataResult;
		}
		dataResult.setResult(!bulkResponse.hasFailures());

		return dataResult;
	}

	/**
	 * 按条件删除文档
	 *
	 * @param esObject 删除请求参数
	 * @return <code>true</code>全部删除成功,<code>false</code>部分删除失败.
	 */
	public DataResult<Boolean> conditionDelete(final ConditionDeleteESObject esObject) {
		DataResult<Boolean> dataResult = new DataResult<>();
		BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
		BulkResponse bulkResponse;
		try {
			final List<String> docIds = getAccordConditionDocIds(esObject.getConditions(), esObject);
			if (CollectionUtils.isEmpty(docIds)) {
				SearchLogger.log(bulkRequestBuilder);
				dataResult.setResult(Boolean.TRUE);
				return dataResult;
			}

			for (String docId : docIds) {
				final DeleteRequestBuilder deleteRequestBuilder = esClient.prepareDelete(esObject.getIndexName(),
						esObject.getTypeName(), docId);
				bulkRequestBuilder.add(deleteRequestBuilder);
			}
			if (esObject.isRefresh()) {
				bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
			}
			SearchLogger.log(bulkRequestBuilder);
			bulkResponse = bulkRequestBuilder.execute().get();
			SearchLogger.log(bulkResponse);
		} catch (Exception ex) {
			SearchLogger.error("conditionDelete", ex);
			dataResult.setStatus(new Status(ESErrorCode.ELASTIC_ERROR_CODE, "esMsg:" + ex.getMessage()));
			return dataResult;
		}
		dataResult.setResult(!bulkResponse.hasFailures());
		return dataResult;
	}

	/**
	 * 获取符合条件的文档ID集合
	 *
	 * @param conditions 筛选条件
	 * @param esObject ES基础请求参数
	 * @return 文档ID集合
	 * @throws ExecutionException es 执行异常
	 * @throws InterruptedException es 执行异常
	 */
	private List<String> getAccordConditionDocIds(List<SearchCondition> conditions, BaseESObject esObject)
			throws ExecutionException, InterruptedException {

		QueryESObject queryESObject = new QueryESObject(esObject.getSystemName(), esObject.getIndexName(),
				esObject.getTypeName());
		queryESObject.setSearchConditions(conditions);

		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch().setIndices(esObject.getIndexName())
				.setTypes(esObject.getTypeName()).setExplain(explain).setSize(perShardSize).setFetchSource(Boolean.TRUE);

		queryConditionBuilder.builde(searchRequestBuilder, queryESObject);
		searchRequestBuilder.setScroll(new Scroll(TimeValue.timeValueSeconds(scrollTime)));

		SearchLogger.log(searchRequestBuilder);
		final SearchResponse searchResponse = searchRequestBuilder.execute().get();
		SearchLogger.log(searchResponse);
		final SearchHits hits = searchResponse.getHits();
		if (hits.getHits().length == 0) {
			return null;
		}

		List<String> docIds = new ArrayList<>();
		for (SearchHit hit : hits) {
			docIds.add(hit.getId());
		}
		scrollSearch(searchResponse.getScrollId(), docIds, new ArrayList<>());
		return docIds;
	}

	/**
	 * 启用游标查询，避免单次查询大批量数据
	 *
	 * @param scrollId 游标ID
	 * @param docIds 文档ID集合
	 */
	private void scrollSearch(String scrollId, List<String> docIds, List<String> scrollIds) {

		scrollIds.add(scrollId);
		final SearchScrollRequestBuilder searchScrollRequestBuilder = esClient.prepareSearchScroll(scrollId);
		searchScrollRequestBuilder.setScroll(TimeValue.timeValueSeconds(scrollTime));

		final SearchResponse searchResponse = searchScrollRequestBuilder.execute().actionGet();

		final SearchHits hits = searchResponse.getHits();
		if (hits.getHits().length == 0) {
			if (!CollectionUtils.isEmpty(scrollIds)) {
				closeScrollWindow(scrollIds);
			}
			return;
		}

		for (SearchHit hit : hits) {
			docIds.add(hit.getId());
		}
		scrollSearch(searchResponse.getScrollId(), docIds, scrollIds);
	}

	/**
	 * 主动清除游标窗，避免资源浪费
	 *
	 * @param scrollIds 游标ID
	 */
	private void closeScrollWindow(List<String> scrollIds) {
		final ClearScrollRequestBuilder clearScrollRequestBuilder = esClient.prepareClearScroll();
		clearScrollRequestBuilder.setScrollIds(scrollIds);
		final ClearScrollResponse clearScrollResponse = clearScrollRequestBuilder.execute().actionGet();
		SearchLogger.log(clearScrollResponse);
	}

	/**
	 * 获取更新请求
	 *
	 * @param updateData 更新请求参数
	 * @return UpdateRequestBuilder 更新请求
	 */
	private UpdateRequestBuilder getUpdateRequest(UpdateESObject updateData) {
		String dataId = getId(updateData.getUkMap());
		final String indexName = updateData.getIndexName();
		final String typeName = updateData.getTypeName();
		final UpdateRequestBuilder updateRequestBuilder = esClient.prepareUpdate(indexName, typeName, dataId);
		updateRequestBuilder.setDocAsUpsert(docAsUpsert);
		updateRequestBuilder.setDoc(updateData.getDataMap());
		return updateRequestBuilder;
	}

	/**
	 * 获取list嵌套更新请求
	 *
	 * @param updateData ES更新请求参数
	 * @return 更新请求
	 */
	private UpdateRequestBuilder getNestedListUpdateRequest(UpdateESObject updateData) {
		final Script script = nestedUpdateGroovyScritpBuilder.build(updateData.getNestedESObject(),
				updateData.getNestedOperateType(), updateData.getDataMap());
		String dataId = getId(updateData.getUkMap());
		UpdateRequestBuilder builder = esClient.prepareUpdate().setIndex(updateData.getIndexName())
				.setType(updateData.getTypeName()).setId(dataId).setScript(script);
		if (updateData.isRefresh()) {
			builder.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
		}
		return builder;
	}

	/**
	 * 获取新增请求
	 *
	 * @param esObject 更新请求参数
	 * @return UpdateRequestBuilder 更新请求
	 */
	private IndexRequestBuilder getIndexRequest(SaveESObject esObject) {
		String dataId = getId(esObject.getUkMap());

		byte[] dataBytes = null;
		try {
			dataBytes = OBJECT_MAPPER.writeValueAsBytes(esObject.getDataMap());
		} catch (JsonProcessingException e) {
			// never hapened
			SearchLogger.error("", e);
		}

		IndexRequestBuilder indexRequestBuilder = esClient.prepareIndex().setIndex(esObject.getIndexName())
				.setType(esObject.getTypeName());
		if (StringUtils.isNotBlank(dataId)) {
			indexRequestBuilder.setId(dataId);
		}
		// TODO 替换
		// indexRequestBuilder.setSource(esObject.getDataMap());
		indexRequestBuilder.setSource(dataBytes, XContentType.JSON);
		return indexRequestBuilder;
	}

	/**
	 * 获取删除请求
	 *
	 * @param deleteData 删除请求参数
	 * @return UpdateRequestBuilder 删除请求
	 */
	private DeleteRequestBuilder getDeleteRequest(DeleteESObject deleteData) {
		String docId = getId(deleteData.getUkMap());
		DeleteRequestBuilder builder = esClient.prepareDelete(deleteData.getIndexName(), deleteData.getTypeName(), docId);
		return builder;
	}

	private String getId(Map<Object, Object> ukMap) {
		return CollectionUtils.isEmpty(ukMap) || !ukMap.containsKey("id") ? null : String.valueOf(ukMap.get("id"));
	}
}
