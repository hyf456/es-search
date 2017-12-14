package com.hivescm.escenter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivescm.escenter.common.BaseESObject;
import com.hivescm.escenter.common.SaveESObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by DongChunfu on 2017/7/27
 * <p>
 * ES 工具类
 */
public class ESUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESUtil.class);

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 根据 {@link SaveESObject#ukMap}生成Document的唯一ID
	 *
	 * @param ukMap {@link SaveESObject#ukMap}
	 * @return 如果ukMap为空，生成随机ID
	 */
	public static String generateUniquekey(Map<Object, Object> ukMap) {
		if (ukMap == null || ukMap.isEmpty()) {
			ukMap = new HashMap<>();
			ukMap.put("random", UUID.randomUUID().toString().replaceAll("-", ""));
		}
		return MD5Util.MD5(ukMap.toString());
	}

	/**
	 * 生成 redis key
	 * （systemName ： indexName ： typeName ）
	 *
	 * @param obj ES 基础请求参数
	 * @return redis key
	 */
	public static String redisRepetitionKey(BaseESObject obj) {
		return new StringBuilder().append(obj.getSystemName()).append(":")
				.append(obj.getIndexName()).append(":")
				.append(obj.getTypeName()).toString();
	}
}
