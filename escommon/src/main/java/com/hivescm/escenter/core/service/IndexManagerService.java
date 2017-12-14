//package com.hivescm.escenter.core.service;
//
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//
///**
// * Created by DongChunfu on 2017/8/22
// */
//public class IndexManagerService {
//
////	/ delete complete index
////client.admin().indices().delete(new DeleteIndexRequest("<indexname>")).actionGet();
////
////	// delete a type in index
////client.prepareDelete().setIndex("<indexname>").setType("<typename>").setId("*").execute().actionGet();
////
////	// delete a particular document
////client.prepareDelete().setIndex("<indexname>").setType("<typename>").setId("<documentId>").execute().actionGet();
////
////	// or
////	DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
////			.execute()
////			.actionGet();
//}
