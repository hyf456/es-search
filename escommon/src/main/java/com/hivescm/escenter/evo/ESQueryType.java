//package com.hivescm.escenter.evo;
//
//import com.hivescm.common.exception.ErrorCode;
//import com.hivescm.common.exception.OpenSystemException;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//
///**
// * @Author ZHJ
// * @Date 2017/12/6
// */
//public enum ESQueryType {
//
//    MATCH("match query");
//
//    private String desc;
//
//    ESQueryType(String desc) {
//        this.desc = desc;
//    }
//
//    public QueryBuilder getQueryBuilder(String name, Object text) {
//        if (this == ESQueryType.MATCH) {
//            return QueryBuilders.matchQuery(name, text);
//        }
//        throw new OpenSystemException(ErrorCode.OTHER_ERROR.getErrorCode(),
//                "QueryBuilder not found|ESQueryType:" + this);
//    }
//}
