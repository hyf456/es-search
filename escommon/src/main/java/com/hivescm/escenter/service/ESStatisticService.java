package com.hivescm.escenter.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.CollapseQueryObject;
import com.hivescm.escenter.common.ESResponse;
import com.hivescm.escenter.common.StatisticESObject;

/**
 * Created by DongChunfu on 2017/8/29
 * <p>
 * ES 统计相关服务
 */
//@FeignClient(value = "escenter")
public interface ESStatisticService {

	/**
	 * 全局统计服务，不受分页效果影响
	 *
	 * @param esObject ES 统计请求参数
	 * @return key:functionName;value:statistic value
	 */
//	@RequestMapping(value = "/statisticByConditions", method = RequestMethod.POST)
	DataResult<Map<String, Number>> statisticByConditions(@RequestBody StatisticESObject esObject);

	/**
	 * 按 field value 去重，返回指定数目的 doc
	 *
	 * @param esObject 去重请求参数
	 * @return ES 通用响应结果
	 */
//	@RequestMapping(value = "/collapse", method = RequestMethod.POST)
	DataResult<Map<String, ESResponse>> collapse(@RequestBody CollapseQueryObject esObject);
}
