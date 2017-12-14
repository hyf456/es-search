package com.hivescm.escenter.service;

import org.springframework.web.bind.annotation.RequestBody;

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

/**
 * Created by DongChunfu on 2017/8/03
 * <p>
 * 搜索引擎，feign 客户端调用API
 */
//@FeignClient(value = "escenter")
public interface ESSearchService {

	/**
	 * 新增操作
	 *
	 * @param obj 存储请求参数
	 * @return <code>true</code>操作成功，<code>false</code>重复操作
	 */
//	@RequestMapping(value = "/esSave", method = RequestMethod.POST)
	DataResult<Boolean> esSave(@RequestBody SaveESObject obj);

	/**
	 * 查询操作
	 *
	 * @param obj 查询参数
	 * @return 检索到的文档集
	 */
//	@RequestMapping(value = "/esQuery", method = RequestMethod.POST)
	DataResult<ESResponse> esQuery(@RequestBody final QueryESObject obj);

	/**
	 * 删除操作（根据 {@link SaveESObject#ukMap} 删除文档）
	 *
	 * @param obj 删除请求参数
	 * @return <code>true</code>操作成功，<code>false</code>操作失败
	 */
//	@RequestMapping(value = "/esDelete", method = RequestMethod.DELETE)
	DataResult<Boolean> esDelete(@RequestBody final DeleteESObject obj);

	/**
	 * 更新操作（根据 {@link SaveESObject#ukMap} 更新文档）
	 * <p>
	 * 1：该操作会根据{@link UpdateESObject#dataMap} 的key-value键值对更新es文档 ，当前key不存在进行新增操作，当key存在进行更新操作。
	 * 2：escommon 提供{@link com.hivescm.escenter.convert.ESSearchConvertor#object2MapExcludeNullValue(Object)}方法，
	 * 可将map中value==null值过滤掉，注意基本类型默认值问题
	 *
	 * @param obj 参数，具体参见参数属性说明
	 * @return <code>true</code>操作成功，<code>false</code>操作失败
	 */
//	@RequestMapping(value = "/esUpdate", method = RequestMethod.PUT)
	DataResult<Boolean> esUpdate(@RequestBody final UpdateESObject obj);


	/**
	 * 批量新增操作
	 *
	 * @param obj 批量新增请求参数，参照{@link #esSave(SaveESObject)}
	 * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
	 */
//	@RequestMapping(value = "/esBatchSave", method = RequestMethod.PUT)
	DataResult<Boolean> esBatchSave(@RequestBody final BatchSaveESObject obj);

	/**
	 * 批量更新操作（根据 {@link SaveESObject#ukMap} 更新文档）
	 *
	 * @param obj 批量更新请求参数，参照{@link #esUpdate(UpdateESObject)}
	 * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
	 */
//	@RequestMapping(value = "/esBatchUpdate", method = RequestMethod.PUT)
	DataResult<Boolean> esBatchUpdate(@RequestBody final BatchUpdateESObject obj);

	/**
	 * 批量删除操作（根据 {@link DeleteESObject#ukMap} 删除文档）
	 *
	 * @param obj 批量删除请求参数，参照{@link #esDelete(DeleteESObject)}
	 * @return <code>true</code>全部操作成功，<code>false</code>部分操作失败
	 */
//	@RequestMapping(value = "/esBatchDelete", method = RequestMethod.PUT)
	DataResult<Boolean> esBatchDelete(@RequestBody final BatchDeleteESObject obj);

	/**
	 * 按条件进行更新（此 {@link SaveESObject#ukMap}条件废弃）
	 *
	 * @param obj 更新请求参数
	 * @return <code>true</code>操作成功，<code>false</code>操作失败
	 */
//	@RequestMapping(value = "/conditionUpdate", method = RequestMethod.PUT)
	DataResult<Boolean> conditionUpdate(@RequestBody final ConditionUpdateESObject obj);

	/**
	 * 按条件删除操作（此 {@link SaveESObject#ukMap}条件废弃）
	 *
	 * @param obj 删除请求参数
	 * @return <code>true</code>操作成功，<code>false</code>操作失败
	 */
//	@RequestMapping(value = "/conditionDelete", method = RequestMethod.DELETE)
	DataResult<Boolean> conditionDelete(@RequestBody final ConditionDeleteESObject obj);
}
