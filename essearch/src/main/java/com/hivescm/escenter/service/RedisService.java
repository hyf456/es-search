//package com.hivescm.escenter.service;
//
//import com.hivescm.cache.client.JedisClient;
//import com.hivescm.escenter.common.SaveESObject;
//import com.hivescm.escenter.util.ESUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
///**
// * Created by dongchunfu on 2017/7/20.
// * <p>
// * es 依赖redis服务
// */
//@Service(value = "redisService")
//public class RedisService {
//
//	@Autowired
//	private JedisClient<String> jedisClient;
//
//	/**
//	 * 验证一个值是否已存储在对应key
//	 *
//	 * @param redisKey 值对应的key
//	 * @param value    值
//	 * @return <code>true</code> 已经存在；<code>false</code> key为空，或者不存在
//	 */
//	public boolean valueExist(String redisKey, String value) {
//		if (null == redisKey || redisKey.isEmpty()) {
//			return false;
//		}
//		return jedisClient.sadd(redisKey, value) == 0;
//	}
//
//	/**
//	 * 判断一个文档是否需要进行索引或更新操作
//	 * 索引，若文档唯一标识已在缓存中存在，则返回false，不进行操作；
//	 * 更新，若文档唯一标识不在缓存中，则返回false，不进行操作。
//	 *
//	 * @param redisKey
//	 * @param obj
//	 * @return
//	 */
//	public boolean needSendMq(String redisKey, SaveESObject obj) {
//		String docId = ESUtil.generateUniquekey(obj.getUkMap());
//		return jedisClient.sadd(redisKey, docId) == 1;
//	}
//
//	/**
//	 * 因发消息失败，回滚之前的redis判重操作
//	 *
//	 * @param redisKey
//	 * @param obj
//	 * @return
//	 * @throws IOException
//	 */
//	/*public boolean deleteJudgeRepetitiveMq(String redisKey, SaveESObject obj) throws IOException {
//		String docSign = ESUtil.getDocUniqueSign(obj);
//		return false;
//	}*/
//}
