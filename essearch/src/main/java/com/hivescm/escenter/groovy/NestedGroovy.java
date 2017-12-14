//package com.hivescm.escenter.groovy;
//
//import org.elasticsearch.script.Script;
//import org.elasticsearch.script.ScriptService;
//import org.mortbay.util.ajax.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//
///**
// * Created by DongChunfu on 2017/8/17
// */
//@Component(value = "nestedGroovy")
//public class NestedGroovy {
//	private static final Logger LOGGER = LoggerFactory.getLogger(NestedGroovy.class);
//
//	/**
//	 * 嵌套类型，新增元素
//	 *
//	 * @return
//	 */
//
//	/**
//	 * √
//	 curl -XPOST '10.12.31.110:9200/escenter/test/%7B"id":1749435324%7D/_update?pretty' -d'
//	 {
//	 "script" : "ctx._source.hobby+=new_tag",
//	 "params" : {
//	 "new_tag" : "search"
//	 }
//	 }'
//	 */
//
////	curl -XPOST '10.12.31.110:9200/escenter/test/%7B"id":1749435324%7D/_update?pretty' -d'
////	{
////		"script" : "ctx._source.list+=new_tag",
////			"params" : {
////		"new_tag" : {"id":10,"name":"王宇","date":"1980-09-10"}
////	}
////	}
////'
//	public String nestedAdd(String fieldName, String newTag) {
//		// ctx._source.tags+=new_tag
//		StringBuilder sb = new StringBuilder();
//		sb.append("ctx._source.");
//		sb.append(fieldName);
//		sb.append("+=");
//		sb.append(newTag);
//		final String addInline = sb.toString();
//		LOGGER.debug("add groovy script:{}.", addInline);
//		return addInline;
//	}
//
//	/**
//	 * 根据嵌套ID删除指定的嵌套内容
//	 *
//	 * @param fieldName
//	 * @param id
//	 * @return
//	 */
//	/**
//	 * script: 'ctx._source.tags.contains(tag) ? ctx.op = "delete" : ctx.op = "none"',
//	 params: {
//	 tag: 'to-delete'
//	 }
//	 * @param fieldName
//	 * @param id
//	 * @return
//	 */
//	public String nesteDeleteById(String fieldName, String id) {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("ctx.op =ctx._source.");
//		sb.append(fieldName);
//		sb.append("==");
//		sb.append(id);
//		sb.append("? 'delete' : 'none'");
//		final String deleteInlineById = sb.toString();
//		LOGGER.debug("delete by id groovy script:{}.", deleteInlineById);
//		return deleteInlineById;
//	}
//
//	//在第二层嵌套数据里面添加一条新的数据到quests中
//	private void add(){
//
//		/*StringBuffer sb_json = new StringBuffer("ctx._source.quests +=  quest");//脚本主体
//
//		HashMap<String, Object> params = new java.util.HashMap<String, Object>()//Map组装
//
//		params.put("quest", JSON.toJSON(user.getQuests))//此处不能用JSON.toJSON(user.getQuests).toString方法，quest必须是一个对象，否则会报错
//
//		Script script = new Script(sb_json.toString(), ScriptService.ScriptType.INLINE, "groovy", params)//组装脚本
//
//		client.prepareUpdate(dynamicIndexName, typeName, uid).setScript(script).execute().get() //发送请求*/
//
//	}
//
//	//在第二层嵌套数据里面删除一条quests数据
//	private void test(){
//		/*StringBuffer sb_json = new StringBuffer("ctx._source.quests.removeAll{it.qid == remove_id}");
//		java.util.HashMap<String, Object> params = new java.util.HashMap<String, Object>();
//		//此处不能用JSON.toJSON(user.getQuests).toString方法，quest必须是一个对象，否则会报错
//		params.put("remove_id", "qid2");
//		Script script = new Script(sb_json.toString(), ScriptService.ScriptType.INLINE, "groovy", params);
//		client.prepareUpdate(dynamicIndexName, typeName, uid).setScript(script).execute().get();*/
//	}
//
//	//在第三层嵌套数据里面添加一条Kp数据
//	private void t1(){
//		/*StringBuffer sb_json = new StringBuffer("ctx._source.quests.findAll {  if(it.qid==qid2||it.qid==qid3) {  it.kps += kp5 } }  ");
//		java.util.HashMap<String, Object> params = new java.util.HashMap<String, Object>();
//		params.put("qid2", "qid2");
//		params.put("qid3", "qid3");
//		params.put("kp5",JSON.toJSON(kp1));
//		Script script = new Script(sb_json.toString(), ScriptService.ScriptType.INLINE, "groovy", params);
//		client.prepareUpdate(dynamicIndexName, typeName, uid).setScript(script).execute().get();*/
//
//	}
//	//在第三层嵌套数据里面删除一条Kp数据
//
//	private void t3(){
//		/*StringBuffer sb_json = new StringBuffer("ctx._source.quests.findAll {  if(it.qid==qid2||it.qid==qid3) {  it.kps.removeAll {it.kid==kid}  } }  "); //删除第三层数据
//		java.util.HashMap<String, Object> params = new java.util.HashMap<String, Object>();
//		params.put("qid2", "qid2");
//		params.put("qid3", "qid3");
//		params.put("kid", "kid3");
//		Script script = new Script(sb_json.toString(), ScriptService.ScriptType.INLINE, "groovy", params);
//		client.prepareUpdate(dynamicIndexName, typeName, uid).setScript(script).execute().get();*/
//	}
//
//}
