package com.hivescm.escenter.evo;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @Author ZHJ
 * @Date 2017/12/6
 */
public class ESQueryBuilder {

    private QueryBuilder queryBuilder;

    public ESQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public static ESQueryBuilder match(String name, Object value) {
        QueryBuilder builder = QueryBuilders.matchQuery(name, value);
        return new ESQueryBuilder(builder);
    }

    public static ESQueryBuilder lt(String name, Object value) {
        QueryBuilder builder = QueryBuilders.rangeQuery(name).lt(value);
        return new ESQueryBuilder(builder);
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}
